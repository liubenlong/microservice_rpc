#!/bin/bash

JAVA_OPTS="-server -Xms1024m -Xmx1024m -Xmn384m -XX:MaxPermSize=256m \
-Xss256k -XX:+UseConcMarkSweepGC \
-XX:+UseParNewGC -XX:CMSFullGCsBeforeCompaction=5 \
-XX:+UseCMSCompactAtFullCollection \
-XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:/letv/logs/apps/externalservice/luffi/gc.log"

PHOME=$(dirname `readlink -f "$0"`)
PHOME=$(dirname $PHOME)

pid=`ps -eo pid,args | grep luffi | grep java | grep -v grep | awk '{print $1}'`

if [ -n "$pid" ]
then
    kill -3 ${pid}
    kill ${pid} && sleep 3
    if [  -n "`ps -eo pid | grep $pid`" ]
    then
        kill -9 ${pid}
    fi
    echo "kill pid: ${pid}"
fi

for ((i=1; i <= 3; i++))
do
    java $JAVA_OPTS -cp $PHOME/conf:$PHOME/lib/* com.MainApplication > /dev/null 2>&1 &
    sleep 5s
    pid=`ps -eo pid,args | grep luffi | grep java | grep -v grep | awk '{print $1}'`
    if [ -n "$pid" ]
    then break
    fi
done

pid=`ps -eo pid,args | grep luffi | grep java | grep -v grep | awk '{print $1}'`

if [ -n "$pid" ]
then echo "deploy success"
else echo "deploy failed"
fi