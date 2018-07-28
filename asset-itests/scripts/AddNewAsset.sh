export CP=./target/asset-itests-0.0.1.jar:./target/test-classes
export CP=$CP:~/.m2/repository/com/datastax/cassandra/cassandra-driver-core/3.5.1/cassandra-driver-core-3.5.1.jar
export CP=$CP:~/.m2/repository/com/datastax/cassandra/cassandra-driver-extras/3.5.1/cassandra-driver-extras-3.5.1.jar
export CP=$CP:~/.m2/repository/com/google/guava/guava/24.1-jre/guava-24.1-jre.jar
export CP=$CP:~/.m2/repository/org/slf4j/slf4j-api/1.7.7/slf4j-api-1.7.7.jar
export CP=$CP:~/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar
export CP=$CP:~/.m2/repository/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar
export CP=$CP:~/.m2/repository/io/netty/netty-all/4.0.9.Final/netty-all-4.0.9.Final.jar
export CP=$CP:~/.m2/repository/io/netty/netty-common/4.1.25.Final/netty-common-4.1.25.Final.jar
export CP=$CP:~/.m2/repository/com/github/jnr/jnr-ffi/2.1.7/jnr-ffi-2.1.7.jar
export CP=$CP:~/.m2/repository/com/github/jnr/jffi/1.2.16/jffi-1.2.16.jar
export CP=$CP:~/.m2/repository/io/dropwizard/metrics/metrics-core/3.2.6/metrics-core-3.2.6.jar
java -cp $CP ibm.cte.pot.cassandra.CassandraAssetWriter