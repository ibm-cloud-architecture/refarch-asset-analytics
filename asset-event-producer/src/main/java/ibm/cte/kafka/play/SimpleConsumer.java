package ibm.cte.kafka.play;

import java.net.ConnectException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import application.kafka.Consumer;
import ibm.cte.esp.ApplicationConfig;




/**
 * This is a producer of text lines used later by stream application to count words
 * @author jeromeboyer
 *
 */
public class SimpleConsumer extends SimpleKafkaBase {

	
	public static void main(String[] args) throws InstantiationException, ConnectException{
		String topic = config.getConfig().getProperty(ApplicationConfig.KAFKA_TEXT_TOPIC_NAME);
		String brokers = config.getConfig().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS);
		
		if(args.length == 2) {
			topic = args[0].toString();
			brokers = args[1].toString();
		}
		System.out.println("############ Text Consumer from topic: "+ topic +" on server "+ brokers +" ####### ");
		 
		Consumer consumer = new Consumer(brokers,topic);
		while (true) {
			for (ConsumerRecord<String, String> record : consumer.consume()) {
	        	 System.out.println("Received :"+ record.toString());
	        }
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				consumer.shutdown();
			}
		}
        
        
	}

}
