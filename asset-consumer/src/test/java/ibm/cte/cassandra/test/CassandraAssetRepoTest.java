package ibm.cte.cassandra.test;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.cte.esp.dao.CassandraRepo;
import ibm.cte.esp.model.MetricEvent;

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
		MetricEvent a = new MetricEvent();
		a.setId("Asset022");
		a.setCurrent(110*Math.random()+10);
		a.setFlowRate((int) (100*Math.random()));

		a.setPressure((int) (1000*Math.random()));
		a.setTemperature((int) (300*Math.random()));

		a.setRotation((int) (360*Math.random()));
		repo.persistMetricEvent(a);
		MetricEvent aOut=repo.getAssetById("Asset022");
		Assert.assertNotNull(aOut);
		System.out.println(aOut.getTemperature());
	}
	
	@Test
	public void shouldGetAllAssets() throws Exception {
		List<MetricEvent> l=repo.getAllAssets();
		Assert.assertTrue(l.size() >= 1);
		for (MetricEvent a : l) {
			System.out.println(a.toString());
		}
	}

}
