package igym.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC configuration class that registers custom interceptors.
 *
 * <p>
 * This configuration registers the {@link RequestLoggingInterceptor} to
 * intercept all incoming HTTP requests,
 * enabling logging of request and response details for the application.
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestLoggingInterceptor requestLoggingInterceptor;

    public WebConfig(RequestLoggingInterceptor requestLoggingInterceptor) {
        this.requestLoggingInterceptor = requestLoggingInterceptor;
    }

    /**
     * Registers the {@link RequestLoggingInterceptor} to intercept all HTTP
     * requests.
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor);
    }
}
