package ibm.cte.esp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import ibm.cte.esp.model.AssetEvent;
import ibm.cte.esp.model.MetricEvent;

/**
 * Simulate a Electrical Pump generating events for rotator, preassure and temperature metrics.
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
	private String pumpId = "ND01";
	private boolean event = false;
	private ApplicationConfig config;

	private KafkaProducer<String, Object> kafkaProducer;

	public PumpSimulator() {
		config = new ApplicationConfig();
	}
	
	public PumpSimulator(ApplicationConfig cfg) {
		config = cfg;
	}


	public static void main(String[] args) throws InterruptedException, ExecutionException {
		logger.info("######### Pump Simulator Starting ############ ");
		PumpSimulator simulator= new PumpSimulator();
		simulator.processArgument(args);
		simulator.prepareProducer();
		logger.info("Kafka server: " + simulator.config.getConfig().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS));
		if (simulator.isEvent()) {
			logger.info("");
			simulator.generateAssetMetricEvents();
		} else {
			simulator.generateAssets();

			simulator.shutdown();
		}
	}


	/**
	 * Generate pump events every n seconds using one of the available pattern:
	 - temperature decrease
	 - preassure decrease over time
	 - preassure increase
	 */
	public List<MetricEvent> generateAssetMetricEvents() {
		int baseT = 60;
		int baseP = 100;
		int baseFlowRate = 100;
		int baseRotation = 0;
		int baseC =110;
		
		int nbOfEvents = Integer.parseInt(config.getConfig().getProperty(ApplicationConfig.NB_OF_EVENTS));
		List<MetricEvent> lme = new ArrayList<MetricEvent>(nbOfEvents);
		while (lme.size() < nbOfEvents) {
			MetricEvent ae = new MetricEvent();
			ae.setAssetId(pumpId);
			ae.setTemperature(baseT);
			ae.setPressure(baseP);
			ae.setFlowRate(baseFlowRate);
			ae.setRotation(baseRotation);
			ae.setCurrent(baseC);
			switch (config.getConfig().getProperty(ApplicationConfig.EVENT_PATTERN)) {
				case "TDrop":
				case "PDrop":
				case "CDrop":
				case "FDrop":
				case "RDrop":
					lme.addAll(metricDropPattern(ae,nbOfEvents,config.getConfig().getProperty(ApplicationConfig.EVENT_PATTERN)));
				 	break;	
				default: lme.add(ae);
			}
		}
		return lme;
	}

	
	
	private List<MetricEvent> metricDropPattern(MetricEvent ae, int nbevents, String pattern) {
		List<MetricEvent> lme = new ArrayList<MetricEvent>();
		lme.add(ae);
		long stepT = stepper(ae,nbevents,pattern);
		MetricEvent me = ae;
		for (int i = 1; i< nbevents; i++) {
			me = fromEvent(me);
			switch (pattern) {
				case "TDrop" :  
					me.setTemperature(ae.getTemperature() - i * stepT);
					break;
				case "PDrop" :
					me.setPressure(ae.getPressure() - i * stepT);
					break;
				case "RDrop" :
					me.setRotation(ae.getRotation() - i * stepT);
					break;
				case "FDrop" :
					me.setFlowRate(ae.getFlowRate() - i * stepT);
					break;
				case "CDrop" :
					me.setCurrent(ae.getCurrent() - i * stepT);
					break;
			}
			
			lme.add(me);
		}
		return lme;
	}
	
	private long stepper(MetricEvent ae, int nbevents, String pattern) {
		switch (pattern) {
			case "TDrop" : return Math.floorDiv(ae.getTemperature(),nbevents);
			case "PDrop" : return Math.floorDiv(ae.getPressure(),nbevents);
			case "RDrop" : return Math.floorDiv(ae.getRotation(),nbevents);
			case "FDrop" : return Math.floorDiv(ae.getFlowRate(),nbevents);
			case "CDrop" : return Math.floorDiv(ae.getCurrent(),nbevents);
		}
		return 10;
	}
	
	private MetricEvent fromEvent(MetricEvent e) {
		MetricEvent me = new MetricEvent();
		me.setAssetId(e.getAssetId());
		me.setCurrent(e.getCurrent());
		me.setTemperature(e.getTemperature());
		me.setFlowRate(e.getFlowRate());
		me.setPressure(e.getPressure());
		me.setRotation(e.getRotation());
		me.setTimeStamp(new Date(e.getTimeStamp().getTime()+1000));
		return me;
	}
	public void shutdown() {
		this.kafkaProducer.close();
	}

	public void prepareProducer() {
		Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        		config.getConfig().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS));
        properties.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 10000);
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 4000);
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);  // retries in case of failure
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);  // size for the buffer
        properties.put(ProducerConfig.LINGER_MS_CONFIG,1); // milli seconds before sending the record. Help batching
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 16000000); //total amount of memory available to the producer for buffering
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.kafkaProducer = new KafkaProducer<>(properties);

		if (event) {
			logger.info("Pump Simulator sending pump event to "
		    + config.getConfig().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS)
		    + " every " + timeGap + " ms");
		} else {
			logger.info("Pump Simulator sending " + numberOfAssets
					+ " new asset event to "+ config.getConfig().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS)  +" every " + timeGap + " ms");
		}
	}

	/**
	The argument can be
	 0 -> topicname to be used to publish events
	 1 -> kafka server name or ip address
	 2 -> number of events to be produce or "--event"
	    in case of event
			3 -> pattern
			4 -> increase or decrease
			5 -> event frequency
			6 -> number of event to generate
			7 -> pumpid
	*/
	public void processArgument(String[] args) {
		if (args.length >= 3) {
			config.getConfig().setProperty(ApplicationConfig.KAFKA_ASSET_TOPIC_NAME, args[0]);
			config.getConfig().setProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS, args[1]);
			if (args[2].contains("event")) {
				event = true;
				config.getConfig().setProperty(ApplicationConfig.EVENT_PATTERN, args[3]);
				config.getConfig().setProperty(ApplicationConfig.METRICS_TRENT, args[4]);
				config.getConfig().setProperty(ApplicationConfig.EVENT_FREQ, args[5]);
				config.getConfig().setProperty(ApplicationConfig.NB_OF_EVENTS, args[6]);
				pumpId=args[7];
			} else {
				numberOfAssets = Integer.parseInt(args[2]);
			}
		}
	}

	public  void generateAssets() throws InterruptedException, ExecutionException {
		for (int i = 0; i < numberOfAssets; i++) {
			String uid= java.util.UUID.randomUUID().toString();
			AssetEvent a = new AssetEvent();
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
			a.setLatitude("30.307182");
			a.setLongitude("-97.755996");

			publishAsset(a,config.getConfig().getProperty(ApplicationConfig.KAFKA_ASSET_TOPIC_NAME));
			Thread.sleep(1000);
		}
	} // generateAssets

	public  void publishAsset(AssetEvent a, String topic) throws InterruptedException, ExecutionException {
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String s = gson.toJson(a);
		logger.info("Send Asset: " + s);
		ProducerRecord<String, Object> record = new ProducerRecord<>(topic, a.getId(), s);
	    RecordMetadata recordMetadata = kafkaProducer.send(record).get();
	    logger.info("Receive partition id= " + recordMetadata.partition() + " offset= " + recordMetadata.offset());
	}

	public void publishAsset(AssetEvent a) throws InterruptedException, ExecutionException {
		publishAsset(a,config.getConfig().getProperty(ApplicationConfig.KAFKA_ASSET_TOPIC_NAME));
	}
	
	public void publishAssetMetric(MetricEvent a) throws InterruptedException, ExecutionException {
		publishMetricEvent(a,config.getConfig().getProperty(ApplicationConfig.KAFKA_ASSET_TOPIC_NAME));
	}
	
	public  void publishMetricEvent(MetricEvent a, String topic) throws InterruptedException, ExecutionException {
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String s = gson.toJson(a);
		logger.info("Send Asset: " + s);
		ProducerRecord<String, Object> record = new ProducerRecord<>(topic, a.getAssetId(), s);
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
