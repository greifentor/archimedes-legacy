#!/bin/bash

echo Setting Archimedes project version to: $1

mvn versions:set -DnewVersion=$1
mvn versions:set -DnewVersion=$1

mvn clean install -Dmaven.test.skip=true

java -cp legacy-core/target/archimedes-legacy-core-$1.jar archimedes.util.VersionBatchWriter . $1 LINUX
java -cp legacy-core/target/archimedes-legacy-core-$1.jar archimedes.util.VersionBatchWriter . $1 WINDOWS