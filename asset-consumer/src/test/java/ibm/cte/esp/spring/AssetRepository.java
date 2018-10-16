package ibm.cte.esp.spring;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import ibm.cte.esp.model.AssetEvent;

@Repository
public interface AssetRepository extends CassandraRepository<AssetEvent, String> {
	
}
