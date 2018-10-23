package ibm.cte.esp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ibm.cte.esp.web.AssetController;

@RunWith(SpringRunner.class)
@WebMvcTest
public class AssetControllerTest {

	@Autowired
	private AssetController controller;
	
	@Autowired
    private MockMvc mockMvc;
    
    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
    

    @Test
    public void shouldReturnSomeAssets() throws Exception {
        this.mockMvc.perform(get("/assets")).andDo(print()).andExpect(status().isOk());
    }

}
