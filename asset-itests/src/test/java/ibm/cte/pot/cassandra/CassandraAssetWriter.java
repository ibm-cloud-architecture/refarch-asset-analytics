package ibm.cte.pot.cassandra;


import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Tool to prepare the Cassandra Asset schema and writes and read assets
 * @author jerome boyer
 *
 */
public class CassandraAssetWriter {

	 
	 public static void main(String[] args) { 
	  try { 
		   System.out.println("######## Start Cassandra Assets Writer Tool #######");
		   System.out.println(" Be sure port forwarding is done to connect to k8s pod with:");
		   System.out.println(" kubectl port-forward cassandra-0 9042:9042");
		   ApplicationConfig cfg = new ApplicationConfig();
		   CassandraRepo cassandra = new CassandraRepo(cfg);
		   listExitingAssets(cassandra);
		   cassandra.close();
	  } catch (Exception e) { 
	      e.printStackTrace(); 
	  } 
	 
	 } 
	 
	 private static void listExitingAssets( CassandraRepo cassandra) throws Exception {
		  List<Asset> l = cassandra.getAllAssets();
		  if (l == null || l.size() == 0) {
			  System.out.println(" No assets found");
			  write3Assets(cassandra);
		  } else {
			  for (Asset a: l) {
				  System.out.println(a.toString());
			  }
		  }
	}

	 
	 private static void write3Assets(CassandraRepo cassandra) throws Exception {
		   Asset a= new Asset("1","IOS", "iphone", "AV567", "172.16.40.1", "v0.0.1"); 
		   cassandra.persistAsset(a);
		   a= new Asset("2","RASPBIAN", "raspberry pi", "AV567", "172.16.40.5", "v0.10.0"); 
		   cassandra.persistAsset(a);
		   a= new Asset("3","IOS", "iphone", "AV567", "172.16.40.56", "v0.0.8"); 
		   cassandra.persistAsset(a);
	 }
}
