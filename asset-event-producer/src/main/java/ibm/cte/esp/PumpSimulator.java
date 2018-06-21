package ibm.cte.esp;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ibm.cte.esp.model.Asset;

/**
 * Simulate a Electrical S Pump generating events for rotator and temperature measurement.
 * 
 * - connect to a kafka topic to publish two types of events:
 *    - new asset added to the grid of devices
 *    - new asset measurement every n seconds
 * @author jerome boyer
 *
 */
public class PumpSimulator {
	private static Logger logger = Logger.getLogger("PumpSimulator");
	private static final String TOPICNAME = "test-topic";
	private static final String BOOTSTRAP_SERVERS = "docker.for.mac.host.internal:30092";
	
	private String brokers = BOOTSTRAP_SERVERS;
	private String topic = TOPICNAME;
	private int numberOfAsset = 5;
	private int timeGap = 30;
	private boolean event = false;
	
	private KafkaProducer<String, Object> kafkaProducer;
	
	public PumpSimulator() {}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		PumpSimulator simulator= new PumpSimulator();
		simulator.processArgument(args);
		simulator.prepareProducer();
		if (simulator.isEvent()) {
			simulator.generateEvents();
		} else {
			simulator.generateAssets();
			simulator.shutdown();
		}
	}
	
	
	
	public void generateEvents() {
	    
		
	}

	public void shutdown() {
		this.kafkaProducer.close();
	}

	private void prepareProducer() {
		Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        properties.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 10000);
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 4000);
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);  // retries in case of failure
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);  // size for the buffer
        properties.put(ProducerConfig.LINGER_MS_CONFIG,1); // milli seconds before sending the record. Help batching
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); //total amount of memory available to the producer for buffering
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.kafkaProducer = new KafkaProducer<>(properties);		
	}

	public void processArgument(String[] args) {
		if (args.length >= 3) {
			topic = args[0];
			brokers = args[1];
			if (args[2].contains("event")) {
				event = true;
			} else {
				numberOfAsset = Integer.parseInt(args[2]);
			}
		}
	}
	
	private  void generateAssets() throws InterruptedException, ExecutionException {
		for (int i = 0; i < numberOfAsset; i++) {
			String uid= java.util.UUID.randomUUID().toString();
			Asset a = new Asset();
			a.setId(uid);
			a.setAntivirus("v2.3");
			a.setCurrent((long)(110*Math.random()+10));
			a.setFlowRate((long)(100*Math.random()));
			a.setIpAddress("172.16.0."+i);
			a.setOs("Raspbian");
			a.setPressure((long)(1000*Math.random()));
			a.setTemperature((long)(300*Math.random()));
			a.setType("ESP");
			a.setRotation((long)(360*Math.random()));
			a.setVersion("0.0.1");
			
			publishAsset(a,topic);
		}
	} // generateAssets

	private  void publishAsset(Asset a, String topic) throws InterruptedException, ExecutionException {
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String s = gson.toJson(a);
		 logger.info("Send Asset with id: "+a.getId());
		 ProducerRecord<String, Object> record = new ProducerRecord<>(topic, a.getId(), s);
	     RecordMetadata recordMetadata = kafkaProducer.send(record).get();
	     logger.info("Receive partition id= " + recordMetadata.partition());
	}

	public String getBrokers() {
		return brokers;
	}

	public void setBrokers(String brokers) {
		this.brokers = brokers;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getNumberOfAsset() {
		return numberOfAsset;
	}

	public void setNumberOfAsset(int numberOfAsset) {
		this.numberOfAsset = numberOfAsset;
	}

	public int getTimeGap() {
		return timeGap;
	}

	public void setTimeGap(int timeGap) {
		this.timeGap = timeGap;
	}

	public boolean isEvent() {
		return event;
	}

	public void setEvent(boolean event) {
		this.event = event;
	}

	public KafkaProducer<String, Object> getKafkaProducer() {
		return kafkaProducer;
	}

	public void setKafkaProducer(KafkaProducer<String, Object> kafkaProducer) {
		this.kafkaProducer = kafkaProducer;
	}
}
