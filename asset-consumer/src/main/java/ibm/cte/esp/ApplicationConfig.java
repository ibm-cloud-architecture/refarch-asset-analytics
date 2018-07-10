package ibm.cte.esp;
import java.io.File;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ApplicationConfig {
	public static final String CASSANDRA_ENDPOINTS = "cassandra.contactpoints";
	public static final String CASSANDRA_PORT = "cassandra.port";
	public static final String CASSANDRA_KEYSPACE = "cassandra.keyspace";
	public static final String CASSANDRA_STRATEGY = "cassandra.strategy";
	public static final String CASSANDRA_REPLICAS = "cassandra.replicas";
	private static Properties p;

	public static Properties getConfig() {
		if (p == null) {
			loadProperties();
		}
		return p;
	}

	private static void loadProperties() {
		 p = new Properties();
		 p.setProperty(CASSANDRA_ENDPOINTS, "localhost,cassandra-0.cassandra.greencompute.svc.cluster.local");
		 p.setProperty(CASSANDRA_PORT, "9042");
		 p.setProperty(CASSANDRA_KEYSPACE, "assetmonitoring");
		 p.setProperty(CASSANDRA_STRATEGY, "SimpleStrategy");
		 p.setProperty(CASSANDRA_REPLICAS, "1");
	}
}
