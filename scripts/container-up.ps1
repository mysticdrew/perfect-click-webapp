$ErrorActionPreference = 'Stop'

if (-not (Test-Path .env)) {
  Copy-Item .env.example .env
}

podman compose -f podman-compose.yml up --build
