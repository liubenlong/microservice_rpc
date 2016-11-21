#!/bin/bash
MODULE_NAME=#module#
ARTIFACT_NAME=#artifact#
ARTIFACT_FILE="#artifact#.tar.gz"
ENV=#env#
cd /letv/app/${MODULE_NAME}
echo `pwd`
if [ -f ${ARTIFACT_FILE} ];then
    tar -xzf ${ARTIFACT_FILE}
else
	  echo "==========${ARTIFACT_FILE} not exist"
    exit 1   
fi

if [ -d ${MODULE_NAME} ];then
    mv ${MODULE_NAME} ${MODULE_NAME}_`date +"%Y%m%d%H%M%S"`
else
    echo "==========${MODULE_NAME} is not exists, not need backup"
fi
mv ${ARTIFACT_NAME} ${MODULE_NAME}
chmod 755 ${MODULE_NAME}/bin/*.sh
bash -ex ${MODULE_NAME}/bin/run.sh ${ENV}