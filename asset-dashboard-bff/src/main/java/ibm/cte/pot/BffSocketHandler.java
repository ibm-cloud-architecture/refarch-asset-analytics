package ibm.cte.pot;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import ibm.cte.esp.model.Asset;
import ibm.cte.pot.msg.KafkaAssetConsumer;

/**
 * This is the socket handler to process websocket clients asking for 'new asset event'.
 * We can assume they may have more then one client so it records all the sessions once any websocket connection is established 
 *
 * The code uses the kafka consumer to subscribe to 'new asset events'. 
 * 
 * @author jerome boyer
 *
 */
public class BffSocketHandler extends TextWebSocketHandler {
	
	Logger logger = Logger.getLogger(BffSocketHandler.class.getName());
	
	// keep the list of open sessions
	List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	
	KafkaAssetConsumer consumer = null;

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		// the message has a time stamp from which we can load assets from topic
		// TODO
		logger.info("Receive a message from socket client:" + message.getPayload());
	}

	/**
	 * Start listening
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);	
		if (consumer == null) {
			consumer = new KafkaAssetConsumer(this);
			consumer.start();
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session,
            CloseStatus status)
     throws java.lang.Exception {
		if (consumer != null) {
			consumer.close();
		}
	}
	
	public void broadcastMessages( List<String> messages) {
		logger.info("should broadcast " + messages.size() + " messages");
		for(WebSocketSession webSocketSession : sessions) {
			for (String assetAsString : messages) {
				try {
					webSocketSession.sendMessage(new TextMessage(assetAsString));
				} catch (IOException e) {
					// may be dropping message is not an issue
					logger.severe("message not sent to " + webSocketSession.getRemoteAddress());
				}
			}
		}
	}
}
