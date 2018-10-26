package ibm.cte.esp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="app")
public class BffProperties {
	private String assetsTopic;
	private AssetManager assetManager;
	
	
	public static class AssetManager {
		private String host;
		private String port;
		private String protocol;
		private String baseUrl;
		
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}
		public String getProtocol() {
			return protocol;
		}
		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}
		public String getBaseUrl() {
			return baseUrl;
		}
		public void setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
		}
		
	}


	public String getAssetsTopic() {
		return assetsTopic;
	}


	public void setAssetsTopic(String assetsTopic) {
		this.assetsTopic = assetsTopic;
	}


	public AssetManager getAssetManager() {
		return assetManager;
	}


	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
}
