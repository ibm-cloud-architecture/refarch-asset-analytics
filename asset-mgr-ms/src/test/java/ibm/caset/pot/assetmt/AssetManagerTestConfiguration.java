package ibm.caset.pot.assetmt;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import ibm.caset.pot.assetmt.repository.AssetRepository;

@Profile("test")
@Configuration
public class AssetManagerTestConfiguration {
	@Bean
    @Primary
    public AssetRepository nameService() {
        return Mockito.mock(AssetRepository.class);
    }
}
