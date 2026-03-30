package com.example.simposium.auth.config;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldServeFrontendAtRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/login-demo/index.html"));
    }

    @Test
    void shouldServeFrontendAtLoginDemoPath() throws Exception {
        mockMvc.perform(get("/login-demo/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/login-demo/index.html"));
    }

    @Test
    void shouldReturnUnauthorizedForProtectedRouteWithoutToken() throws Exception {
        mockMvc.perform(get("/api/private/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldLoginSuccessfullyWithDemoCredentials() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"demo@simposium.com\",\"password\":\"demo123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.email").value("demo@simposium.com"));
    }

    @Test
    void shouldReturnUnauthorizedWithIncorrectCredentials() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"demo@simposium.com\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value("AUTH_INVALID_CREDENTIALS"))
                .andExpect(jsonPath("$.path").value("/auth/login"));
    }

    @Test
    void shouldReturnBadRequestWhenLoginPayloadIsInvalid() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"correo-invalido\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("AUTH_VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.email").value("El email no es valido"))
                .andExpect(jsonPath("$.details.password").value("La password es obligatoria"));
    }

    @Test
    void shouldAccessPrivateEndpointWithValidBearerToken() throws Exception {
        String loginResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"demo@simposium.com\",\"password\":\"demo123\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String accessToken = JsonPath.read(loginResponse, "$.accessToken");

        mockMvc.perform(get("/api/private/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("demo@simposium.com"));
    }
}

