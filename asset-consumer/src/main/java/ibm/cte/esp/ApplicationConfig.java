package ibm.cte.esp;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {
	public static final String CASSANDRA_ENDPOINTS = "cassandra.contactpoints";
	public static final String CASSANDRA_PORT = "cassandra.port";
	public static final String CASSANDRA_KEYSPACE = "cassandra.keyspace";
	public static final String CASSANDRA_STRATEGY = "cassandra.strategy";
	public static final String CASSANDRA_REPLICAS = "cassandra.replicas";
	public static final String CASSANDRA_TABLE_NAME = "cassandra.asset.table.name";
	
	public static final String KAFKA_BOOTSTRAP_SERVER = "kafka.bootstrap.server";
	public static final String KAFKA_ASSET_TOPIC_NAME = "kafka.asset.topic.name";
	public static final String KAFKA_GROUPID = "kafka.groupid";
	
	
	private Properties p;
	InputStream input = null;
	
	public ApplicationConfig() {
		p = new Properties();
		try {
			input = new FileInputStream("conf/config.properties");
			p.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
			setDefaults();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Properties getConfig() {
		return p;
	}

	private  void setDefaults() {
		 p.setProperty(CASSANDRA_ENDPOINTS, "localhost");
		 p.setProperty(CASSANDRA_PORT, "9042");
		 p.setProperty(CASSANDRA_KEYSPACE, "assetmonitoring");
		 p.setProperty(CASSANDRA_STRATEGY, "SimpleStrategy");
		 p.setProperty(CASSANDRA_REPLICAS, "1");
	}
}
