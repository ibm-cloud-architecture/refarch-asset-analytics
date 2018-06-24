package ibm.cte.pot;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * records all the session once any websocket connection is established 
 * and broadcasts the message to all the sessions once any message is received
 * @author jerome boyer
 *
 */
public class BffSocketHandler extends TextWebSocketHandler {
	List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		
		for(WebSocketSession webSocketSession : sessions) {
			//Map value = new Gson().fromJson(message.getPayload(), Map.class);
			webSocketSession.sendMessage(new TextMessage("Hello  from BFF !"));
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		//the messages will be broadcasted to all connected users.
		sessions.add(session);
	}
}
