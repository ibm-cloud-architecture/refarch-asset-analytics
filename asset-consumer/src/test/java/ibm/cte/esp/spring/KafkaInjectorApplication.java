package ibm.cte.esp.spring;

import java.util.Map;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import com.google.gson.Gson;

import ibm.cte.esp.model.Asset;

/**
 * listen all events coming from input topic in the form of Asset and persist
 * them into Cassandra
 * 
 * @author jeromeboyer
 *
 */
@SpringBootApplication
public class KafkaInjectorApplication implements CommandLineRunner  {
	
	public static Logger logger = Logger.getLogger(KafkaInjectorApplication.class.getName());
	
	 public static void main(String[] args) {
		    SpringApplication.run(KafkaInjectorApplication.class, args);
	}
	 
	@Autowired
	private KafkaTemplate<String, String> template;

	@Autowired
	private AssetRepository cassandra;
	
	@Override
	public void run(String... args) throws Exception {
		AppConfig appConfig = new AppConfig();
		Gson gson = new Gson();
		
		
		ContainerProperties containerProps = new ContainerProperties(AppConfig.INTOPIC);
	    
	    containerProps.setMessageListener(new MessageListener<String, String>() {
	        @Override
	        public void onMessage(ConsumerRecord<String, String> message) {
	            logger.info("received: " + message);
	            Asset a = gson.fromJson(message.value(),Asset.class);
	            
	            cassandra.save(a);
	        }

	    });
		Map<String, Object> props = appConfig.consumerProps();
		DefaultKafkaConsumerFactory<Integer, String> cf =
		                new DefaultKafkaConsumerFactory<Integer, String>(props);
		KafkaMessageListenerContainer<Integer, String> container =
		                new KafkaMessageListenerContainer<>(cf, containerProps);
	    container.setBeanName("AssetListener");
	    container.start();
	}
}
