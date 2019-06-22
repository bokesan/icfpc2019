#!/bin/bash

NEXTBLOCK=$2
TASKJAR=$1
TARGETDIR=blocks/$NEXTBLOCK/
CURRENTBLOCK="$(./lambda-cli.py getblockchaininfo block)"

echo "Waiting for block: '$NEXTBLOCK'" 
echo "Current block: '$CURRENTBLOCK'"

if (( $CURRENTBLOCK == $NEXTBLOCK ))
	then
		echo "Solving block number '$NEXTBLOCK'" 
		java -Xms2g -jar "$TASKJAR" --target-dir "$TARGETDIR" $TARGETDIR/*.desc
		echo "Task solved" 
fi


