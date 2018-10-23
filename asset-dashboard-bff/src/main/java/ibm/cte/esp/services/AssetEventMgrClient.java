package ibm.cte.esp.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ibm.cte.esp.config.BffProperties;
import ibm.cte.esp.domain.Asset;

@Service
public class AssetEventMgrClient {
	private RestClient restClient;
	private BffProperties config;
	private String baseUrl = "/assetmanager";
	
	protected static Gson parser = new GsonBuilder()
			   .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
	

	public AssetEventMgrClient(BffProperties cfg) {
		 this.config = cfg;
		 restClient = new RestClient(this.config.getAssetManager().getProtocol(),
				 this.config.getAssetManager().getHost(),
				 this.config.getAssetManager().getPort());
		 this.baseUrl = this.config.getAssetManager().getBaseUrl();
	}
	
	public Asset getAssetById(String id) throws Exception {
		//List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		//nvps.add(new BasicNameValuePair("id",id));
		String resp = restClient.executeGetMethod(this.baseUrl+"/assets/"+id, null);
		Asset[] lae = parser.fromJson(resp, Asset[].class);
		if (lae.length == 1) return lae[0];
		return null;
	}

	public List<Asset> getAllAssets() throws Exception {
		String resp = restClient.executeGetMethod(this.baseUrl+"/assets/", null);
		Asset[] lae = parser.fromJson(resp, Asset[].class);
		return Arrays.asList(lae);
	}

	public void saveAsset(Asset ae) throws Exception {
		String json = parser.toJson(ae);
		restClient.executePostMethod(this.baseUrl+"/assets/",json);
	}
}
