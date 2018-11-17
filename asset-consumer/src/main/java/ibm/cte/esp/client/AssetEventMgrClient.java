package ibm.cte.esp.client;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import ibm.cte.esp.ApplicationConfig;
import ibm.cte.esp.model.MetricEvent;

public class AssetEventMgrClient {
	protected RestClient restClient;
	protected ApplicationConfig config;
	protected String baseUrl = "/assetmanager";
	
	JsonSerializer<Date> ser = new JsonSerializer<Date>() {
		  @Override
		  public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext 
		             context) {
		    return src == null ? null : new JsonPrimitive(src.getTime());
		  }
		};

		JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
		  @Override
		  public Date deserialize(JsonElement json, Type typeOfT,
		       JsonDeserializationContext context) throws JsonParseException {
		    return json == null ? null : new Date(json.getAsLong());
		  }
		};

		Gson parser = new GsonBuilder()
		   .registerTypeAdapter(Date.class, ser)
		   .registerTypeAdapter(Date.class, deser).create();
	//protected static Gson parser = new GsonBuilder()
	//		   .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
	
	public AssetEventMgrClient(ApplicationConfig cfg) {
		 this.config = cfg;
		 restClient = new RestClient(this.config.getProperties().getProperty(ApplicationConfig.ASSET_MGR_HOST),
				 this.config.getProperties().getProperty(ApplicationConfig.ASSET_MGR_PORT),
				 "http");
		 
	}
	
	public MetricEvent getAssetById(String id) throws Exception {
		//List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		//nvps.add(new BasicNameValuePair("id",id));
		String resp = restClient.executeGetMethod(this.baseUrl+"/events/"+id, null);
		MetricEvent[] lae = parser.fromJson(resp, MetricEvent[].class);
		if (lae.length == 1) return lae[0];
		return null;
	}

	public void saveAsset(MetricEvent ae) throws Exception {
		String json = parser.toJson(ae);
		restClient.executePostMethod(this.baseUrl+"/events/",json);
	}
}
