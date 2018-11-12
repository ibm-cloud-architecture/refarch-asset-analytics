package ibm.cte.esp;

import java.net.URI;
import java.net.URISyntaxException;

public class TestWebSocket {
	// private static final String SERVERNAME = "asset.bff.green";
	private static final String SERVERNAME = "localhost";
	private static final String PORT = "8081";
	
	public static void main(String[] args) {
		System.out.println(
				  "##################### \n"
				+ " Tool to test web socket implementation with BFF running locally on port " + PORT +"\n"
				+ "#####################");
		WebsocketClientEndpoint clientEndPoint;
		try {
            // open websocket
            clientEndPoint = new WebsocketClientEndpoint(new URI("ws://" + SERVERNAME + ":" + PORT + "/assetmanager/push"));

            // add listener
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            // send message to websocket
            clientEndPoint.sendMessage("hello I'm the test web socket client v0.0.1");
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

