package ibm.cte.pot;

import java.net.URI;
import java.net.URISyntaxException;

public class TestWebSocket {

	public static void main(String[] args) {
		System.out.println(
				  "##################### \n"
				+ " Tool to test web socket implementation with BFF running locally on port 8080 \n"
				+ "#####################");
		WebsocketClientEndpoint clientEndPoint;
		try {
            // open websocket
            clientEndPoint = new WebsocketClientEndpoint(new URI("ws://localhost:8080/assetstream"));

            // add listener
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            // send message to websocket
            clientEndPoint.sendMessage("hello");
            while (true) {
	            // wait 5 seconds for messages from websocket to come
	            Thread.sleep(5000);
            }
        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        } finally {
        	clientEndPoint = null;
        }
    }

}

