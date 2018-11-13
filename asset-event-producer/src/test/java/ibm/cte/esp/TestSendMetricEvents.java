package ibm.cte.esp;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import ibm.cte.esp.model.MetricEvent;

public class TestSendMetricEvents {

	@Test
	public void testSendingTwoMetricEvents() {
		ApplicationConfig cfg = new ApplicationConfig();
		cfg.getConfig().setProperty(ApplicationConfig.NB_OF_EVENTS,  "2");
		cfg.getConfig().setProperty(ApplicationConfig.EVENT_PATTERN, "FDrop");
		PumpSimulator ps = new PumpSimulator(cfg);	
		ps.prepareProducer();
		List<MetricEvent> l = ps.generateAssetMetricEvents();
		for (MetricEvent me: l) {
			try {
				ps.publishAssetMetric(me);
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail("Execption with kafka connection");
			} catch (ExecutionException e) {
				e.printStackTrace();
				fail("Execption with kafka connection");
			}
		}
		
	}

}
