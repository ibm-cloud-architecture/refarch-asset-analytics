package ibm.cte.kafka.test;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class EventProducer {
	public static Logger logger = Logger.getLogger(EventProducer.class);
	
	@Autowired
	  private KafkaTemplate<String, String> kafkaTemplate;

	  public void send(String topic, String payload) {
		  logger.info("sending payload= " + payload + " to topic=" + topic);
	    kafkaTemplate.send(topic, payload);
	  }
}
