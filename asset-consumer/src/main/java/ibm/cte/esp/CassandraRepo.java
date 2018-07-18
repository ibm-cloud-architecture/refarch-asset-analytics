package ibm.cte.esp;



import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import ibm.cte.esp.model.Asset;

public class CassandraRepo implements AssetDAO {
	private static Logger logger = Logger.getLogger("CassandraRepo");
	// can be reused as they are thread safe
	public static  Cluster cluster = null;
	public static Session session ;	
	public static Integer port; 
	public ApplicationConfig cfg;
	
	public CassandraRepo(ApplicationConfig cfg) {
		this.cfg = cfg;
		Builder b;
		String ep = cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_ENDPOINTS);
		if (ep.contains(",")) {
			b = Cluster.builder().addContactPoints(ep.split(","));
		} else {
			b = Cluster.builder().addContactPoint(ep);
		}
		
		
		port = new Integer(cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_PORT));
		if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();
        session = cluster.connect();
        createKeyspace(cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_KEYSPACE),
        		cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_STRATEGY),
        		Integer.parseInt(cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_REPLICAS)));
        createTable();
	}
	
   
    public void close() {
        session.close();
        cluster.close();
    }
    
    private void createKeyspace(String keyspaceName, String replicationStrategy, int replicationFactor) {
	  StringBuilder sb = 
	    new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
	      .append(keyspaceName).append(" WITH replication = {")
	      .append("'class':'")
	      .append(replicationStrategy)
	      .append("','replication_factor':")
	      .append(replicationFactor)
	      .append("};");
	         
	    String query = sb.toString();
	    session.execute(query);
	}
    
    private void createTable() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
          .append(cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_KEYSPACE) 
        		  + "." 
        		  + cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_TABLE_NAME)).append("(")
          .append("id text PRIMARY KEY, ")
          .append("os text,")
          .append("version text,")
          .append("type text,")
          .append("ipAddress text,")
          .append("antivirus text,")
          .append("rotation decimal,")
          .append("current decimal,")
          .append("pressure decimal,")
          .append("flowRate decimal,")
          .append("temperature decimal,")
          .append("latitude double,")
          .append("longitude double,")
          .append("creationDate timestamp);");
        
     
        String query = sb.toString();
        
        session.execute(query);
    }
    
    private void updateAssetFromRow(Row r,Asset a) {
    	a.setId(r.getString("id"));
		a.setOs(r.getString("os"));
		a.setVersion(r.getString("version"));
		a.setType(r.getString("type"));
		a.setAntivirus(r.getString("antivirus"));
		a.setIpAddress(r.getString("ipAddress"));
		a.setCurrent(r.getDecimal("current"));
		a.setRotation(r.getDecimal("rotation"));
		a.setPressure(r.getDecimal("pressure"));
		a.setFlowRate(r.getDecimal("flowRate"));
		a.setTemperature(r.getDecimal("temperature"));
		a.setLatitude(r.getDouble("latitude"));
		a.setLongitude(r.getDouble("longitude"));
		a.setCreationDate(r.getTimestamp("creationDate"));
    }
    
    private Asset rowToAsset(Row r) {
    	Asset a = new Asset();
    	updateAssetFromRow(r,a);
		return a;
    }

	public Asset getAssetById(String assetId) throws Exception {
		StringBuilder sb = 
			      new StringBuilder("SELECT * FROM ")
			      .append(cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_KEYSPACE) 
			    		  + "." 
			    		  + cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_TABLE_NAME))
			      .append(" WHERE id='")
			      .append(assetId)
			      .append("';");
		String query = sb.toString();
		Asset a = new Asset();
		ResultSet rs = session.execute(query);
		if (rs.isFullyFetched()) {
			updateAssetFromRow(rs.one(),a);	
		}
		return a;
	}

	public List<Asset> getAllAssets() throws Exception {
		StringBuilder sb = 
			      new StringBuilder("SELECT * FROM ")
			      .append(cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_KEYSPACE) 
			    		  + "." 
			    		  + cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_TABLE_NAME))
			      .append(";");
		String query = sb.toString();
		ResultSet rs = session.execute(query);
		List<Asset> l = new ArrayList<Asset>();
		while (! rs.isExhausted()) {
			l.add(rowToAsset(rs.one()));	
		}
		return l;
	}
	
	public void persistAsset(Asset a) throws Exception {
		StringBuilder sb = new StringBuilder("INSERT INTO ")
			      .append(cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_KEYSPACE) 
			    		  + "." 
			    		  + cfg.getConfig().getProperty(ApplicationConfig.CASSANDRA_TABLE_NAME))
			      .append("(id, os, version, type, ipAddress, antivirus, rotation, current, pressure, flowRate, temperature,latitude,longitude, creationDate ) ")
			      .append("VALUES ('").append(a.getId())
			      .append("','").append(a.getOs())
			      .append("','").append(a.getVersion())
			      .append("','").append(a.getType())
			      .append("','").append(a.getIpAddress())
			      .append("','").append(a.getAntivirus())
			      .append("',").append(a.getRotation())
			      .append(",").append(a.getCurrent())
			      .append(",").append(a.getPressure())
			      .append(",").append(a.getFlowRate())
			      .append(",").append(a.getTemperature())
			      .append(",").append(a.getLatitude())
			      .append(",").append(a.getLongitude())
			      .append(",").append(a.getCreationDate())
			      .append(");");
			 
			    String query = sb.toString();
			    logger.info(query);
			    session.execute(query);
	}

}
