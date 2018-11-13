package ibm.cte.esp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import ibm.cte.esp.web.BffSocketHandler;

/**
 * Register websocket handler to a URL path.
We need Stomp to send a message only to users who are subscribed to a particular topic, or how to send a message to a particular user.

On a javascript side the connection is done using code like:
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
 * @author jerome boyer
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class BffSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	/**
	 * Define an in memory broker to support connection from the clients and carry message 
	 * on the destination /bfftopic
	 * Example of javacript code on the client side
    * Subscribe to the Public Topic
         stompClient.subscribe('/bfftopic/', onMessageReceived);
    	 // Send userid to the server
    	stompClient.send("/bff/identify",{}, JSON.stringify({userid: username})
    )
	 */
	public void configureMessageBroker(MessageBrokerRegistry registry) {
        // the prefix is used to filter destination
        registry.setApplicationDestinationPrefixes("/bffws");
        // message with the destination /bff should be routed to a message handling method (see class EventListener...)
        // messages whose destination starts with “/bfftopic” should be routed to the message broker
        registry.enableSimpleBroker("/bfftopic");
        // Message broker broadcasts messages to all the connected clients who are subscribed to a particular topic.
    }
	
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new BffSocketHandler(), "/ws");
	}
	
	/**
	 * Enable using STOMP protocol to the end point is /assetmetricstream
	 * SockJS is used to enable fallback options for browsers that don’t support websocket.
	 */
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws");
		registry.addEndpoint("/ws").withSockJS();
	}
}

