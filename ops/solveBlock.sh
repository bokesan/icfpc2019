#!/bin/bash

TASKJAR=$1
STACKDIR=$2
NEXTBLOCK=$3
TARGETDIR=blocks/$NEXTBLOCK/
CURRENTBLOCK="$(./lambda-cli.py getblockchaininfo block)"

echo "Waiting for block: '$NEXTBLOCK'" 
echo "Current block: '$CURRENTBLOCK'"

if (( $CURRENTBLOCK == $NEXTBLOCK ))
	then
		echo "Solving block number '$NEXTBLOCK'" 
		java -Xms2g -jar "$TASKJAR" --target-dir "$TARGETDIR" $TARGETDIR/*.desc
		echo "Task solved"
		"$STACKDIR"/stack run -- "$TARGETDIR"/puzzle.cond > "$TARGETDIR"/puzzle.desc
		echo "Puzzle solved"
fi


