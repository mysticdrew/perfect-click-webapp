@echo off
setlocal

call .\gradlew.bat --no-daemon spotlessCheck test
endlocal
