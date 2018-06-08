package ibm.caset.pot.assetmt;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import ibm.caset.pot.assetmt.model.Asset;

public interface AssetRepository extends CassandraRepository<Asset, Long> {
	@Query(value="SELECT * FROM assetmonitoring.assets")
	public List<Asset> getAssets();

	@Query(value="SELECT * FROM assetmonitoring.assets where ")
	public List<Asset> getAssetsByOS(String os);
}
