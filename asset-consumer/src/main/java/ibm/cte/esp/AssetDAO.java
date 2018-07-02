package ibm.cte.esp;

import java.util.List;

import ibm.cte.esp.model.Asset;

public interface AssetDAO {
	public Asset getAssetById(String assetId) throws Exception;
	
	public List<Asset> getAllAssets() throws Exception;
	
	public void persistAsset(Asset a) throws Exception;
}
