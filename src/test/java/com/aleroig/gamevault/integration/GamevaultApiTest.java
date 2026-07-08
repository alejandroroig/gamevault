package com.aleroig.gamevault.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.rabbitmq.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class GamevaultApiTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

    @Container
    @ServiceConnection
    static MongoDBContainer mongodb = new MongoDBContainer("mongo:7");

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitmq = new RabbitMQContainer("rabbitmq:4-management");

    @SuppressWarnings("resource")
    @Container
    @ServiceConnection(name = "redis")
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @Autowired
    private MockMvc mockMvc;

    @Test
    void flujoPrincipal_DebeFuncionarConServiciosReales() throws Exception {
        mockMvc.perform(get("/api/v1/videojuegos"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hollow Knight")));

        String userToken = login("user", "user123");

        mockMvc.perform(post("/api/v1/videojuegos/1/reviews")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "puntuacion": 8,
                                  "comentario": "Review creada desde un test con Testcontainers."
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.autor").value("user"))
                .andExpect(jsonPath("$.videojuegoId").value(1))
                .andExpect(jsonPath("$.puntuacion").value(8));

        mockMvc.perform(get("/api/v1/actividad")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        String adminToken = login("admin", "admin123");

        mockMvc.perform(get("/api/v1/actividad")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    private String login(String username, String password) throws Exception {
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "%s",
                                  "password": "%s"
                                }
                                """.formatted(username, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return extraerAccessToken(response);
    }

    private String extraerAccessToken(String json) {
        String marcador = "\"accessToken\":\"";
        int inicio = json.indexOf(marcador);

        if (inicio == -1) {
            throw new IllegalStateException("No se ha encontrado accessToken en la respuesta: " + json);
        }

        int tokenInicio = inicio + marcador.length();
        int tokenFin = json.indexOf("\"", tokenInicio);

        if (tokenFin == -1) {
            throw new IllegalStateException("Token JWT mal formado en la respuesta: " + json);
        }

        return json.substring(tokenInicio, tokenFin);
    }
}