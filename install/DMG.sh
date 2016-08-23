#!/bin/bash
DMG_HOME="/usr/local/dmg"
DMG_Exec="$DMG_HOME"/dmg.jar
java -jar $DMG_Exec "$@" #&
