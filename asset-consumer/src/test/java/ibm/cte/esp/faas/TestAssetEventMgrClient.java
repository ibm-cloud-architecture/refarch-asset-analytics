package ibm.cte.esp.faas;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ibm.cte.esp.client.AssetEventMgrClient;
import ibm.cte.esp.model.AssetEvent;


public class TestAssetEventMgrClient {

	@Test
	public void testGetAllAssets(){
		AssetEventMgrClient client = new AssetEventMgrClient();
		List<AssetEvent> ael = null;;
		try {
			ael = client.getAllAssets();
			Assert.assertNotNull(ael);
			for (AssetEvent ae : ael) {
				System.out.println(ae.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception in test");
		}
	}
	
	//@Test 
	public void testSaveAsset() {
		AssetEventMgrClient client = new AssetEventMgrClient();
		AssetEvent a = new AssetEvent();
		a.setId("PUMP03");
		a.setAntivirus("v3.0");
		a.setCurrent(new BigDecimal(110*Math.random()+10));
		a.setFlowRate(new BigDecimal(100*Math.random()));
		a.setIpAddress("172.16.0.3");
		a.setOs("Raspbian");
		a.setPressure(new BigDecimal(1000*Math.random()));
		a.setTemperature(new BigDecimal(300*Math.random()));
		a.setType("ESP");
		a.setRotation(new BigDecimal(360*Math.random()));
		a.setVersion("0.0.1");
		a.setLatitude(30.2672);
		a.setLongitude(97.7431);
		try {
			client.saveAsset(a);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception in test");
		}
	}
	
	@Test
	public void testAccessById() {
		AssetEventMgrClient client = new AssetEventMgrClient();
		AssetEvent ae = null;;
		try {
			ae = client.getAssetById("PUMP01");
			Assert.assertNotNull(ae);
			System.out.println(ae.toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception in test");
		}
	}

}
