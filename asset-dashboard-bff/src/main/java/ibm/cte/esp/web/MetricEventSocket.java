package ibm.cte.esp.web;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/push")
public class MetricEventSocket {
	Logger logger = Logger.getLogger(MetricEventSocket.class.getName());
    @OnMessage
    public void onMessage(String message, final Session session) {

        logger.info("Message from " + session.getId() + ": " + message);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    session.getBasicRemote().sendText("Push");
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        };
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 3 * 1000);
    }
}
