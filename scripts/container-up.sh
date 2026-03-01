#!/usr/bin/env sh
set -eu

if [ ! -f ".env" ]; then
  cp .env.example .env
fi

podman compose -f podman-compose.yml up --build
