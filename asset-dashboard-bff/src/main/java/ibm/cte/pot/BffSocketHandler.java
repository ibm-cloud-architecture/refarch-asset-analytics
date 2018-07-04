package ibm.cte.pot;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import ibm.cte.esp.model.Asset;
import ibm.cte.pot.msg.KafkaAssetConsumer;

/**
 * records all the sessions once any websocket connection is established 
 * and broadcasts the message to all the sessions once any message is received
 * @author jerome boyer
 *
 */
public class BffSocketHandler extends TextWebSocketHandler {
	List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	KafkaAssetConsumer consumer = new KafkaAssetConsumer();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		
		for(WebSocketSession webSocketSession : sessions) {
			//Map value = new Gson().fromJson(message.getPayload(), Map.class);
			if (consumer != null) {
				for (String assetAsString : consumer.getNext()) {
					session.sendMessage(new TextMessage(assetAsString));
				}
			}
			
		}
	}

	/**
	 * Start listening
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		consumer = new KafkaAssetConsumer();
		sessions.add(session);	
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session,
            CloseStatus status)
     throws java.lang.Exception {
		if (consumer != null) {
			consumer.close();
		}
	}
}
