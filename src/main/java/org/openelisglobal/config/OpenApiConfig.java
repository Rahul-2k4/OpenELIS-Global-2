package org.openelisglobal.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.openelisglobal.config.condition.ConditionalOnProperty;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for REST API documentation.
 *
 * <p>
 * This configuration is enabled only when the property
 * 'org.openelisglobal.openapi.enabled' is set to 'true'. It is intended for
 * dev/test environments only to aid developer onboarding and frontend-backend
 * alignment.
 *
 * <p>
 * Access the Swagger UI at: /swagger-ui.html. Access the OpenAPI spec at:
 * /v3/api-docs.
 */
@Configuration
@ConditionalOnProperty(property = "org.openelisglobal.openapi.enabled", havingValue = "true", matchIfMissing = false)
public class OpenApiConfig {

    @Bean
    public OpenAPI openElisOpenAPI() {
        return new OpenAPI().info(new Info().title("OpenELIS Global REST API")
                .description("REST API documentation for OpenELIS Global Laboratory Information Management System. "
                        + "This documentation is for internal development use in dev/test environments only.")
                .version("3.x").contact(new Contact().name("OpenELIS Global Team").url("https://openelis-global.org"))
                .license(
                        new License().name("Mozilla Public License 2.0").url("https://www.mozilla.org/en-US/MPL/2.0/")))
                .servers(List.of(new Server().url("/").description("Default Server")));
    }

    @Bean
    public GroupedOpenApi patientApi() {
        return GroupedOpenApi.builder().group("patient")
                .pathsToMatch("/rest/PatientManagement/**", "/rest/patient-photos/**")
                .displayName("Patient Management API").build();
    }

    @Bean
    public GroupedOpenApi allApisGroup() {
        return GroupedOpenApi.builder().group("all").pathsToMatch("/rest/**").displayName("All REST APIs").build();
    }
}
