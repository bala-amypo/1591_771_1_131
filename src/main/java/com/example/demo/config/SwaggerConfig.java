package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Server publicServer = new Server();
        publicServer.setUrl("https://9310.408procr.amypo.ai");
        publicServer.setDescription("Public Cloud Server");

        return new OpenAPI()
                .servers(List.of(publicServer))
