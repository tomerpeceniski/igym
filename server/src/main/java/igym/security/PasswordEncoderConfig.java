package igym.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for password encoding.
 * Provides a BCrypt password encoder bean for secure password hashing.
 *
 * <p>
 * This configuration ensures that passwords are securely hashed using BCrypt
 * before being stored in the database, and provides the necessary encoder
 * for password verification during authentication.
 * </p>
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Creates a BCrypt password encoder bean.
     *
     * @return a PasswordEncoder instance using BCrypt hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
