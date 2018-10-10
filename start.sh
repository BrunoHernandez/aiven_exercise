#/bin/bash

set -euo pipefail

APPLICATION=${1:-"none"}
TOPIC=${2:-"Dummy"}
ITERATIONS=${3:-"5"}
GROUP=${4:-"unset"}

usage () {
    echo "Usage: <application> ${0} TOPIC ITERATIONS GROUP"
    echo "Available applications:"
    echo "    consumer"
    echo "    producer"
}

arguments () {
    echo "Argument values:"
    echo "TOPIC=${TOPIC:-default}"
    echo "ITERATIONS=${ITERATIONS:-default}"
    if [ "${GROUP}" != "unset" ]; then
        echo "GROUP=${GROUP:-default}"
    fi
}

check_application_and_run () {
    if [ "${APPLICATION}" == "none" ]; then
        usage
        echo
        echo -e "Error: application was not specified." >&2
        exit 1
    elif [ "${APPLICATION}" == "consumer" ]; then
        if [ "${GROUP}" == "unset" ]; then
            echo -e "WARNING: Variable GROUP (Kafka group) is \"unset\"" >&2
        fi
        arguments
        java -jar java/consumer/target/consumer.jar \
             "${TOPIC}" "${ITERATIONS}" "${GROUP}"
    elif [ "${APPLICATION}" == "producer" ]; then
        arguments
        java -jar java/producer/target/producer.jar \
             "${TOPIC}" "${ITERATIONS}"
    else
        fail "Unsupported application \"${APPLICATION}\""
    fi
}

check_application_and_run
