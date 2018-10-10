#/bin/bash

set -euo pipefail

please_install () {
    echo -e "Please install ${1} in your linux distribution: $(lsb_release -a)" >&2
    exit 1
}

check_openjdk () {
    which java
}

check_maven () {
    which mvn
}

check_postgresql () {
    which psql
}

if ! check_openjdk; then
    please_install "OpenJDK"
elif ! check_maven; then
    please_install "Maven"
elif ! check_postgresql; then
    please_install "PostgreSQL"
fi

echo "Done."
