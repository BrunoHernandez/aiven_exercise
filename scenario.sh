#/bin/bash

set -euo pipefail

./build.sh

./start.sh producer "Dummy" 40 &
./start.sh consumer "Dummy" 40

/usr/bin/psql "postgres://avnadmin:dqy7oecyr6vt0bcw@bhzeto-postgresql-example-bhzeto-8387.aivencloud.com:26894/defaultdb?sslmode=require" --command="select * from Records;"
