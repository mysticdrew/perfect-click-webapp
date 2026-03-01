@echo off
setlocal

if not exist ".env" (
  copy /Y ".env.example" ".env" >nul
)

podman compose -f podman-compose.yml up --build
endlocal
