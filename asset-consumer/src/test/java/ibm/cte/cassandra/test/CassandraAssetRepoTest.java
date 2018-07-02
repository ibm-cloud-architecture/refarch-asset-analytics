package ibm.cte.cassandra.test;

import java.math.BigDecimal;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.cte.esp.AssetDAO;
import ibm.cte.esp.CassandraRepo;
import ibm.cte.esp.model.Asset;

public class CassandraAssetRepoTest {
	
	static CassandraRepo repo;
	@BeforeClass
	public static void init() {
		repo = new CassandraRepo();
	}
	
	@AfterClass
	public static void close() {
		repo.close();
	}

	@Test
	public void shouldAddNewAsset() throws Exception {
		Asset a = new Asset();
		a.setId("Asset022");
		a.setOs("Raspbian");
		a.setAntivirus("v2.3");
		a.setCurrent(new BigDecimal(110*Math.random()+10));
		a.setFlowRate(new BigDecimal(100*Math.random()));
		a.setIpAddress("172.16.0.0");
		a.setPressure(new BigDecimal(1000*Math.random()));
		a.setTemperature(new BigDecimal(300*Math.random()));
		a.setType("ESP");
		a.setRotation(new BigDecimal(360*Math.random()));
		a.setVersion("0.0.1");
		a.setLatitude(new Double(30.307182));
		a.setLongitude(new Double(-97.755996));
		repo.persistAsset(a);
		Asset aOut=repo.getAssetById("Asset022");
		Assert.assertNotNull(aOut);
		System.out.println(aOut.getOs());
	}
	
	@Test
	public void shouldGetAllAssets() throws Exception {
		List<Asset> l=repo.getAllAssets();
		Assert.assertTrue(l.size() >= 1);
		for (Asset a : l) {
			System.out.println(a.toString());
		}
	}

}
