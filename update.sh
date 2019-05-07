#!/bin/sh
year=`date +%Y`
month=`date +%m`
day=`date +%d`
hour=`date +%H`
minute=`date +%M`


FILENAME="alice-bot-1.0-SNAPSHOT-jar-with-dependencies.jar"

screen -x -r "alice-bot-prod" -X stuff "^C"
find backups/* -mtime +2 -exec rm {} \;
mv $FILENAME backups/$year.$month.$day-$hour.$minute-$FILENAME
mv jenkins/$FILENAME .
./start.sh
