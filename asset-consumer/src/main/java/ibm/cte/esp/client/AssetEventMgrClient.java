package ibm.cte.esp.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ibm.cte.esp.ApplicationConfig;
import ibm.cte.esp.model.AssetEvent;

public class AssetEventMgrClient {
	protected RestClient restClient;
	protected ApplicationConfig config;
	protected String baseUrl = "/assetmanager";
	protected static Gson parser = new GsonBuilder()
			   .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
	
	public AssetEventMgrClient(ApplicationConfig cfg) {
		 this.config = cfg;
		 restClient = new RestClient(this.config.getProperties().getProperty(ApplicationConfig.ASSET_MGR_HOST),
				 this.config.getProperties().getProperty(ApplicationConfig.ASSET_MGR_PORT),
				 "http");
		 
	}
	
	public AssetEvent getAssetById(String id) throws Exception {
		//List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		//nvps.add(new BasicNameValuePair("id",id));
		String resp = restClient.executeGetMethod(this.baseUrl+"/assets/"+id, null);
		AssetEvent[] lae = parser.fromJson(resp, AssetEvent[].class);
		if (lae.length == 1) return lae[0];
		return null;
	}

	public void saveAsset(AssetEvent ae) throws Exception {
		String json = parser.toJson(ae);
		restClient.executePostMethod(this.baseUrl+"/assets/",json);
	}
}
