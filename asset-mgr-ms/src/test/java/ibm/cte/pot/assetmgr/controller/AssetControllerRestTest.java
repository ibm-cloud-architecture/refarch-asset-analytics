package ibm.cte.pot.assetmgr.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestClientException;

import ibm.cte.pot.assetmgr.model.Asset;
import ibm.cte.pot.assetmgr.repository.AssetRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssetControllerRestTest {

	@LocalServerPort
    private int port;

	@Autowired
	private WebTestClient webTestClient;
	
    
    @MockBean
    private AssetRepository assetRepository;

    

	@Test
	public void testGetAssets() throws RestClientException, MalformedURLException {
		 Flux<Asset> li =  Flux.create(sink -> {
			 sink.next(new Asset("1","Android","Sam"));
			 sink.complete();
		 });
		 
		 Mockito.doReturn(li).when(assetRepository).findAll();
		 
		 webTestClient.get().uri("/assets")
         .accept(MediaType.APPLICATION_JSON_UTF8)
         .exchange()
         .expectStatus().isOk()
         .expectBodyList(Asset.class)
         .consumeWith( assets -> assertThat(assets.getResponseBody().get(0).getOs()).contains("Android"));
        
	}
	
	@Test
	public void shouldHave3Assets() throws RestClientException, MalformedURLException {
		
		 Mono<Long> m = Mono.just(new Long(3));
		 Mockito.doReturn(m).when(assetRepository).count();
		 
		 webTestClient.get().uri("/assets/count")
         .accept(MediaType.APPLICATION_JSON_UTF8)
         .exchange()
         .expectStatus().isOk()
         .expectBody()
         .consumeWith( rep -> assertThat(rep.getResponseBody()).isNotNull());
	}
	
	@Test
	public void shouldGetAnAssetById() {
		 Mono<Asset> a =  Mono.just(new Asset("3","Android","Sam"));
			
		 Mockito.doReturn(a).when(assetRepository).findById("3");
		 
		 webTestClient.get().uri("/assets/3")
         .accept(MediaType.APPLICATION_JSON_UTF8)
         .exchange()
         .expectStatus().isOk()
         .expectBody()
         .consumeWith( rep -> {
        	 assertThat(rep.getResponseBody()).isNotNull();
        	 System.out.println(rep.toString());
         });
	}
}
