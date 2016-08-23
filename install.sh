#!/bin/bash
function testCMD() 
{
	echo "$@"
    "$@"
    local status=$?
    if [ $status -ne 0 ]; then
        echo "ERROR: code: $status"
        exit 255
    fi
    return $status
}

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
cd "$DIR"
DMG_HOME="/usr/local/dmg"
testCMD mvn package
echo "sudo mkdir \"$DMG_HOME\""
sudo mkdir "$DMG_HOME"
testCMD sudo cp "./target/dmg.jar" "$DMG_HOME/dmg.jar"
testCMD sudo cp "./install/DMG.sh" "$DMG_HOME/DMG.sh"
testCMD sudo chmod +x "$DMG_HOME/DMG.sh"
testCMD sudo sudo ln -sf "$DMG_HOME/DMG.sh" "/usr/local/bin/dmg"
echo checking
testCMD which dmg
testCMD dmg -version
