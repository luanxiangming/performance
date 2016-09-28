#!/usr/bin/node

var mysql = require("mysql");
var lazy = require("lazy");
var fs = require("fs");
var async = require("async");
var emitter = require('events').EventEmitter;
var conn = null;
var safeToCloseMySQL = false;


function fillTest() {
    conn = getConn();
    conn.connect(function(err) {
        if (err) {
            console.error(err);
            return;
        }
        var value = [[process.argv[7], process.argv[8]]];
        conn.query("INSERT INTO test (timestamp, end_time) VALUES ?", [value],
                   function(err, result) {
            if (err) {
                console.error(err);
                return;
            }
            console.log("test info inserted");
            conn.end();
            fillRun_log(process.argv[7]);
        });
    });
}


function fillRun_log(test_id) {
    conn = getConn();
    conn.connect(function(err) {
        if (err) {
            console.error(err);
            return;
        }
        var scriptFile = process.argv[2] + '/script.txt';
        var scriptRun = [];
        var value = [];

        new lazy(fs.createReadStream(scriptFile))
            .lines
            .forEach(function(line) {
                scriptRun.push(line.toString());
            })
            .on('pipe', function() {
                async.series(
                    [   function(callback){
                            logValue(scriptRun, function(res){
                                value = res;
                                for (var i = 0; i < value.length; i++) {
                                    value[i].unshift(test_id);
                                }
                                // console.log(value);
                                return callback();
                            });
                        },
                        function(callback) {
                            conn.query(
                                'INSERT INTO run_log (test_id, script_name, ' +
                                'server, start_time, duration, end_time, ' +
                                'concurrent, service, avg_response_time, ' +
                                'avg_throughput, err) VALUES ?',
                                [value], function(err, result) {
                                    if (err) {
                                        console.error(err);
                                        return callback(err);
                                    }
                                    console.log("run_log inserted");
                                    return callback();
                                });
                        },
                        function(callback) {
                            conn.query("SELECT * FROM run_log where test_id = ?",
                                       [test_id], function(err, rows) {
                                if (err) {
                                    console.error(err);
                                    return;
                                }
                                rows.forEach(function(row) {
                                    fillRun_detail(row.id, row.script_name);
                                    // fillServer(row.id, row.script_name);
                                });
                                return callback();
                            });
                        }
                    ], function(err) {
                        if(err){
                            console.log(err);
                            return;
                        }
                    });
            });
    });
}


function fillRun_detail(run_id, script_name) {
    var jtlFile = process.argv[2]+'/' + script_name + '.jtl';

    new lazy(fs.createReadStream(jtlFile))
        .lines
        .forEach(function(line) {
            var jtlResult = line.toString().split(",");
            if (jtlResult[3] === '200' && jtlResult[7] === 'true')
                var errCode = "success";
            else
                var errCode = "fail";
            var detail = [[jtlResult[0], run_id, parseInt(jtlResult[1]),
                           parseInt(jtlResult[9]), errCode]];
            // console.log(detail);
            conn.query('INSERT INTO run_detail (timestamp, run_id, ' +
                       'response_time, bandwidth, error) VALUES ?',
                       [detail], function(err, result) {
                if (err) {
                    console.error(err);
                    return;
                }
            });
        })
        .on('pipe', function() {
            console.log("run_detail inserted");
            safeCloseConn();
        });
}


function fillServer(run_id, script_name) {
    var serverFile = process.argv[2]+'/' + script_name + '_server.jtl';
    var values = [];
    var isFirstLine = true;

    new lazy(fs.createReadStream(serverFile))
        .lines
        .forEach(function(line) {
            if (isFirstLine) {
                isFirstLine = false
            } else {
                var jtlResult = line.toString().split(",");
                var jtlItem = jtlResult[2].split(" ")[2];
                var value = [jtlResult[0], run_id, jtlItem, parseInt(jtlResult[1])];
                values.push(value);
            }
        })
        .on('pipe', function() {
            conn.query('INSERT INTO run_metrics (timestamp, run_id, name, ' +
                       'value) VALUES ?', [values], function(err, result) {
                if (err) {
                    console.error(err);
                    return;
                }
                console.log("run_metrics inserted");
                safeCloseConn();
            });
        });
}


function JmeterResult(item, webservice, threadCount, callback) {
    var serviceName = webservice + '_' + threadCount + 'users'
    var jFile = process.argv[2] + '/result_' + item + '.txt';

    new lazy(fs.createReadStream(jFile))
        .lines
        .forEach(function(line) {
            var resultArray = line.toString().split(",");
            if (resultArray[0] === serviceName) {
                return callback(resultArray[1]);
            } else {
                return;
            }
        });
}


function logValue(scriptLine, callback2) {
    var val = [];
    var tps;
    var rt;
    var errRate;

    async.forEach(scriptLine, function(line, callback1){
        var scriptLineStr = line.split(",");
        async.parallel([
            function(callback) {
                JmeterResult("throughput", scriptLineStr[6],
                             parseInt(scriptLineStr[2]), function(res) {
                    tps = res;
                    return callback();
                });
            },
            function(callback) {
                JmeterResult("responseTime", scriptLineStr[6],
                             parseInt(scriptLineStr[2]), function(res) {
                    rt = res;
                    return callback();
                });
            },
            function(callback) {
                JmeterResult("error", scriptLineStr[6],
                             parseInt(scriptLineStr[2]), function(res) {
                    errRate = res;
                    return callback();
                });
            }
        ], function(err) {
                if (err) {
                    console.log(err);
                    return;
                }
                var input = [scriptLineStr[1], scriptLineStr[3],
                             scriptLineStr[0], parseInt(scriptLineStr[4]),
                             scriptLineStr[5], parseInt(scriptLineStr[2]),
                             scriptLineStr[6], rt, tps, errRate];
                val.push(input);
                return callback1();
        });

    }, function(err){
            if (err) {
                console.log(err);
                return;
            }
            return callback2(val);
    });
}


function getConn() {
    var connection = mysql.createConnection({
        host: process.argv[3],
        user: process.argv[4],
        password: process.argv[5],
        database: process.argv[6],
    });
    return connection;
}

function safeCloseConn() {
    if (safeToCloseMySQL)
        conn.end();
    else
        safeToCloseMySQL = true;
}


fillTest();
