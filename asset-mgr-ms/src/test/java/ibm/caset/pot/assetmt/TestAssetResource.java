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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;

import ibm.caset.pot.assetmt.model.Asset;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestAssetResource {

	@LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;
    
    @Autowired 
    private AssetRepository assetRepository;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/health");
    }
    
	@Test
	public void testHello() {
		ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        Assert.assertTrue(response.getBody().startsWith("Greetings from Asset Manager"));
	}

	@Test
	public void testGetAssets() throws RestClientException, MalformedURLException {
		 List<Asset> li = new ArrayList<Asset>();
		 li.add(new Asset(1,"Android","Sam"));
		 Mockito.when(assetRepository.getAssets()).thenReturn(li);
		 List<Asset> l = assetRepository.getAssets();
		 Assert.assertNotNull(l);
		 Assert.assertTrue(l.size() == 1);
		 
		 ResponseEntity<String> response = template.getForEntity(new URL("http://localhost:" + port + "/assets").toString(),String.class);
		 Assert.assertTrue(response.getBody().startsWith("[{\"id\":1,\"os\":\"Android"));
	}
}
