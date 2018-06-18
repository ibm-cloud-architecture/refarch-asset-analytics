package ibm.caset.pot.reactive.assetmgr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

@Configuration
@EnableReactiveCassandraRepositories
public class CassandraConfig extends AbstractReactiveCassandraConfiguration {

/*    @Value("${cassandra.contactpoints}")
    private String contactPoints;

    @Value("${cassandra.port}")
    private int port;

    @Value("${cassandra.keyspace}")
    private String keySpace;

    @Value("${cassandra.basepackages}")
    private String basePackages;
*/
    @Override
    protected String getKeyspaceName() {
        return "assetmonitoring";
    }

    @Override
    protected String getContactPoints() {
        return "localhost,10.1.0.30";
    }

   @Override
    protected int getPort() {
        return 9042;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

/*
    @Override
    public String[] getEntityBasePackages() {
        return new String[] {basePackages};
    }
*/
}