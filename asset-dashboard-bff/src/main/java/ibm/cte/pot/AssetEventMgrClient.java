package ibm.cte.pot;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ibm.cte.esp.model.Asset;

public class AssetEventMgrClient {
	RestClient restClient;
	protected static Gson parser = new GsonBuilder()
			   .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
	
	public AssetEventMgrClient() {
		 restClient = new RestClient("http","assetmgr.greencompute.ibmcase.com");
	}
	
	public Asset getAssetById(String id) throws Exception {
		//List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		//nvps.add(new BasicNameValuePair("id",id));
		String resp = restClient.executeGetMethod("/assetmanager/assets/"+id, null);
		Asset[] lae = parser.fromJson(resp, Asset[].class);
		if (lae.length == 1) return lae[0];
		return null;
	}

	public List<Asset> getAllAssets() throws Exception {
		String resp = restClient.executeGetMethod("/assetmanager/assets", null);
		Asset[] lae = parser.fromJson(resp, Asset[].class);
		return Arrays.asList(lae);
	}

	public void saveAsset(Asset ae) throws Exception {
		String json = parser.toJson(ae);
		restClient.executePostMethod("/assetmanager/assets",json);
	}
}
