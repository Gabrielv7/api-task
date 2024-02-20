package com.gabriel.apitask.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API REST para gerenciamento de tarefas",
                description = "API responsavél por gerenciar tarefas com suas respectivas categorias",
                version = "1.0.0",
                contact = @Contact(
                        email = "gabrielcosta.v7@gmail.com",
                        name = "Gabriel Costa Afonso"
                )
        )
)
public class OpenApiConfig {

}
