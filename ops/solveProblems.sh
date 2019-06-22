#!/bin/bash

JARFILE=$1
SOURCEDIR=$2
TARGETDIR=$3

java -Xms2g -jar "$JARFILE" --target-dir "$TARGETDIR" $SOURCEDIR/*.desc
