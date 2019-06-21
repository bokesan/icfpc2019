#!/bin/bash

JARFILE=$1
SOURCEDIR=$2
TARGETDIR=$3

for file in ${SOURCEDIR}/*.desc; do
	[ -e "$file" ] || continue
	TARGET=`echo ${file} | sed "s/.*\///" | cut -d'.' -f 1`
	TARGET="${TARGETDIR}/${TARGET}.sol"
	java -Xms2g -Xss500m -jar ${JARFILE} ${file} ${TARGET}
done
