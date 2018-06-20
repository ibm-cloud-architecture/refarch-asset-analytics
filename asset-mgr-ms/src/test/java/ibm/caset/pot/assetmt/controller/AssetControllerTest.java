package ibm.caset.pot.assetmt.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ibm.caset.pot.assetmt.repository.AssetRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AssetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private AssetController controller;
    
    @MockBean
    private AssetRepository assetRepository;
    
    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
    
	@Test
	public void shouldReturnGreetingMessage() throws Exception {
		this.mockMvc.perform(get("/"))
		.andDo(print())
		.andExpect(status().isOk())
        .andExpect(content().string(containsString("Greetings from Asset")));
		
	}
	
	@Test
	public void shouldReturnPongMessage() throws Exception {
		this.mockMvc.perform(get("/health"))
		.andDo(print())
		.andExpect(status().isOk())
        .andExpect(content().string(containsString("Pong")));
		
	}
}
