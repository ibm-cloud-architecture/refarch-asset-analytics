package ibm.caset.pot.assetmt;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

@Configuration
public class AppConfig extends AbstractCassandraConfiguration {

	  /*
	   * Provide a contact point to the configuration.
	   */
	  public String getContactPoints() {
	    return "localhost,10.1.0.30";
	  }

	  /*
	   * Provide a keyspace name to the configuration.
	   */
	  public String getKeyspaceName() {
	    return "assetmonitoring";
	  }

}
