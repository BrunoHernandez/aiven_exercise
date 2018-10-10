#/bin/bash

set -euo pipefail

MAVEN_PROJECT_PATH=java

mvn package --file="${MAVEN_PROJECT_PATH}/pom.xml"
