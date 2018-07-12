package ibm.cte.pot.assetmgr.repository;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import ibm.cte.pot.assetmgr.model.Asset;
import reactor.core.publisher.Flux;

@Repository
public interface AssetRepository extends ReactiveCassandraRepository<Asset, String> {
	
	@Query(value="SELECT * FROM assetmonitoring.assets where os = ?1")
	public Flux<Asset> getAssetsByOS(String os);
}
