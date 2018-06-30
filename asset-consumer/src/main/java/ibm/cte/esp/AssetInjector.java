package ibm.cte.esp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.google.gson.Gson;

import ibm.cte.esp.model.Asset;

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

	private static Logger logger = Logger.getLogger("AssetIngestor");
	private static String TOPICNAME = "test-topic";
	private static String GROUPID = "AssetIngestor";
	private static String BOOTSTRAP_SERVERS = "docker.for.mac.host.internal:30092";
	
	private static KafkaConsumer<String, String> kafkaConsumer;
    private static final long POLL_DURATION = 1000;
    
	private String brokers = BOOTSTRAP_SERVERS;
	private String topic = TOPICNAME;
    private String groupid = GROUPID;
    private int minBatchSize = 2;
    
    private CassandraRepo cassandra;
    
	public static void main(String[] args) {
		AssetInjector consumer = new AssetInjector();
		consumer.processArgument(args);
		consumer.prepareConsumer();
		consumer.run();
	}
	
	/* Arguments are optional */
	public void processArgument(String[] args) {
		if (args.length == 4) {
			topic = args[0];
			brokers = args[1];
			groupid = args[2];
			minBatchSize = Integer.parseInt(args[3]);
		}
		if (args.length == 2) {
			topic = args[0];
			brokers = args[1];
		}
	}
	
	public void prepareConsumer() {
		Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupid);
        // offsets are committed automatically with a frequency controlled by the config auto.commit.interval.ms
        // here we want to use manual commit 
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
 
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(topic));
	}
	

	public void run() {
		cassandra = new CassandraRepo();
        Gson gson = new Gson();
        List<Asset> buffer = new ArrayList<>();
        boolean runAgain = true;
        while (runAgain) {
	        // commit offset only when persisted in DB.
		    ConsumerRecords<String, String> records = kafkaConsumer.poll(POLL_DURATION);
		    for (ConsumerRecord<String, String> record : records) {
		    		Asset a = gson.fromJson(record.value(), Asset.class);
		    		logger.info("Received :" + a.getId() + " offset "+record.offset());
		    		logger.info(record.key() + " => " + record.value());
		            buffer.add(a);
		    }
		    if (buffer.size() >= minBatchSize) {
		    	try {
		    		insertIntoDb(buffer);
			    	kafkaConsumer.commitSync();
		            buffer.clear();	 
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    		runAgain = false;
		    	}
		    	
		    } 
        }
        kafkaConsumer.close();
	}



	
	
	private void insertIntoDb( List<Asset> buffer) throws Exception{
		for (Asset a  : buffer) {			
			cassandra.persistAsset(a);
		}
	}
}
