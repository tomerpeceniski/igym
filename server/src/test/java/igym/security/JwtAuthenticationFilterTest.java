package igym.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import io.jsonwebtoken.MalformedJwtException;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UUID testUserId = UUID.randomUUID();
    private final String validToken = "valid.jwt.token";
    private final UserDetails testUserDetails;

    JwtAuthenticationFilterTest() {
        testUserDetails = new User(
            testUserId.toString(),
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should authenticate user when valid token is passed")
    void doFilterInternalSuccess() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtil.getUserIdFromToken(validToken)).thenReturn(testUserId);
        when(userDetailsService.loadUserByUsername(testUserId.toString())).thenReturn(testUserDetails);
        when(jwtUtil.validateToken(validToken)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(testUserId.toString(), SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    @DisplayName("Should not authenticate user when Malformed token is passed")
    void doFilterInternalInvalidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(jwtUtil.getUserIdFromToken(anyString())).thenThrow(new MalformedJwtException("Invalid token format"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Should not authenticate user when no token is passed")
    void doFilterInternalNoToken() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Should not authenticate user when header in invalid format is passed")
    void doFilterInternalInvalidFormat() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidFormat " + validToken);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Should not authenticate user when expired token is passed")
    void doFilterInternalExpiredToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer expired.token");
        when(jwtUtil.getUserIdFromToken(any(String.class))).thenReturn(testUserId);
        when(userDetailsService.loadUserByUsername(testUserId.toString())).thenReturn(testUserDetails);
        when(jwtUtil.validateToken(validToken)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Should not authenticate user when invalid user is passed in token")
    void doFilterInternalInvalidUser() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtil.getUserIdFromToken(validToken)).thenReturn(testUserId);
        when(userDetailsService.loadUserByUsername(testUserId.toString())).thenThrow(new UsernameNotFoundException("User not found"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
} 