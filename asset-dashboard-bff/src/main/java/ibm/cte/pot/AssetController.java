package ibm.cte.pot;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ibm.cte.esp.model.Asset;

@RestController()
@RequestMapping(value="/assets")
public class AssetController {
	
	@RequestMapping("") 
	public @ResponseBody List<Asset> getAssets() {
		Asset a = new Asset();
		a.setId("Asset_01");		
		return Arrays.asList(a);
	}
	
	
	@RequestMapping("/assetstream")
    public void webSocket() {
    	// delegate to a kafka consumer to get new asset
		// push new asset to UI / session web socket session.
    }
}
