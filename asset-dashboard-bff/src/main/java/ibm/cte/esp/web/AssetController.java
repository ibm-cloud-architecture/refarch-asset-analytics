package ibm.cte.esp.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ibm.cte.esp.domain.Asset;
import ibm.cte.esp.services.AssetEventMgrClient;

@RestController()
@RequestMapping(value="/assets")
public class AssetController {
	
	public  AssetEventMgrClient restClient;
	
	public AssetController(AssetEventMgrClient c ) {
		this.restClient = c;
	}
	
	@RequestMapping("") 
	public @ResponseBody List<Asset> getAssets() {
		try {
			return restClient.getAllAssets();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Asset>();
		}
	}
	
}
