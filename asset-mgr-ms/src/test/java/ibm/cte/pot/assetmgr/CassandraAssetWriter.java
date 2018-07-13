package ibm.cte.pot.assetmgr;

import java.util.List;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import ibm.cte.pot.assetmgr.model.Asset;

/**
 * Tool to prepare the Cassandra Asset schema and writes and read assets
 * @author jerome boyer
 *
 */
public class CassandraAssetWriter {

	 private static Cluster cluster; 
	 private static Session session; 
	 
	 public static void main(String[] args) { 
	  try { 
		   System.out.println("######## Start Cassandra Assets Tool #######");
		   System.out.println(" Be sure port forwarding is done to connect to k8s pod with:");
		   System.out.println(" kubectl port-forward cassandra-0 9042:9042");
		   cluster = Cluster.builder().addContactPoints("localhost").build();  
		   session = cluster.connect("assetmonitoring"); 
		   boolean continu = true;
		   if (session == null) {
			   continu=createKeySpace("assetmonitoring","SimpleStrategy","1");
		   }
		   if (continu) {
			   listExitingAssets();
		   }
		   
		   session.close();
		   cluster.close();
	  } catch (Exception e) { 
	      e.printStackTrace(); 
	  } 
	 
	 } 
	 
	 private static void listExitingAssets() {
		  CassandraOperations cassandraOps = new CassandraTemplate(session); 
		  Select s = QueryBuilder.select().from("assets"); 
		  List<Asset> l = cassandraOps.select(s, Asset.class);
		  // write3Assets();
		  if (l == null || l.size() == 0) {
			  System.out.println(" No assets found");
			  write3Assets();
		  } else {
			  for (Asset a: l) {
				  System.out.println(a.toString());
			  }
		  }
		  
	}

	public static boolean createKeySpace(String keyspaceName,
			 String replicationStrategy,
			 String replicationFactor) {
		 StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
				      .append(keyspaceName).append(" WITH replication = {")
				      .append("'class':'").append(replicationStrategy)
				      .append("','replication_factor':").append(replicationFactor)
				      .append("};");

		String query = sb.toString();
		try {
			session.execute(query);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	 }
	 
	 private static void writeAsset(Asset a) {
		 CassandraOperations cassandraOps = new CassandraTemplate(session); 
		  cassandraOps.insert(a);
	 }
	 
	 public static void write3Assets() {
		   Asset a= new Asset("1","IOS", "iphone", "AV567", "172.16.40.1", "v0.0.1"); 
		   writeAsset(a);
		   a= new Asset("2","RASPBIAN", "raspberry pi", "AV567", "172.16.40.5", "v0.10.0"); 
		   writeAsset(a);
		   a= new Asset("3","IOS", "iphone", "AV567", "172.16.40.56", "v0.0.8"); 
		   writeAsset(a);
	 }
}
