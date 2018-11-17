package ibm.cte.esp.dao;

import java.util.List;

import ibm.cte.esp.model.MetricEvent;

public interface AssetDAO {
	public MetricEvent getAssetById(String assetId) throws Exception;
	
	public List<MetricEvent> getAllAssets() throws Exception;
	
	public void persistMetricEvent(MetricEvent a) throws Exception;

}
