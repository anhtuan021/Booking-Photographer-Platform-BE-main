package org.bookingplatform.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfig {

    @Value("${app.swagger.enabled:true}")
    private boolean swaggerEnabled;

    @Value("${app.swagger.title:Booking Platform API}")
    private String title;

    @Value("${app.swagger.description:API documentation for Booking Platform - Professional Photography Booking System}")
    private String description;

    @Value("${app.swagger.version:1.0.0}")
    private String version;

    @Value("${app.swagger.contact.name:Anh Tuan}")
    private String contactName;

    @Value("${app.swagger.contact.url:https://github.com/anhtuan021}")
    private String contactUrl;

    @Value("${app.swagger.server.url:http://localhost:5000}")
    private String serverUrl;

    @Value("${app.swagger.server.description:Development Server}")
    private String serverDescription;

    @Bean
    public OpenAPI customOpenAPI() {
        if (!swaggerEnabled) {
            return new OpenAPI();
        }

        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server()
                                .url(serverUrl)
                                .description(serverDescription),
                        new Server()
                                .url("https://api.bookingplatform.com")
                                .description("Production Server"),
                        new Server()
                                .url("https://staging-api.bookingplatform.com")
                                .description("Staging Server")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Authorization header using the Bearer scheme. Example: \"Authorization: Bearer {token}\""))
                        .addSecuritySchemes("apiKey", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-API-Key")
                                .description("API Key for external services")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .tags(List.of(
                        new Tag()
                                .name("Photographer Profile")
                                .description("APIs for managing photographer profiles"),
                        new Tag()
                                .name("Service Packages")
                                .description("APIs for managing service packages"),
                        new Tag()
                                .name("Portfolio Management")
                                .description("APIs for managing portfolio images"),
                        new Tag()
                                .name("Booking Management")
                                .description("APIs for managing bookings"),
                        new Tag()
                                .name("Availability Management")
                                .description("APIs for managing photographer availability"),
                        new Tag()
                                .name("Reviews & Ratings")
                                .description("APIs for managing reviews and ratings"),
                        new Tag()
                                .name("Notifications")
                                .description("APIs for managing notifications"),
                        new Tag()
                                .name("Dashboard & Analytics")
                                .description("APIs for dashboard and analytics"),
                        new Tag()
                                .name("Search & Discovery")
                                .description("APIs for searching and discovering photographers")
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title(title)
                .description(description)
                .version(version)
                .contact(new Contact()
                        .name(contactName)
                        .url(contactUrl))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"))
                .termsOfService("https://bookingplatform.com/terms");
    }
}