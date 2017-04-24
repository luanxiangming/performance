# Glossary:
1. Target server: the server runs  and being tested.
2. Monitor server: the server runs the script and JMeter to kick off tests, and collect metrics.


# Update (24 April 2017): <br />
1. To simplify step2~3 and enable jmeter dashboard since version 3.0, latest jmeter binary with modified properties files have been included.<br />
2. You can trigger test by "sh go.sh" which leverages docker image "xiangming/java8:node6" which has java8 and node6 inside.<br />
3. You can still start test by "sh start_test.sh". Further, now you can run e.g. "sh start_test.sh 100"<br />


# How to setup the test environment:
1. Install and run MySQL on monitor server. <br />
   Install and run Node.js on monitor server (http://ask.xmodulo.com/install-node-js-linux.html)

2. Download and unzip JMeter to monitor server (download binaries version 3.0 from http://mirror.symnds.com/software/Apache//jmeter/binaries/apache-jmeter-3.0.tgz). One can test it by opening /path/to/apache-jemeter-3.0/bin/jmeter .

3. Download Jmeter Standard plugin 1.3.1 to monitor server (http://jmeterplugins.com/downloads/index.html), unzip it. Put everything inside the /path/to/JMeterPlugins-Standard-1.3.1/lib/ext folder into /path/to/apache-jemeter-3.0/lib/ext (create the folder if not exists).

4. Download PerfMon Server Agent 2.2.1 (http://jmeter-plugins.org/downloads/file/ServerAgent-2.2.1.zip), unzip it, and put it on target server.

5. Copy the jar of SocketIO sampler to target server. (cp performance/jmeter-socket.io/target/ApacheJmeter_socket_io-0.1.0-SNAPSHOT-jar-with-dependencies.jar apache-jmeter-3.0/lib/ext/)

6. Create a database called vls_perf (or other name) in MySQL. One way is to execute "mysqladmin -h host -u username -p create vls_perf" and enter the password. Remeber to specify the correct host and username. If the command is executed on monitor server, "-h host" can be neglected.

7. Import the database (vls_perf.sql) on monitor server by executing "mysql -u username -p -h host vls_perf < vls_perf.sql". Make sure you are either in the same directory with vls_perf.sql, or specify the complete path to vls_perf.sql. Similarly, remeber to specify the correct host and username. If the command is executed on monitor server, "-h host" can be neglected.
This step can be verified by seeing four tables "test", "run_log", "run_detail", and "run_metrics" under the "vls_perf" database.

8. Set up parameters in env.sh:
SCRIPT: the path to the directory of performance/start_test.sh
JMETER_PATH: the path to bin/jmeter.sh
JMETER_SRC: the path to /performance/TestPlan
JMETER_RESULT: the path to /performance/TestResult/${DATE}_${TIMESTAMP}
Note: at this point, please keep the "/${DATE}_${TIMESTAMP}" in JMETER_RESULT.
JMETER_TEST_DATA: the path to /performance/TestData

JMETER_HOST: hostname of the target server
JMETER_PORT: port used for  on the target server
JMETER_PORT_AGENT: port used to kick off PerfMon Server Agent on target server. By default this is 4444.

JMETER_START_TIME: the start period (in seconds) for JMeter to kick off the number of threads specified.
JMETER_LOAD_TIME: the load period (in seconds) for JMeter to maintain the number of threads specified.
JMETER_SHUT_TIME: the shut period (in seconds) for JMeter to shut down the number of threads specified.
Note: the total number of tests will be JMETER_START_TIME + JMETER_LOAD_TIME + JMETER_SHUT_TIME.

MYSQL_HOST: the hostname of MySQL database.
MYSQL_USERNAME: the username of MySQL.
MYSQL_PASSWORD: the password of MySQL.
MYSQL_DATABASE: the database used for performance test. "vls_perf" by default.

The remaining settings are subject to change at each test, and will be explained in the "How to kick off a test" part.


# How to kick off a test:
1. Make sure  server, MySQL, and Redis are running on target server.
2. Get PIDs of  server, MySQL and Redis, and configured them in MYSQL_PROCESS_ID, SERVER_PROCESS_ID, REDIS_PROCESS_ID in evn.sh.
3. Set the TEST_PLAN and LOAD_COUNT in env.sh. TEST_PLAN is a list of strings denoting the test plan file names excluding the ".jmx" extension separated by space in a parenthesis. E.g. ("Login") or ("Login" "ContactList" "ContactOrg" "SearchUser"). LOAD_COUNT is a list of integers denoting the number of threads (users) each test to run. E.g. (10) or (10 50 70 100 130 170).
4. Run PerfMon Server Agent on target server by executing /path/to/ServerAgent-2.2.1/startAgent.sh .
5. Make sure the CSV files in TestData folder are properly configured.
6. Run start_test.sh.
7. Terminate the PerfMon Server Agent by CRTL + C or kill -9 PID (replace PID with the PID of PerfMon Server Agent).


# How to write a test plan:
1. Copy and paste a similar test plan in the TestPlan folder, and rename it to a meaningful name.
2. Run the JMeter GUI by opening /path/to/apache-jemeter-3.0/bin/jmeter .
3. Open the new test plan file through the GUI.
4. Modify the "Path" and "Parameters" in the "HTTP Request".
5. Modify the "Patterns to Test" in the "Response Assertion" following the "HTTP Request".


# How to create a new action:
1. Create a new actione file under package net.unit8.jmeter.protocol.socket_io.util and name if XXXAction, e.g. NewAction.
2. Put the action name "New" in SocketIOActionTablePanel.ACTIONS.
3. Extends NewAction from SocketIOAsyncActionBase and implements run().


# Documentations about JMeter:
1. User Manual: http://jmeter.apache.org/usermanual/index.html
2. A good example of building a web test plan: http://jmeter.apache.org/usermanual/build-web-test-plan.html
