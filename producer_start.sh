#/bin/bash

set -euo pipefail

TOPIC=${1:-"Dummy"}
ITERATIONS=${2:-"5"}

echo "Usage: ${0} TOPIC ITERATIONS"
echo "Values:"
echo "TOPIC=${TOPIC:-default}"
echo "ITERATIONS=${ITERATIONS:-default}"

java -jar java/producer/target/producer.jar \
     "${TOPIC}" "${ITERATIONS}"
