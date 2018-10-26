package ibm.cte.esp.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Register websocket handler to a URL path.
 * 
 * @author jeromeboyer
 *
 */
@Configuration
@EnableWebSocket
public class BffSocketConfig implements WebSocketConfigurer {
	
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new BffSocketHandler(), "/assetstream");
	}
}

