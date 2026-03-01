$ErrorActionPreference = 'Stop'

if (-not (Test-Path .env)) {
  Copy-Item .env.example .env
}

.\gradlew.bat --no-daemon spotlessCheck test
