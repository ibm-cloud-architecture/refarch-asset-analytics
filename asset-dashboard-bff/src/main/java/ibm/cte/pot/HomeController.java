package ibm.cte.pot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	
	@RequestMapping("/health")
    public @ResponseBody String greeting() {
        return "Hello Asset";
    }
	
}
