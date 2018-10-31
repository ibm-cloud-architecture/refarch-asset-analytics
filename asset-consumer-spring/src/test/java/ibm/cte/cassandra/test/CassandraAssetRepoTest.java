package ibm.cte.cassandra.test;

import java.math.BigDecimal;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.cte.esp.ApplicationConfig;
import ibm.cte.esp.dao.CassandraRepo;
import ibm.cte.esp.model.AssetEvent;

public class CassandraAssetRepoTest extends BaseTest {
	
	static CassandraRepo repo = null;
	@BeforeClass
	public static void init() {
		repo = new CassandraRepo(cassandra.session,"assetmonitoring","assets");
	}
	
	@AfterClass
	public static void close() {
		if (repo != null)
			repo.close();
	}

	@Test
	public void shouldAddNewAsset() throws Exception {
		AssetEvent a = new AssetEvent();
		a.setId("Asset022");
		a.setOs("Raspbian");
		a.setAntivirus("v2.3");
		a.setCurrent(110*Math.random()+10);
		a.setFlowRate((long) (100*Math.random()));
		a.setIpAddress("172.16.0.0");
		a.setPressure((int) (1000*Math.random()));
		a.setTemperature((int) (300*Math.random()));
		a.setType("ESP");
		a.setRotation((int) (360*Math.random()));
		a.setVersion("0.0.1");
		a.setLatitude("30.307182");
		a.setLongitude("-97.755996");
		repo.persistAsset(a);
		AssetEvent aOut=repo.getAssetById("Asset022");
		Assert.assertNotNull(aOut);
		System.out.println(aOut.getOs());
	}
	
	@Test
	public void shouldGetAllAssets() throws Exception {
		List<AssetEvent> l=repo.getAllAssets();
		Assert.assertTrue(l.size() >= 1);
		for (AssetEvent a : l) {
			System.out.println(a.toString());
		}
	}

}
