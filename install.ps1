$src = $MyInvocation.MyCommand.Path
$dir = $PSScriptRoot
cd $dir
$DMG_HOME = $env:userprofile + "\.dmg"
mvn package
mkdir $DMG_HOME -Force
cp ".\target\dmg.jar" "$DMG_HOME\dmg.jar"
cp ".\install\DMG.ps1" "$DMG_HOME\DMG.ps1"
New-Alias -Name "dmg" -Value "$DMG_HOME\DMG.ps1" -Force -Scope Global