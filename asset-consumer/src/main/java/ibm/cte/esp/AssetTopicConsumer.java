package ibm.cte.esp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.google.gson.Gson;

import ibm.cte.esp.model.Asset;

/**
 * subscriber to asset topic
 * @author jeromeboyer
 *
 */
public class AssetTopicConsumer {

	private static KafkaConsumer<String, String> kafkaConsumer;
    private static final long POLL_DURATION = 1000;
    
    private Gson gson = new Gson();
    public ApplicationConfig config;
    
    public AssetTopicConsumer(ApplicationConfig cfg) {
     this.config = cfg;	
     prepareConsumer();
    }
    
    private void prepareConsumer() {
		Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, 
        		config.getConfig().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVER));
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, 
        		config.getConfig().getProperty(ApplicationConfig.KAFKA_GROUPID));
        // offsets are committed automatically with a frequency controlled by the config auto.commit.interval.ms
        // here we want to use manual commit 
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
 
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(config.getConfig().getProperty(ApplicationConfig.KAFKA_ASSET_TOPIC_NAME)));
	}
    
    public List<Asset>  consume() {
    	List<Asset> buffer = new ArrayList<>();
    	ConsumerRecords<String, String> records = kafkaConsumer.poll(POLL_DURATION);
	    for (ConsumerRecord<String, String> record : records) {
	    		Asset a = gson.fromJson(record.value(), Asset.class);
	            buffer.add(a);
	    }
    	return buffer;
    }
    
    public void commitOffset() {
    	kafkaConsumer.commitSync();
    }
    
    public void close() {
    	kafkaConsumer.close();
    }
}
