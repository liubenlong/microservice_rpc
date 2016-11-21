#!/bin/bash
TAR_GZ_DIR=$TARGET_PATH
TARGET_DIR=/letv/app/${MODULE_NAME}

ARTIFACT_NAME=${MODULE_NAME}-${VERSION}
ENV_NAME=$ENV
SCRIPT_DIR=$(dirname $(readlink -f $0))

if [ "$ENV" = "dev" ]
then  SERVER_LIST_FILE="10.104.29.19"
elif [ "$ENV" = "prod" ]
then  SERVER_LIST_FILE="10.176.28.66 10.176.28.67 10.135.28.95 10.135.28.96 10.154.30.131 10.154.30.133"
fi

echo "-------------------------"
echo "----target_dir="$TARGET_DIR
echo "----server_list_file="$SERVER_LIST_FILE
echo "----artifact_name="$ARTIFACT_NAME
echo "-------------------------"

for server in $SERVER_LIST_FILE
do
	echo "----scp build artifacts to remote server: $server"
	ssh root@${server} "mkdir -p $TARGET_DIR"
	scp $TAR_GZ_DIR/${ARTIFACT_NAME}.tar.gz root@${server}:${TARGET_DIR}
	echo "scp build artifacts complete..."
	
	cat $SCRIPT_DIR/remote.sh | sed -e "s/#env#/${ENV_NAME}/g"  -e "s/#artifact#/${ARTIFACT_NAME}/g" -e "s/#module#/${MODULE_NAME}/g" | ssh root@$server
done