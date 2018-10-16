package ibm.cte.esp.dao;

import java.util.List;

import ibm.cte.esp.model.AssetEvent;

public interface AssetDAO {
	public AssetEvent getAssetById(String assetId) throws Exception;
	
	public List<AssetEvent> getAllAssets() throws Exception;
	
	public void persistAsset(AssetEvent a) throws Exception;
}
