package ibm.cte.esp;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {
	public static final String KAFKA_BOOTSTRAP_SERVER = "kafka.bootstrap.server";
	public static final String KAFKA_ASSET_TOPIC_NAME = "kafka.asset.topic.name";
	public static final String EVENT_PATTERN = "pump.event.pattern";
	public static final String METRICS_TRENT = "pump.metric.trent";
	public static final String EVENT_FREQ = "pump.event.frequency";
	public static final String NB_OF_EVENTS = "pump.event.number";

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
		 p.setProperty(KAFKA_BOOTSTRAP_SERVER, "gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local:32224");
		 p.setProperty(KAFKA_ASSET_TOPIC_NAME,"asset-topic");
	}
}
