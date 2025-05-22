package igym.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecret = "testSecretKey1234567890123456789012345678901234567890";
    private final long testExpiration = 3600L;
    private final UUID testUserId = UUID.randomUUID();
    private final String testUsername = "testuser";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationInSec", testExpiration);
    }

    @Test
    @DisplayName("Should generate a token successfully")
    void generateTokenTest() {
        String token = jwtUtil.generateToken(testUserId, testUsername);
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("Should return true when validating a valid token")
    void validateTokenTest() {
        String token = jwtUtil.generateToken(testUserId, testUsername);
        boolean isValid = jwtUtil.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false when validating an invalid token")
    void validateInvalidTokenTest() {
        String invalidToken = "invalid.token";
        boolean isValid = jwtUtil.validateToken(invalidToken);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return the correct user name from the token")
    void getUsernameFromTokenTest() {
        String token = jwtUtil.generateToken(testUserId, testUsername);
        String username = jwtUtil.getUsernameFromToken(token);
        assertEquals(testUsername, username);
    }

    @Test
    @DisplayName("Should return the correct id from the token")
    void getUserIdFromTokenTest() {
        String token = jwtUtil.generateToken(testUserId, testUsername);
        UUID userId = jwtUtil.getUserIdFromToken(token);
        assertEquals(testUserId, userId);
    }

    @Test
    @DisplayName("Should return the correct claims from the token")
    void extractAllClaimsTest() {
        String token = jwtUtil.generateToken(testUserId, testUsername);
        Claims claims = jwtUtil.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals(testUserId.toString(), claims.getSubject());
        assertEquals(testUsername, claims.get("username", String.class));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }
} 