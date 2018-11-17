package ibm.cte.esp;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ibm.cte.esp.ApplicationConfig;
import ibm.cte.esp.client.AssetEventMgrClient;
import ibm.cte.esp.model.MetricEvent;

public class TestAssetEventMgrClient {

	ApplicationConfig cfg ;
	AssetEventMgrClient client;
	@Before
	public void init() {
		cfg = new ApplicationConfig();
		client = new AssetEventMgrClient(cfg);
	}
	
	//@Test 
	public void testSaveAsset() {
		MetricEvent a = new MetricEvent();
		a.setId("PUMP03");
		a.setCurrent(110*Math.random()+10);
		a.setFlowRate((int) (100*Math.random()));
		a.setPressure((int) (1000*Math.random()));
		a.setTemperature((int) (300*Math.random()));
		
		a.setRotation((int) (360*Math.random()));
		try {
			client.saveAsset(a);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception in test");
		}
	}
	
	//@Test
	public void testAccessById() {
		MetricEvent ae = null;;
		try {
			ae = client.getAssetById("PUMP03");
			Assert.assertNotNull(ae);
			System.out.println(ae.toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception in test");
		}
	}

}
