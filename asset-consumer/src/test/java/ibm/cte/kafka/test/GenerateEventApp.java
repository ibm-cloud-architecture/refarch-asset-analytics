package ibm.cte.kafka.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GenerateEventApp   {
	
	public static void main(String[] args) {
		SpringApplication.run(GenerateEventApp.class, args).close();
	}

}
