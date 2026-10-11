#!/usr/bin/env sh
# Local backend: Postgres from ../docker-compose.yml, API on :8081, CORS for the frontend on :4202.
# Builds the jar (skip with SKIP_BUILD=1) and runs it.
cd "$(dirname "$0")"
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5436/jigsawsudoku"
export SPRING_DATASOURCE_USERNAME=jigsaw
export SPRING_DATASOURCE_PASSWORD=jigsaw
export PORT="${PORT:-8081}"
export ALLOWED_ORIGIN="${ALLOWED_ORIGIN:-http://localhost:4202}"
[ "${SKIP_BUILD:-0}" = 1 ] || ./mvnw -q -B -DskipTests package || exit 1
exec java -jar target/jigsawsudoku-1.0.jar
