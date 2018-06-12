package ibm.cte.kafka.play;

import java.net.ConnectException;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import application.kafka.Consumer;




/**
 * This is a producer of text lines used later by stream application to count words
 * @author jeromeboyer
 *
 */
public class WordCountConsumer {
	private static String BOOTSTRAP_SERVERS = "green-kafka-cluster:32688,192.168.1.89:30092";
	private static String TOPICNAME = "streams-wordcount-output";
     
	public static void main(String[] args) throws InstantiationException, ConnectException, InterruptedException, ExecutionException {
		String brokers = BOOTSTRAP_SERVERS;
		String topic = TOPICNAME;
		
		if(args.length == 2) {
			topic = args[0].toString();
			brokers = args[1].toString();
		}
		System.out.println("############ Text Consumer from topic: "+ topic +" on server "+ brokers +" ####### ");
		 
		Consumer consumer = new Consumer(brokers,topic);
        for (ConsumerRecord<String, String> record : consumer.consume()) {
         
        	 System.out.println(record.toString());
        }
        consumer.shutdown();
	}

}
