package ibm.cte.pot.msg;

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


public class KafkaAssetConsumer {
	 private static final Logger logger = Logger.getLogger(KafkaAssetConsumer.class.getName());
	private static String TOPICNAME = "test-topic";
	private static String GROUPID = "BFFConsumers";
	private static String BOOTSTRAP_SERVERS = "docker.for.mac.host.internal:30092";
	
	private static KafkaConsumer<String, String> kafkaConsumer;
    private static final long POLL_DURATION = 1000;
    
	private String brokers = BOOTSTRAP_SERVERS;
	private String topic = TOPICNAME;
    private String groupid = GROUPID;
	   
    public KafkaAssetConsumer(){
    	prepareConsumer();
    }
    
    private void prepareConsumer() {
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
    
    public List<String> getNext() {
    	 List<String> buffer = new ArrayList<>();
    	 ConsumerRecords<String, String> records = kafkaConsumer.poll(POLL_DURATION);
    	 for (ConsumerRecord<String, String> record : records) {
	    		logger.info("Received :" + record.key() + " offset "+record.offset() + " => " + record.value());
	            buffer.add(record.value());
	            kafkaConsumer.commitSync();
	    }
    	 return buffer;
    }
    
    public void close() {
    	kafkaConsumer.close();
    }
}
