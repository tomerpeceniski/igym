package igym.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import igym.entities.User;
import igym.exceptions.UserNotFoundException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Service class responsible for loading user-specific data for Spring Security.
 * Implements UserDetailsService to provide user authentication information.
 *
 * <p>
 * This service is used by Spring Security to load user details during authentication.
 * It retrieves user information from the database and converts it into Spring Security's
 * UserDetails format.
 * </p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Loads a user by their ID and converts it to Spring Security's UserDetails format.
     *
     * @param id the UUID of the user as a string
     * @return UserDetails object containing the user's authentication information
     * @throws UsernameNotFoundException if the user is not found or is inactive
     */
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        try {
            User user = userService.findById(UUID.fromString(id));
            return new org.springframework.security.core.userdetails.User(
                    user.getName(), user.getPassword(), new ArrayList<>());
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
} 