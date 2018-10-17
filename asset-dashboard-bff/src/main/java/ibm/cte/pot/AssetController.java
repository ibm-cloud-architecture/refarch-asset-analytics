package ibm.cte.pot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ibm.cte.esp.model.Asset;

@RestController()
@RequestMapping(value="/assets")
public class AssetController {
	public static AssetEventMgrClient restClient = new AssetEventMgrClient();;
	
	public AssetController() {
		
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
