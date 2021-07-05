CALL mvn versions:set "-DnewVersion=%2"
CALL mvn versions:set "-DnewVersion=%2"

java -cp legacy-core\target\archimedes-legacy-core-2.18.0.jar  archimedes.util.VersionBatchWriter %1 %2 LINUX
java -cp legacy-core\target\archimedes-legacy-core-2.18.0.jar  archimedes.util.VersionBatchWriter %1 %2 WINDOWS