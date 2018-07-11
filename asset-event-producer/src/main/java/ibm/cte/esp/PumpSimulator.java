package ibm.cte.esp;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	final static Logger logger = LoggerFactory.getLogger("PumpSimulator");
	private int numberOfAssets = 5;
	private int timeGap = 10000;
	private boolean event = false;
	private ApplicationConfig config;
	
	private KafkaProducer<String, Object> kafkaProducer;
	
	public PumpSimulator() {
		config = new ApplicationConfig();
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		PumpSimulator simulator= new PumpSimulator();
		simulator.processArgument(args);
		simulator.prepareProducer();
		logger.info("######### Pump Simulator Starting ############ ");
		if (simulator.isEvent()) {
			simulator.generateEvents();
		} else {
			simulator.generateAssets();
			
			simulator.shutdown();
		}
	}
	
	
	/**
	 * Generate pump event every n seconds
	 */
	public void generateEvents() {
	    
		
	}

	public void shutdown() {
		this.kafkaProducer.close();
	}

	private void prepareProducer() {
		Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
        		config.getConfig().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVER));
        properties.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 10000);
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 4000);
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);  // retries in case of failure
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);  // size for the buffer
        properties.put(ProducerConfig.LINGER_MS_CONFIG,1); // milli seconds before sending the record. Help batching
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); //total amount of memory available to the producer for buffering
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.kafkaProducer = new KafkaProducer<>(properties);
        
		if (event) {
			logger.info("Pump Simulator sending pump event to " 
		    + config.getConfig().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVER) 
		    + " every " + timeGap + " ms");
		} else {
			logger.info("Pump Simulator sending " + numberOfAssets 
					+ " new asset event to "+ config.getConfig().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVER)  +" every " + timeGap + " ms");
		}
	}

	public void processArgument(String[] args) {
		if (args.length >= 3) {
			config.getConfig().setProperty(ApplicationConfig.KAFKA_ASSET_TOPIC_NAME, args[0]);
			config.getConfig().setProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVER, args[1]);
			if (args[2].contains("event")) {
				event = true;
			} else {
				numberOfAssets = Integer.parseInt(args[2]);
			}
		}
	}
	
	private  void generateAssets() throws InterruptedException, ExecutionException {
		for (int i = 0; i < numberOfAssets; i++) {
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
			a.setLatitude(30.307182);
			a.setLongitude(-97.755996);
			
			publishAsset(a,config.getConfig().getProperty(ApplicationConfig.KAFKA_ASSET_TOPIC_NAME));
			Thread.sleep(3000);
		}
	} // generateAssets

	private  void publishAsset(Asset a, String topic) throws InterruptedException, ExecutionException {
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String s = gson.toJson(a);
		logger.info("Send Asset: " + s);
		ProducerRecord<String, Object> record = new ProducerRecord<>(topic, a.getId(), s);
	    RecordMetadata recordMetadata = kafkaProducer.send(record).get();
	    logger.info("Receive partition id= " + recordMetadata.partition() + " offset= " + recordMetadata.offset());
	}


	public int getNumberOfAssets() {
		return numberOfAssets;
	}

	public void setNumberOfAssets(int numberOfAsset) {
		this.numberOfAssets = numberOfAsset;
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
