package com.example.health.interceptor;

import com.example.health.dto.ApiLogDto;
import com.example.health.service.LoggingMQService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private final LoggingMQService loggingMQService;
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    public LoggingInterceptor(LoggingMQService messagingService) {
        this.loggingMQService = messagingService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler) {
        startTime.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler, @Nullable Exception ex) {
        try {
            long duration = System.currentTimeMillis() - startTime.get();

            // Get payload based on HTTP method
            String payload = getPayload(request);
            String requestId = response.getHeader("X-Request-ID");
            
            // Get username from gateway header
            String username = request.getHeader("X-User-Name");
            if (username == null || username.trim().isEmpty()) {
                username = request.getRemoteUser() != null ? request.getRemoteUser() : "anonymous";
            }

            ApiLogDto logDto = new ApiLogDto();
            logDto.setRequestId(requestId);
            logDto.setUsername(username);
            logDto.setMethod(request.getMethod());
            logDto.setEndpoint(request.getRequestURI());
            logDto.setStatusCode(response.getStatus());
            logDto.setDuration(duration);
            logDto.setTimestamp(LocalDateTime.now());
            logDto.setPayload(payload);
            

            // Delegate actual send to messaging service
            loggingMQService.sendApiLog(logDto);

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

        // For POST/PUT, use request body
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
            if (request instanceof ContentCachingRequestWrapper) {
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;

                byte[] buf = wrapper.getContentAsByteArray();
                if (buf.length > 0) {
                    String encoding = request.getCharacterEncoding() != null
                            ? request.getCharacterEncoding()
                            : StandardCharsets.UTF_8.name();
                    String payload = new String(buf, 0, buf.length, java.nio.charset.Charset.forName(encoding));
                    return payload.trim(); // Clean before returning
                }
            }
        }

        return null;
    }
}
