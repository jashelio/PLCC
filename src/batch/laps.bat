@echo off

set LAPS_DIR=%~dp0
set MAIN_CLASS=edu.rit.gec8773.laps.Main
java -cp "%LAPS_DIR:~0,-1%\LAPS.jar;%CD%" %MAIN_CLASS% %*