package qa.qdb.dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

@EnableJpaAuditing
@EnableAutoConfiguration
@EntityScan(basePackages = "qa.qdb.dms.model")
@EnableFeignClients
@SpringBootApplication
public class DocumentManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentManagementSystemApplication.class, args);
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
