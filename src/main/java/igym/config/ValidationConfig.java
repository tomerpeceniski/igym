package igym.config;

import jakarta.validation.Validator;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {

    @Bean
    static public Validator validator() {
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .addProperty("hibernate.validator.fail_fast", "false")  
                .buildValidatorFactory();
        return factory.getValidator();
    }
}