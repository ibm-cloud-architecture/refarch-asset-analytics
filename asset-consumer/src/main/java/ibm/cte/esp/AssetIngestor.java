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
public class AssetIngestor {

	private static Logger logger = Logger.getLogger("AssetIngestor");
	private static String TOPICNAME = "test-topic";
	private static String GROUPID = "AssetIngestor";
	private static String BOOTSTRAP_SERVERS = "docker.for.mac.host.internal:30092";
	
	private static KafkaConsumer<String, String> kafkaConsumer;
    private static final long POLL_DURATION = 1000;
	
    
	public static void main(String[] args) {
		String brokers = BOOTSTRAP_SERVERS;
		String topic = TOPICNAME;
		String groupid = GROUPID;
		int minBatchSize = 2;
		if (args.length == 4) {
			topic = args[0];
			brokers = args[1];
			groupid = args[2];
			minBatchSize = Integer.parseInt(args[3]);
		}
		
		Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupid);
        // offsets are committed automatically with a frequency controlled by the config auto.commit.interval.ms
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
 
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(topic));
        Gson gson = new Gson();
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        
        
        // commit offset only when persisted in DB.
	    ConsumerRecords<String, String> records = kafkaConsumer.poll(POLL_DURATION);
	    for (ConsumerRecord<String, String> record : records) {
	    		Asset a = gson.fromJson(record.value(), Asset.class);
	    		logger.info("Received :" + a.getId() + " offset "+record.offset());
	    		logger.info(record.key() + " => " + record.value());
	            buffer.add(record);
	    }
	    if (buffer.size() >= minBatchSize) {
	    	insertIntoDb(buffer);
	    	kafkaConsumer.commitSync();
            buffer.clear();	 
	    } 
        kafkaConsumer.close();
	}

	private static void insertIntoDb( List<ConsumerRecord<String, String>> buffer) {
		// TODO use web client to send data to Data Access Layer. 
	}
}
