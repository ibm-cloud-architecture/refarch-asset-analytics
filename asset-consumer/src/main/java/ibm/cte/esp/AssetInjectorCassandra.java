package ibm.cte.esp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ibm.cte.esp.dao.AssetDAO;
import ibm.cte.esp.dao.CassandraRepo;
import ibm.cte.esp.model.MetricEvent;

/**
 * Read Asset published on a kafka topic and call remote service to persist to cassandra
 *
 * The characteristics of this consumer:
 *  - will be unique consumer with its group
 *  - subscribe to a unique topic
 *  - poll every 1000ms
 *  - manage committing consumed offsets manually
 *
 * @author jerome boyer
 *
 */
public class AssetInjectorCassandra {

	final static Logger logger = LoggerFactory.getLogger("AssetIngestor");

	private int minBatchSize = 2;
    private AssetTopicConsumer kafkaConsumer;
    private AssetDAO assetDAO;
    private boolean runAgain = true;

    public AssetInjectorCassandra() {
    	ApplicationConfig cfg = new ApplicationConfig();
		logger.info("########### Asset Injector START ##########");
		logger.info("  Version:" + cfg.getProperties().getProperty(ApplicationConfig.VERSION));
		logger.info("  Kafka:" + cfg.getProperties().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS));
		logger.info("  Cassandra:" + cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_ENDPOINTS));

		try {
			assetDAO = new CassandraRepo(cfg);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	kafkaConsumer = new AssetTopicConsumer(cfg);
    }

	public static void main(String[] args) {
		AssetInjectorCassandra injector = new AssetInjectorCassandra();
		injector.run();
	}

	public void run() {
        while (runAgain) {
        	 List<MetricEvent> buffer = kafkaConsumer.consume();
	        // commit offset only when persisted in DB.
			    if (buffer.size() >= minBatchSize) {
			    	try {
			    		insertIntoDb(buffer);
				    	kafkaConsumer.commitOffset();
			        buffer.clear();
			    	} catch (Exception e) {
			    		e.printStackTrace();
			    		runAgain = false;
			    	}
			    }
        }
        if (kafkaConsumer != null) kafkaConsumer.close();
        logger.info("Stopping consumer gracefully !");
	}


	private void insertIntoDb( List<MetricEvent> buffer) throws Exception{
		for (MetricEvent a  : buffer) {
			logger.info("Get asset "+a.getId()+"....persist it!");
			assetDAO.persistMetricEvent(a);
		}
	}
}
