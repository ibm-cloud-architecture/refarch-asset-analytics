package application.kafka;

import java.net.ConnectException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;


public class Producer {

    private final String topic;

    private static KafkaProducer<String, String> kafkaProducer = null;
    
    private Logger logger = Logger.getLogger(Producer.class.getName());

    public  Producer(String bootstrapServerAddress, String topic) throws InstantiationException {
        this.topic = topic;
        if (topic == null) {
            throw new InstantiationException("Missing required topic name.");
        } else if (bootstrapServerAddress == null) {
            throw new InstantiationException("Missing required bootstrap server address.");
        }
        try {
            kafkaProducer = createProducer(bootstrapServerAddress);
        } catch (KafkaException e) {
            throw new InstantiationException(e.getMessage());
        }
    }

    private KafkaProducer<String, String> createProducer(String brokerList) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        properties.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 10000);
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 4000);
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        
        KafkaProducer<String, String> kafkaProducer = null;
        
        try {
            kafkaProducer = new KafkaProducer<>(properties);
        } catch (KafkaException kafkaError ) {
            logger.severe("Error while creating producer." + kafkaError.getMessage());
            throw kafkaError;
        }
        return kafkaProducer;
    }

    public RecordMetadata produce(String message) throws InterruptedException, ExecutionException, ConnectException {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, message);
        RecordMetadata recordMetadata = kafkaProducer.send(record).get();
        return recordMetadata;
    }

    public void shutdown() {
        kafkaProducer.flush();
        kafkaProducer.close();
    }
    
}