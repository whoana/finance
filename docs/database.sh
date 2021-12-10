export DATABASE_HOME=/Users/whoana/DEV/apps/h2
java -cp ${DATABASE_HOME}/bin/h2*.jar org.h2.tools.Server -webAllowOthers -webPort 8082 -tcpAllowOthers -tcpPort 9092 -tcpPassword whoana