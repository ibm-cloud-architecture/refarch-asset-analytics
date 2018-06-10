package application.kafka;

import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Kafka consumer of message
 * @author Based on generated code from IBM Event Streams - jerome boyer
 *
 */
public class Consumer {

    private final String CONSUMER_GROUP_ID = "CONSUMER_GROUP_ID";
    private final String DEFAULT = "DEFAULT";
    private final long POLL_DURATION = 1000;

    private String consumerGroupId;
    private KafkaConsumer<String, String> kafkaConsumer;

    private Logger logger = Logger.getLogger(Consumer.class.getName());


    public Consumer(String bootstrapServerAddress, String topic) throws InstantiationException {
        setOrGenerateConsumerGroupId();

        if (topic == null) {
            throw new InstantiationException("Missing required topic name.");
        } else if (bootstrapServerAddress == null) {
            throw new InstantiationException("Missing required bootstrap server address.");
        }
        try {
            kafkaConsumer = createConsumer(bootstrapServerAddress);
        } catch (KafkaException e) {
            throw new InstantiationException(e.getMessage());
        }
        kafkaConsumer.subscribe(Arrays.asList(topic));
    }

    private KafkaConsumer<String, String> createConsumer(String brokerList) {

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> kafkaConsumer = null;

        try {
            kafkaConsumer = new KafkaConsumer<>(props);
        } catch (KafkaException kafkaError) {
            logger.severe("Error creating kafka consumer." + kafkaError.getMessage());
            throw kafkaError;
        }
        
        return kafkaConsumer;
    }

    private void setOrGenerateConsumerGroupId() {
        consumerGroupId = System.getenv(CONSUMER_GROUP_ID);
        
        if (consumerGroupId == null) { 
            consumerGroupId = UUID.randomUUID().toString(); 

        } else if (consumerGroupId.equals(DEFAULT)) {
            consumerGroupId = UUID.randomUUID().toString(); 
        }
    }

    public ConsumerRecords<String, String> consume() {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(POLL_DURATION);
        return records;
    }
    
    public void shutdown() {
        kafkaConsumer.close();
        logger.info(String.format("Closed consumer: %s", consumerGroupId));
    }
}