package ibm.cte.esp;

import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;

import org.junit.Test;

import ibm.cte.esp.model.AssetEvent;

class TestPumpSimulatorAsset {

	@Test
	void testAddOneAsset() {
		ApplicationConfig cfg = new ApplicationConfig();

		PumpSimulator ps = new PumpSimulator(cfg);
		ps.prepareProducer();
		AssetEvent a = new AssetEvent();
		a.setId("PUMP01");
		a.setAntivirus("v3.0");
		a.setCurrent((long)(110*Math.random()+10));
		a.setFlowRate((long)(100*Math.random()));
		a.setIpAddress("172.16.0.1");
		a.setOs("Raspbian");
		a.setPressure((long)(100*Math.random()));
		a.setTemperature((long)(200*Math.random()));
		a.setType("ESP");
		a.setRotation((long)(360*Math.random()));
		a.setVersion("0.0.1");
		a.setLatitude("30.307182");
		a.setLongitude("-97.755996");
		try {
			ps.publishAsset(a);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Exception in test");
		} catch (ExecutionException e) {
			e.printStackTrace();
			fail("Exception in test");
		}
	}

}
