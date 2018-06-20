package ibm.caset.pot.assetmt.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ibm.caset.pot.assetmt.model.Asset;
import ibm.caset.pot.assetmt.repository.AssetRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AssetController {
	
	private AssetRepository assetMgrRepository;
	

	public AssetController(AssetRepository rep) {
		this.assetMgrRepository = rep;
	}
	
	@RequestMapping("/")
    public @ResponseBody String index() {
        return "Greetings from Asset Manager v0.0.1 !";
    }
	
	@RequestMapping("/health")
    public @ResponseBody String health() {
        return "Pong";
    }
	
	
	@RequestMapping("/assets")
    public Flux<Asset> getAllAssets() {
        return this.assetMgrRepository.findAll();
    }
	
    @GetMapping("/assets/kpi/count")
    public Mono<Long> countAssets() {
        return this.assetMgrRepository.count();
    }

    @PostMapping("/assets")
    public Mono<Asset> createAsset(@Valid @RequestBody Asset asset) {
        return this.assetMgrRepository.save(asset);
    }

    @GetMapping("/assets/{id}")
    public Mono<ResponseEntity<Asset>> getAssetById(@PathVariable(value = "id") String assetId) {
        return this.assetMgrRepository.findById(assetId)
                .map(savedAsset -> ResponseEntity.ok(savedAsset))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/assets/{id}")
    public Mono<ResponseEntity<Asset>> updateAsset(@PathVariable(value = "id") String assetId,
                                                   @Valid @RequestBody Asset asset) {
        return this.assetMgrRepository.findById(assetId)
                .flatMap(existingAsset -> {
                    existingAsset.setType(asset.getType());
                    existingAsset.setOs(asset.getOs());
                    existingAsset.setAntivirus(asset.getAntivirus());
                    existingAsset.setIpAddress(asset.getIpAddress());
                    return assetMgrRepository.save(existingAsset);
                })
                .map(updatedAsset -> new ResponseEntity<>(updatedAsset, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/assets/{id}")
    public Mono<ResponseEntity<Void>> deleteAsset(@PathVariable(value = "id") String assetId) {

        return this.assetMgrRepository.findById(assetId)
                .flatMap(existingAsset ->
                        assetMgrRepository.delete(existingAsset)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Assets are Sent to the client as Server Sent Events
    @GetMapping(value = "/stream/assetcount", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Long> streamAssetsCount() {
        return this.assetMgrRepository.count();
    }

    // Assets are Sent to the client as Server Sent Events
    @GetMapping(value = "/stream/assets", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Asset> streamAllAssets() {
        return this.assetMgrRepository.findAll();
    }
    
}
