package ibm.cte.esp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ibm.cte.esp.client.AssetEventMgrClient;
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
public class AssetInjector {

	final static Logger logger = LoggerFactory.getLogger("AssetIngestor");

	private int minBatchSize = 1;
    private AssetTopicConsumer kafkaConsumer;
    private AssetEventMgrClient assertManager;
    private boolean runAgain = true;

    public AssetInjector() {
    	ApplicationConfig cfg = new ApplicationConfig();
		logger.info("########### Asset Injector START ##########");
		logger.info("  Version:" + cfg.getProperties().getProperty(ApplicationConfig.VERSION));
		logger.info("  Kafka:" + cfg.getProperties().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS));
		logger.info("  Asset Manager:" + cfg.getProperties().getProperty(ApplicationConfig.ASSET_MGR_HOST));

    	kafkaConsumer = new AssetTopicConsumer(cfg);
    	assertManager = new AssetEventMgrClient(cfg);
    }

	public static void main(String[] args) {
		AssetInjector injector = new AssetInjector();	
		injector.run();
	}

	public void run() {
        while (runAgain) {
        	 List<MetricEvent> buffer = kafkaConsumer.consume();
	        // commit offset only when persisted in DB.
			    if (buffer.size() >= getMinBatchSize()) {
			    	try {
			    		sendAssetsToDataSource(buffer);
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


	private void sendAssetsToDataSource( List<MetricEvent> buffer) throws Exception{
		for (MetricEvent a  : buffer) {
			assertManager.saveAsset(a);
		}
	}

	public boolean isRunAgain() {
		return runAgain;
	}

	public void setRunAgain(boolean runAgain) {
		this.runAgain = runAgain;
	}

	public int getMinBatchSize() {
		return minBatchSize;
	}

	public AssetTopicConsumer getKafkaConsumer() {
		return kafkaConsumer;
	}

	public AssetEventMgrClient getAssertManager() {
		return assertManager;
	}
}
