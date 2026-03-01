#!/usr/bin/env sh
set -eu

./gradlew --no-daemon spotlessCheck test
