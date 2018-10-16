package ibm.cte.esp.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ibm.cte.esp.model.AssetEvent;

public class AssetEventMgrClient {
	RestClient restClient;
	protected static Gson parser = new GsonBuilder()
			   .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
	
	public AssetEventMgrClient() {
		 restClient = new RestClient("http","assetmgr.greencompute.ibmcase.com");
	}
	
	public AssetEvent getAssetById(String id) throws Exception {
		//List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		//nvps.add(new BasicNameValuePair("id",id));
		String resp = restClient.executeGetMethod("/assetmanager/assets/"+id, null);
		AssetEvent[] lae = parser.fromJson(resp, AssetEvent[].class);
		if (lae.length == 1) return lae[0];
		return null;
	}

	public List<AssetEvent> getAllAssets() throws Exception {
		String resp = restClient.executeGetMethod("/assetmanager/assets", null);
		AssetEvent[] lae = parser.fromJson(resp, AssetEvent[].class);
		return Arrays.asList(lae);
	}

	public void saveAsset(AssetEvent ae) throws Exception {
		String json = parser.toJson(ae);
		restClient.executePostMethod("/assetmanager/assets",json);
	}
}
