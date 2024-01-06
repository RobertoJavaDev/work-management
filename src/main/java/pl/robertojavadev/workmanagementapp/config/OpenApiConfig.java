package pl.robertojavadev.workmanagementapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI githubRepositoryOpenApi() {

        return new OpenAPI()
                .info(new Info().title("REST API for 'Work management application'")
                        .description("Work Management is a web application for managing work on projects. " +
                                "The application allows users to create projects, add tasks, assign them " +
                                "to users, and track progress on the project."));
    }
}
