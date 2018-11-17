package ibm.cte.esp.dao;



import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import ibm.cte.esp.ApplicationConfig;
import ibm.cte.esp.model.MetricEvent;

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
		String ep = cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_ENDPOINTS);
		if (ep.contains(",")) {
			b = Cluster.builder().addContactPoints(ep.split(","));
		} else {
			b = Cluster.builder().addContactPoint(ep);
		}
		
		
		port = new Integer(cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_PORT));
		if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();
        session = cluster.connect();
        createKeyspace(cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_KEYSPACE),
        		cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_STRATEGY),
        		Integer.parseInt(cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_REPLICAS)));
        createTable(cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_KEYSPACE),
        		cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_TABLE_NAME));
	}
	
   
    public CassandraRepo(Session inSession,String keySpace,String tableName) {
		CassandraRepo.session = inSession;
		createKeyspace(keySpace, "SimpleStrategy", 1);
		createTable(keySpace,tableName);
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
    
    private void createTable(String keySpace,String tableName) {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
          .append(keySpace
        		  + "." 
        		  + tableName).append("(")
          .append("id text PRIMARY KEY, ")
          .append("rotation in,")
          .append("current double,")
          .append("pressure int,")
          .append("flowRate int,")
          .append("temperature int,")
          .append("timecreated timestamp);");
        
        String query = sb.toString();
        
        session.execute(query);
    }
    
    private void updateAssetFromRow(Row r,MetricEvent a) {
    	a.setId(r.getString("id"));
		a.setCurrent(r.getDouble("current"));
		a.setRotation(r.getInt("rotation"));
		a.setPressure(r.getInt("pressure"));
		a.setFlowRate(r.getInt("flowRate"));
		a.setTemperature(r.getInt("temperature"));
		a.setTimeStamp(r.getTimestamp("timecreated"));
    }
    
    private MetricEvent rowToAsset(Row row) {
		MetricEvent a = new MetricEvent();
		updateAssetFromRow(row,a);
		return a;
    }

	public MetricEvent getAssetById(String assetId) throws Exception {
		StringBuilder sb = 
			      new StringBuilder("SELECT * FROM ")
			      .append(cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_KEYSPACE) 
			    		  + "." 
			    		  + cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_TABLE_NAME))
			      .append(" WHERE id='")
			      .append(assetId)
			      .append("';");
		String query = sb.toString();
		MetricEvent a = new MetricEvent();
		ResultSet rs = session.execute(query);
		if (rs.isFullyFetched()) {
			updateAssetFromRow(rs.one(),a);	
		}
		return a;
	}

	public List<MetricEvent> getAllAssets() throws Exception {
		StringBuilder sb = 
			      new StringBuilder("SELECT * FROM ")
			      .append(cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_KEYSPACE) 
			    		  + "." 
			    		  + cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_TABLE_NAME))
			      .append(";");
		String query = sb.toString();
		ResultSet rs = session.execute(query);
		List<MetricEvent> l = new ArrayList<MetricEvent>();
		while (! rs.isExhausted()) {
			l.add(rowToAsset(rs.one()));	
		}
		return l;
	}
	
	public void persistMetricEvent(MetricEvent a) throws Exception {
		StringBuilder sb = new StringBuilder("INSERT INTO ")
			      .append(cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_KEYSPACE) 
			    		  + "." 
			    		  + cfg.getProperties().getProperty(ApplicationConfig.CASSANDRA_TABLE_NAME))
			      .append("(id, rotation, current, pressure, flowRate, temperature, timeStamp ) ")
			      .append("VALUES ('").append(a.getId())
			      .append("',").append(a.getRotation())
			      .append(",").append(a.getCurrent())
			      .append(",").append(a.getPressure())
			      .append(",").append(a.getFlowRate())
			      .append(",").append(a.getTemperature())
			      .append(",").append(a.getTimeStamp())
			      .append(");");
			 
			    String query = sb.toString();
			    logger.info(query);
			    session.execute(query);
	}

}
