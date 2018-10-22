package ibm.cte.esp;

import org.junit.Test;

class TestPumpSimulatorEvent {

	@Test
	void testPumpEventEmission() {
		ApplicationConfig cfg = new ApplicationConfig();
		cfg.getConfig().setProperty(ApplicationConfig.NB_OF_EVENTS,  "6");
		PumpSimulator ps = new PumpSimulator();
		ps.generateEvents();
	}

}
