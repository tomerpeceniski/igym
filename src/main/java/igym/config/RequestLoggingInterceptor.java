package igym.config;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Intercepts incoming HTTP requests and outgoing responses to log important
 * request information.
 *
 * <p>
 * This interceptor logs the HTTP method, request path, response status, and
 * duration of each request.
 * It also generates a unique {@code requestId} for each request, stored in the
 * Mapped Diagnostic Context (MDC),
 * allowing logs to be correlated for better traceability.
 * </p>
 *
 * <p>
 * This class is automatically detected as a Spring component and registered via
 * {@link WebConfig}.
 * </p>
 */
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    /**
     * Logs the incoming HTTP request and sets the request start time and a unique
     * request ID in the MDC.
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler) {
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        logger.info("Received {} {}", request.getMethod(), pattern);
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    /**
     * Logs the outgoing HTTP response with status and duration, and clears the MDC.
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler, @Nullable Exception ex) {
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        long duration = System.currentTimeMillis() - (Long) request.getAttribute("startTime");
        logger.info("Responding {} {} - status [{}] - {} ms",
                request.getMethod(), pattern, response.getStatus(), duration);
        MDC.clear();
    }
}
