package ibm.cte.pot.msg;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import ibm.cte.pot.BffSocketHandler;

/**
 * Subscribe to kafka topic to get asset events. Two types of event:
 * - new asset event
 * - measurement from asset
 * @author jeromeboyer
 *
 */
public class KafkaAssetConsumer {
	 private static final Logger logger = Logger.getLogger(KafkaAssetConsumer.class.getName());
	private static String TOPICNAME = "test-topic";
	private static String GROUPID = "BFFConsumers";
	private static String BOOTSTRAP_SERVERS = "docker.for.mac.host.internal:30092";
    private static final long POLL_DURATION = 1000;
    
	private String brokers = BOOTSTRAP_SERVERS;
	private String topic = TOPICNAME;
    private String groupid = GROUPID;

    private BffSocketHandler broadcaster;
    KafkaMessageListenerContainer<Integer, String> kafkaConsumer ;
	   
    
    public KafkaAssetConsumer(BffSocketHandler broadcaster){
    	this.broadcaster= broadcaster;
    	prepareConsumer();
    }
    
    
    private void prepareConsumer() {
    	Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupid);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,true);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"100");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
       
    	DefaultKafkaConsumerFactory<Integer, String> cf =
                new DefaultKafkaConsumerFactory<Integer, String>(properties);
    	
    	ContainerProperties containerProps = new ContainerProperties(topic);
    	containerProps.setPollTimeout(POLL_DURATION);
    	containerProps.setMessageListener(new MessageListener<String, String>() {
			@Override
			public void onMessage(ConsumerRecord<String, String> record) {
				 if ( record != null) {
	     	    	logger.info("Received from kafka:" + record.key() + " offset "+record.offset() + " => " + record.value());
	        		 broadcaster.broadcastMessage(record.value());
	        	 }
			}
    	});
    	kafkaConsumer =  new KafkaMessageListenerContainer<>(cf, containerProps);
	}
    
    
    public void start() {
    	 logger.info("Start Kafka Asset Event Consumer ........");
    	 kafkaConsumer.start(); 	 
    }
    
    public void close() {
    	kafkaConsumer.stop();
    }
}
