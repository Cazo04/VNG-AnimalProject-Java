package com.example.animal.interceptor;

import com.example.animal.dto.ApiLogDto;
import com.example.animal.service.MessagingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private final MessagingService messagingService;
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    public LoggingInterceptor(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        startTime.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) {
        try {
            long duration = System.currentTimeMillis() - startTime.get();
            
            // Get payload based on HTTP method
            String payload = getPayload(request);

            ApiLogDto logDto = new ApiLogDto(
                request.getRemoteUser() != null ? request.getRemoteUser() : "anonymous",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration,
                LocalDateTime.now(),
                payload
            );

            // Delegate actual send to messaging service
            messagingService.sendZooLog(logDto);

        } catch (Exception e) {
            // Log error but don't interrupt the request flow
            System.err.println("Error sending log to ActiveMQ: " + e.getMessage());
        } finally {
            startTime.remove();
        }
    }

    private String getPayload(HttpServletRequest request) {
        String method = request.getMethod();
        
        // For GET/DELETE, use query string
        if ("GET".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
            return request.getQueryString();
        }
        
        // For POST/PUT/PATCH, get JSON body from request attribute
        // (requires a request wrapper filter to cache the body)
        Object bodyAttribute = request.getAttribute("cachedRequestBody");
        if (bodyAttribute != null) {
            return bodyAttribute.toString();
        }
        
        return null;
    }
}
