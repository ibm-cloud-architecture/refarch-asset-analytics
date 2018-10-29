package ibm.cte.kafka.test;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import ibm.cte.esp.spring.AppConfig;

public class TestSimpleSpringProducerConsumer {

	public static Logger logger = Logger.getLogger(TestSimpleSpringProducerConsumer.class);
	public static final String topic1 = "text-topic" ;
	public static String group = "consumergroup01";

	
	@Test
	public void testAutoCommit() throws Exception {
		    logger.setLevel(Level.INFO);
		    logger.info("Start auto");
		    ContainerProperties containerProps = new ContainerProperties(topic1, "topic2");
		    final CountDownLatch latch = new CountDownLatch(4);
		    
		    containerProps.setMessageListener(new MessageListener<Integer, String>() {
		        @Override
		        public void onMessage(ConsumerRecord<Integer, String> message) {
		            logger.info("received: " + message);
		            latch.countDown();
		        }

		    });
		    AppConfig appConfig = new AppConfig();
		    KafkaMessageListenerContainer<Integer, String> container = createContainer(appConfig,containerProps);
		    container.setBeanName("testAuto");
		    container.start();
		    Thread.sleep(1000); // wait a bit for the container to start
		    KafkaTemplate<Integer, String> template = createProducerTemplate(appConfig);
		    template.setDefaultTopic(topic1);
		    template.sendDefault(0, "Send message with key 0");
		    template.sendDefault(2, "Send message with key 2");
		    template.sendDefault(0, "Send 2nd message with key 0");
		    template.sendDefault(2, "Send 2nd message with key 2");
		    template.flush();
		    assertTrue(latch.await(60, TimeUnit.SECONDS));
		    container.stop();
		    logger.info("Stop auto");

		}

	private KafkaMessageListenerContainer<Integer, String> createContainer(AppConfig appConfig,ContainerProperties containerProps) {
			Map<String, Object> props = appConfig.consumerProps();
			DefaultKafkaConsumerFactory<Integer, String> cf =
			                new DefaultKafkaConsumerFactory<Integer, String>(props);
			KafkaMessageListenerContainer<Integer, String> container =
			                new KafkaMessageListenerContainer<>(cf, containerProps);
			return container;
	}
			
	private KafkaTemplate<Integer, String> createProducerTemplate(AppConfig appConfig) {
			Map<String, Object> senderProps = appConfig.producerProps();
			ProducerFactory<Integer, String> pf =
			  new DefaultKafkaProducerFactory<Integer, String>(senderProps);
			KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
			return template;
	}
	
	
}
