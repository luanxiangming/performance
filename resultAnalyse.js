#!/usr/bin/node

var fs = require("fs");
var lazy = require("lazy");
var async = require('async');

var webServices = process.argv[2].split(",");
var userCounts = process.argv[3].split(",");
var jmeter_result = process.argv[4];
var tps_all = [];
var bw_all = [];
var rtAvg_all = [];
var error_all = [];


async.forEach(webServices, function(service, callback1) {
    async.forEach(userCounts, function(user_count, callback2) {
        var response = [];
        var service_user = service + '_' + user_count + 'users';
        var fileName = jmeter_result + '/' + service_user +
                       '_' + process.argv[5] + 'min.jtl';
        var bw = [service_user];//bandwidth
        var tps = [service_user]; // throughput
        var rtAvg = [service_user]; //average response time
        var errPercent = [service_user];//error percentage

        new lazy(fs.createReadStream(fileName))
            .lines
            .forEach(function(line) {
                response.push(line.toString());
            })
            .on('pipe',function() {
                var len = response.length;
                console.log("Response:" + len);

                var start = response[1].split(",");
                console.log("Start:" + start);

                var stop = response[len - 1].split(",");
                console.log("Stop:" + stop);

                var time = (parseInt(stop[0]) - parseInt(start[0])) / 1000;
                console.log("Time:" + time);

                tps.push((len / time).toFixed(1));
                bw.push(((sum(response, 9) / 1024) / time).toFixed(1));

                rtAvg.push(Math.round(sum(response, 1) / len));
                errPercent.push(errorNum(response).toFixed(2));

                console.log("Throughput: " + tps[1]);
                console.log("Bandwidth: " + bw[1]);
                console.log("Response time: " + rtAvg[1]);
                console.log("Error percentage: " + errPercent[1]);

                tps.push("\n");
                rtAvg.push("\n");
                bw.push("\n");
                errPercent.push("\n");

                fs.appendFileSync(jmeter_result + '/result_throughput.txt', tps);
                fs.appendFileSync(jmeter_result + '/result_bandwidth.txt', bw);
                fs.appendFileSync(jmeter_result + '/result_responseTime.txt', rtAvg);
                fs.appendFileSync(jmeter_result + '/result_error.txt', errPercent);

                return callback2();
            });
        }, function(err){
            return callback1();
        });
    }, function(err){
        return;
});


function sum(response, position) {
    var dataSum = 0;
    for (var i = 1; i < response.length; i++) {
        var str = response[i].split(",");
        dataSum += parseInt(str[position]);
    }
    return dataSum;
}


function errorNum(response) {
   var err = 0;
   for (var i = 0; i < response.length; i++) {
        var str = response[i].split(",");
        if (str[3] != 200 || str[7] != 'true')
            err++;
   }

    var err_percent = err / (response.length);
    return err_percent;
}
