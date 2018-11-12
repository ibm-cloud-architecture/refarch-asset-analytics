package ibm.cte.esp;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ibm.cte.esp.model.MetricEvent;

public class TestPumpSimulatorEvent {

	//@Test
	public void testPumpEventEmission() {
		ApplicationConfig cfg = new ApplicationConfig();
		cfg.getConfig().setProperty(ApplicationConfig.NB_OF_EVENTS,  "6");
		PumpSimulator ps = new PumpSimulator(cfg);
		ps.generateAssetMetricEvents();
	}
	
	@Test
	public void testPumpTemperatureDrop() {
		ApplicationConfig cfg = new ApplicationConfig();
		cfg.getConfig().setProperty(ApplicationConfig.NB_OF_EVENTS,  "15");
		cfg.getConfig().setProperty(ApplicationConfig.EVENT_PATTERN, "TDrop");
		PumpSimulator ps = new PumpSimulator(cfg);
		List<MetricEvent> l = ps.generateAssetMetricEvents();
		for (MetricEvent e: l) {
			System.out.println(e.toString());
		}
		Assert.assertTrue(l.size() == 15 );
	}

	@Test
	public void testPumpPressureDrop() {
		ApplicationConfig cfg = new ApplicationConfig();
		cfg.getConfig().setProperty(ApplicationConfig.NB_OF_EVENTS,  "10");
		cfg.getConfig().setProperty(ApplicationConfig.EVENT_PATTERN, "PDrop");
		PumpSimulator ps = new PumpSimulator(cfg);
		List<MetricEvent> l = ps.generateAssetMetricEvents();
		for (MetricEvent e: l) {
			System.out.println(e.toString());
		}
		Assert.assertTrue(l.size() == 10 );
	}
	
	@Test
	public void testPumpOnePressureDrop() {
		ApplicationConfig cfg = new ApplicationConfig();
		cfg.getConfig().setProperty(ApplicationConfig.NB_OF_EVENTS,  "2");
		cfg.getConfig().setProperty(ApplicationConfig.EVENT_PATTERN, "PDrop");
		PumpSimulator ps = new PumpSimulator(cfg);	
		List<MetricEvent> l = ps.generateAssetMetricEvents();
		Assert.assertTrue(l.size() == 2 );
		Assert.assertTrue(l.get(1).getPressure() == 50);
	}
	
	@Test
	public void testPumpOneFlowRateDrop() {
		ApplicationConfig cfg = new ApplicationConfig();
		cfg.getConfig().setProperty(ApplicationConfig.NB_OF_EVENTS,  "2");
		cfg.getConfig().setProperty(ApplicationConfig.EVENT_PATTERN, "FDrop");
		PumpSimulator ps = new PumpSimulator(cfg);	
		List<MetricEvent> l = ps.generateAssetMetricEvents();
		Assert.assertTrue(l.size() == 2 );
		MetricEvent e = l.get(1);
		System.out.println(e.toString());
		Assert.assertTrue(e.getFlowRate() == 50);
	}
	
	@Test
	public void testPumpOneCurrentDrop() {
		ApplicationConfig cfg = new ApplicationConfig();
		cfg.getConfig().setProperty(ApplicationConfig.NB_OF_EVENTS,  "2");
		cfg.getConfig().setProperty(ApplicationConfig.EVENT_PATTERN, "CDrop");
		PumpSimulator ps = new PumpSimulator(cfg);	
		List<MetricEvent> l = ps.generateAssetMetricEvents();
		
		Assert.assertTrue(l.size() == 2 );
		MetricEvent e = l.get(1);
		System.out.println(e.toString());
		Assert.assertTrue(e.getCurrent() == 55);
	}
}
