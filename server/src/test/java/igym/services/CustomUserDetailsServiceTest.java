package igym.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import igym.entities.User;
import igym.exceptions.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    
    @Mock
    private UserService userService;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private UUID userId;
    private User user1;

    @BeforeEach
    void config() {
        userId = UUID.randomUUID();
        user1 = new User("Maria Clown");
        user1.setPassword("encodedPassword");
    }

    @Test
    @DisplayName("Should return the UserDetails representing the user found by id")
    void successFindByIdTest() {
        when(userService.findById(userId)).thenReturn(user1);

        UserDetails details = customUserDetailsService.loadUserByUsername(userId.toString());
        assertEquals(details.getUsername(), user1.getName());
        assertEquals(details.getPassword(), user1.getPassword());        
    }

    @Test
    @DisplayName("Should thrown exception when there is no active user with provided id")
    void notFoundTest() {
        when(userService.findById(userId)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(userId.toString()));
        verify(userService, times(1)).findById(userId);
    }
}
