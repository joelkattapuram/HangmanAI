#!/bin/bash

echo "Building project"
mvn compile
echo "Executing program"
mvn exec:java -Dexec.args="$1 $2"
echo "Cleaning up project"
mvn clean
