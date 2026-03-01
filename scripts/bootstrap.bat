@echo off
setlocal

if not exist ".env" (
  copy /Y ".env.example" ".env" >nul
)

call .\gradlew.bat --no-daemon spotlessCheck test
endlocal
