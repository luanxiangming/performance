DATE=`date +%m%d%Y`
TIMESTAMP=`date +%s`

# File path settings
SCRIPT=${HOME}/Developer/performance

#Jmeter 3.1
JMETER_ROOT_PATH=${SCRIPT}/apache-jmeter-3.1
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
# JMETER_HOST=www.vliveshow.com
# JMETER_HOST=staging.vliveshow.com
JMETER_HOST=172.16.233.127
JMETER_PORT=8092

JMETER_PORT_AGENT=4444

JMETER_START_TIME=5
JMETER_LOAD_TIME=60
JMETER_SHUT_TIME=5
JMETER_LOAD_TIME_MIN=$[JMETER_LOAD_TIME/60]

# MySQL settings
MYSQL_HOST=localhost
MYSQL_USERNAME=root
MYSQL_PASSWORD=password
MYSQL_DATABASE=performance

# Test settings ("VQS" "uploadWithFileConvert" "newLogin")
# Test settings ("ad_list" "liveshow_list" "send_groupmsg" "join_room" "like_it" "send_gift" "create_account" "create_room")
TEST_PLAN=("VQS")
LOAD_COUNT=(5)
HTTP_PROTOCOL=http
