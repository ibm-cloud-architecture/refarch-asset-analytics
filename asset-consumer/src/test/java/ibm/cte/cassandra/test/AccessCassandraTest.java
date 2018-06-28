package ibm.cte.cassandra.test;

import org.junit.Test;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class AccessCassandraTest {
	
	@Test
	public void testAccess() {
		Cluster cluster = null;
		try {
			// cluster holds metadata about cassandra cluster - create one instance of this class per app.
		    cluster = Cluster.builder()                                                    
		            .addContactPoint("127.0.0.1")
		            .build();
		    // session should be reused
		    Session session = cluster.connect();                                          
		    // send query to cassandra
		    ResultSet rs = session.execute("select release_version from system.local");    
		    // extract row
		    Row row = rs.one();
		    // extract column
		    System.out.println(row.getString("release_version"));                         
		} finally {
		    if (cluster != null) cluster.close();                                         
		}
	}
}
