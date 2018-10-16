package ibm.cte.esp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ibm.cte.esp.dao.AssetDAO;
import ibm.cte.esp.dao.CassandraRepo;
import ibm.cte.esp.model.AssetEvent;

/**
 * Read Asset published on a kafka topic and insert it into Cassandra asset table.
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
public class AssetInjector {

	final static Logger logger = LoggerFactory.getLogger("AssetIngestor");

	private int minBatchSize = 2;
    private AssetTopicConsumer kafkaConsumer;
    private AssetDAO assetDAO;
    private boolean runAgain = true;

    public AssetInjector() {
    	ApplicationConfig cfg = new ApplicationConfig();
		logger.info("########### Asset Injector START ##########");
		logger.info("  Version:" + cfg.getConfig().getProperty(ApplicationConfig.VERSION));
		logger.info("  Kafka:" + cfg.getConfig().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS));
		logger.info("  Cassandra:" + cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_ENDPOINTS));


		try {
			assetDAO = new CassandraRepo(cfg);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	kafkaConsumer = new AssetTopicConsumer(cfg);
    }

	public static void main(String[] args) {
		AssetInjector injector = new AssetInjector();
		injector.run();
	}

	public void run() {
        while (runAgain) {
        	 List<AssetEvent> buffer = kafkaConsumer.consume();
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


	private void insertIntoDb( List<AssetEvent> buffer) throws Exception{
		for (AssetEvent a  : buffer) {
			logger.info("Get asset "+a.getId()+"....persist it!");
			assetDAO.persistAsset(a);
		}
	}
}
