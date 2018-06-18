package ibm.caset.pot.assetmt;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestClientException;

import ibm.caset.pot.assetmt.model.Asset;
import ibm.caset.pot.assetmt.repository.AssetRepository;
import reactor.core.publisher.Flux;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestAssetResource {

	@LocalServerPort
    private int port;

    private URL base;

	@Autowired
	private WebTestClient webTestClient;
    
    @Autowired 
    private AssetRepository assetRepository;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/health");
    }
    
	@Test
	public void testHello() {
		ResponseEntity<String> response = webTestClient.get().uri("/health");
        Assert.assertTrue(response.getBody().startsWith("Greetings from Asset Manager"));
	}

	@Test
	public void testGetAssets() throws RestClientException, MalformedURLException {
		 Flux<Asset> li = new ArrayList<Asset>();
		 li.add(new Asset("1","Android","Sam"));
		 Mockito.when(assetRepository.findAll()).thenReturn(li);
		 List<Asset> l = assetRepository.getAssets();
		 Assert.assertNotNull(l);
		 Assert.assertTrue(l.size() == 1);
		 
		 ResponseEntity<String> response = template.getForEntity(new URL("http://localhost:" + port + "/assets").toString(),String.class);
		 Assert.assertTrue(response.getBody().startsWith("[{\"id\":1,\"os\":\"Android"));
	}
}
