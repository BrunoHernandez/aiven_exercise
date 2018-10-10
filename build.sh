#/bin/bash

set -euo pipefail

SERVICE_KEY_PATH=service.key
SERVICE_CERT_PATH=service.cert
CA_PATH=ca.pem
MAVEN_PROJECT_PATH=java
POSTGRES_SSL_ROOT_CERT=postgresca.pem
POSTGRES_TRUSTSTORE=postgres.truststore.jks
SSL_KEYSTORE_LOCATION=client.keystore.p12
SSL_TRUSTSTORE_LOCATION=client.truststore.jks

fail_with_message () {
  echo -e "${1}" >&2
  exit 1
}

check_keys_and_certs () {
    for file in $SERVICE_KEY_PATH $SERVICE_CERT_PATH $CA_PATH \
                                  $POSTGRES_SSL_ROOT_CERT; do
        if [ ! -f "${file}" ]; then
            fail_with_message "File ${file} not found."
        fi
    done
}

check_credentials () {
    for file in $SSL_TRUSTSTORE_LOCATION $SSL_KEYSTORE_LOCATION \
                $POSTGRES_TRUSTSTORE; do
        if [ ! -f "${file}" ]; then
            test -f "${file}"
        fi
    done
}

create_credentials () {
    openssl pkcs12 -export \
            -inkey "${SERVICE_KEY_PATH}" \
            -in "${SERVICE_CERT_PATH}" \
            -out "${SSL_KEYSTORE_LOCATION}" \
            -name service_key
    keytool -import \
            -file "${CA_PATH}" \
            -alias CA \
            -keystore "${SSL_TRUSTSTORE_LOCATION}"
    keytool -import \
            -file "${POSTGRES_SSL_ROOT_CERT}" \
            -alias POSTGRESCA \
            -keystore "${POSTGRES_TRUSTSTORE}"
}

if ! check_credentials; then
    echo "Credentials not found. Making credentials..."
    create_credentials
fi

mvn package --file="${MAVEN_PROJECT_PATH}/pom.xml"
