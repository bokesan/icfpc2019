#!/bin/bash

BLOCK=$1

echo "Submitting mining data for block '$BLOCK'"
./lambda-cli.py submit $BLOCK blocks/$BLOCK/task.sol blocks/$BLOCK/puzzle.desc 
