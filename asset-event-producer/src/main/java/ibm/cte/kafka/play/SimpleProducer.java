package ibm.cte.kafka.play;

import java.net.ConnectException;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.RecordMetadata;

import application.kafka.Producer;


/**
 * This is a producer of text lines used later by stream application to count words
 * @author jeromeboyer
 *
 */
public class SimpleProducer  extends SimpleKafkaBase  {
	private static String[] textToSend = { "this is the first line",
	                                      "and we like the second line too",
	                                      "no at least the bye bye. line" };
	
	public static void main(String[] args)  {
		String topic = TOPICNAME;
		String broker = BOOTSTRAP_SERVERS;
		if(args.length == 2) {
			topic = args[0].toString();
			broker = args[1].toString();
		}
		System.out.println("############ Text Producer to topic: "+ topic +" on server "+ broker +" ####### ");
		Producer producer = null;
		try {
			producer =  new Producer(broker,topic);
			 for (String line : textToSend) {
	        	 System.out.print(line);
	        	 RecordMetadata recordMetadata= producer.produce(line);
	        	 System.out.println(" -> sent" + recordMetadata.offset());
	         }
	         producer.shutdown();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			 if (producer != null) producer.shutdown();
		}
       
	}

}
