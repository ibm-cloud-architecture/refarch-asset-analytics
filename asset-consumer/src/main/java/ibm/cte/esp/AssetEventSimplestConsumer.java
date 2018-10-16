package ibm.cte.esp;

import java.net.ConnectException;

import ibm.cte.esp.model.AssetEvent;

/**
 * Consume AssetEvent, and print to stdout, this is for verifying kafka integration and serialization
 * from json
 * 
 * @author jerome boyer
 *
 */
public class AssetEventSimplestConsumer {

	
	public static void main(String[] args) throws InstantiationException, ConnectException{
		ApplicationConfig cfg = new ApplicationConfig();
		String brokers = cfg.getConfig().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS);
		String topic = cfg.getConfig().getProperty(ApplicationConfig.KAFKA_ASSET_TOPIC_NAME);
		
		if(args.length == 2) {
			topic = args[0].toString();
			brokers = args[1].toString();
		}
		System.out.println("############ Text Consumer from topic: "+ topic +" on server "+ brokers +" ####### ");
		 
		AssetTopicConsumer consumer = new AssetTopicConsumer(cfg);
		while (true) {
			for ( AssetEvent a : consumer.consume()) {
	        	 System.out.println("AssetEvent :"+ a.toString());
	        }
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				consumer.close();
			}
		}
        
        
	}

}
