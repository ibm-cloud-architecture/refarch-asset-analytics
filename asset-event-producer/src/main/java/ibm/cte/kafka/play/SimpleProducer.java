package ibm.cte.kafka.play;

import java.net.ConnectException;
import java.util.concurrent.ExecutionException;

import application.kafka.Producer;


/**
 * This is a producer of text lines used later by stream application to count words
 * @author jeromeboyer
 *
 */
public class SimpleProducer {
	private static String[] textToSend = { "this is the first line",
	                                      "and we like the second line too",
	                                      "no at least the bye bye. line" };
	private static String TOPICNAME = "streams-plaintext-input";
	private static String BOOTSTRAP_SERVER = "green-kafka-cluster:32688";

     
	public static void main(String[] args) throws InstantiationException, ConnectException, InterruptedException, ExecutionException {
		String topic = TOPICNAME;
		String broker = BOOTSTRAP_SERVER;
		if(args.length == 2) {
			topic = args[0].toString();
			broker = args[1].toString();
		}
		System.out.println("############ Text Producer to topic: "+ topic +" on server "+ broker +" ####### ");
		Producer producer = new Producer(broker,topic);
        for (String line : textToSend) {
        	 producer.produce(line);
        	 System.out.println(line + " -> sent");
         }
         producer.shutdown();
	}

}
