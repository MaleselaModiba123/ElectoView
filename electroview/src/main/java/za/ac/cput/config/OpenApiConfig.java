package za.ac.cput.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI electroViewOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ElectroView API")
                        .description("Electricity Usage Analytics Dashboard — "
                                + "REST API for monitoring electricity consumption, "
                                + "detecting anomalies, and generating reports.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Malesela Modiba")
                                .email("malesela@cput.ac.za"))
                        .license(new License()
                                .name("CPUT — SEG 580S")
                                .url("https://www.cput.ac.za")));
    }
}