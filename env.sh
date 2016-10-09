DATE=`date +%m%d%Y`
TIMESTAMP=`date +%s`

# File path settings
SCRIPT=${HOME}/Developer/performance

#Jmeter 3.0
JMETER_ROOT_PATH=${HOME}/Developer/apache-jmeter-3.0
# JMETER_CMD_RUNNER_PATH=$JMETER_ROOT_PATH/lib/ext/jmeter-plugins-cmd-2.1.jar

#Jmeter 2.11
# JMETER_ROOT_PATH=${HOME}/Developer/apache-jmeter-2.11
JMETER_CMD_RUNNER_PATH=$JMETER_ROOT_PATH/lib/ext/CMDRunner.jar

JMETER_PATH=$JMETER_ROOT_PATH/bin/jmeter
JMETER_SRC=${SCRIPT}/TestPlan
JMETER_RESULT=${SCRIPT}/TestResult/${DATE}_${TIMESTAMP}
JMETER_TEST_DATA=${SCRIPT}/TestData

# JMeter settings
# JMETER_HOST=dev.vliveshow.com
JMETER_HOST=www.vliveshow.com
JMETER_PORT=80
# JMETER_PORT_AGENT=4444

JMETER_START_TIME=5
JMETER_LOAD_TIME=10
JMETER_SHUT_TIME=5
JMETER_LOAD_TIME_MIN=$[JMETER_LOAD_TIME/60]

# MySQL settings
MYSQL_HOST=localhost
MYSQL_USERNAME=root
MYSQL_PASSWORD=Dujun1205
MYSQL_DATABASE=vls_perf

# Test settings ("ad_list" "liveshow_list" "send_groupmsg" "join_room" "like_it" "send_gift")
TEST_PLAN=("join_room")
LOAD_COUNT=(10)
HTTP_PROTOCOL=http
