package ibm.cte.pot.msg;

import java.util.logging.Logger;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaAssetConsumer {

	    private static final Logger LOG = Logger.getLogger(KafkaAssetConsumer.class.getName());

	    @KafkaListener(topics = "${app.topic.test}")
	    public void listen(@Payload String message) {
	    	//  Gson gson = new Gson();
	        LOG.info("received message= " + message);
	    }
}
