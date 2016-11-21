#!/bin/bash

ENV=$1
SCRIPT_HOME=$(dirname $(readlink -f $0))
bash -ex $SCRIPT_HOME/startup.sh $ENV