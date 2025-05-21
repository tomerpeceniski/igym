package igym.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.TestPropertySource;
import igym.controllers.UserController;
import igym.entities.User;
import igym.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtil.class})
@TestPropertySource(properties = {
    "jwt.secret=testSecretKey1234567890123456789012345678901234567890",
    "jwt.expiration=3600"
})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private UserService userService;

    @SuppressWarnings("removal")
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("Should allow login endpoint request without token")
    void loginEndpointTest() throws Exception {
        mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"test\",\"password\":\"ValidPassword\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should allow register endpoint request without token")
    void registerEndpointTest() throws Exception {
        when(userService.createUser(any())).thenReturn(new User());

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"test\",\"password\":\"ValidPassword\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return unauthorized for protected endpoint without token")
    void protectedEndpointNoToken() throws Exception {
        mockMvc.perform(get("/api/v1/protected-endpoint"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("Should return unauthorized for protected endpoint with invalid token")
    void protectedEndpointInvalidToken() throws Exception {
        mockMvc.perform(get("/api/v1/protected-endpoint")
                .header("Authorization", "Bearer invalid.token"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").exists());
    }
} 