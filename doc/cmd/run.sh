#! /bin/bash
export JAVA_HOME=/home/dev/jdk1.8.0_151
export PATH=$PATH:$JAVA_HOME/bin
export HADOOP_USER_NAME=hdfs
chmod +x /home/dev/edu-tf/platform.jar
nohup java -Dspring.profiles.active=test -Dserver.port=8080 -Dlog.dir=/home/dev/edu-tf/logs -Djava.io.tmpdir=/home/dev/uploadFiles/tmp -jar /home/dev/edu-tf/platform.jar -server >init.log  2>&1  &