#!/usr/bin/env bash
screen -d -m -S alice-bot-prod java -Xms512M -Xmx2G -jar alice-bot-1.0-SNAPSHOT-jar-with-dependencies.jar && echo 'Alice started.'