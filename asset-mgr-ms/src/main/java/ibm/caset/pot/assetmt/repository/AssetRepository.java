package ibm.caset.pot.assetmt.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import ibm.caset.pot.assetmt.model.Asset;

@Repository
public interface AssetRepository extends ReactiveCassandraRepository<Asset, String> {
}
