package ibm.cte.cassandra.test;

import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.CQLDataSet;

public class CassandraEmbedded extends CassandraCQLUnit {

	public CassandraEmbedded(CQLDataSet dataSet) {
       super(dataSet);
   }

   public CassandraEmbedded(CQLDataSet dataSet, int readTimeoutMillis) {
       super(dataSet, readTimeoutMillis);
   }

   public CassandraEmbedded(CQLDataSet dataSet, String configurationFileName) {
       super(dataSet, configurationFileName);
   }

   public CassandraEmbedded(CQLDataSet dataSet, String configurationFileName, int readTimeoutMillis) {
       super(dataSet, configurationFileName, readTimeoutMillis);
   }

   public CassandraEmbedded(CQLDataSet dataSet, String configurationFileName, long startUpTimeoutMillis) {
       super(dataSet, configurationFileName, startUpTimeoutMillis);
   }

   public CassandraEmbedded(CQLDataSet dataSet, String configurationFileName, long startUpTimeoutMillis, int readTimeoutMillis) {
       super(dataSet, configurationFileName, startUpTimeoutMillis, readTimeoutMillis);
   }

   public void start() throws Exception {
       before();
   }

   public void stop() {
       after();
   }

}
