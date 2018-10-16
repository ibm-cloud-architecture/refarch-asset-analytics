package ibm.cte.esp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestPumpSimulatorEvent {

	@Test
	void testPumpEventEmission() {
		ApplicationConfig cfg = new ApplicationConfig();
		cfg.getConfig().setProperty(ApplicationConfig.NB_OF_EVENTS,  "6");
		PumpSimulator ps = new PumpSimulator();
		ps.generateEvents();
	}

}
