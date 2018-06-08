package ibm.caset.pot.assetmt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibm.caset.pot.assetmt.model.Asset;

@RestController
public class AssetResource {
	@Autowired
	private AssetRepository repository;
	
	@RequestMapping("/")
    public String index() {
        return "Greetings from Asset Manager v0.0.1 !";
    }
	
	@RequestMapping("/assets")
    public List<Asset> getAssets() {
        return repository.getAssets();
    }
}
