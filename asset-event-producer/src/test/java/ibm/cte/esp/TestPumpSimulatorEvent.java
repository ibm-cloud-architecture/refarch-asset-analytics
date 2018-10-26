package ibm.cte.esp;

import org.junit.Test;

public class TestPumpSimulatorEvent {

	@Test
	public void testPumpEventEmission() {
		ApplicationConfig cfg = new ApplicationConfig();
		cfg.getConfig().setProperty(ApplicationConfig.NB_OF_EVENTS,  "6");
		PumpSimulator ps = new PumpSimulator();
		ps.generateEvents();
	}

}
