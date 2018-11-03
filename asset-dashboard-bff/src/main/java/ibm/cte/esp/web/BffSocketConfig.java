package ibm.cte.esp.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Register websocket handler to a URL path.
 * 
 * @author jeromeboyer
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class BffSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/bff");
    }
	
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new BffSocketHandler(), "/assetmetricstream");
	}
	
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/assetmetricstream");
		registry.addEndpoint("/assetmetricstream").withSockJS();
	}
}

