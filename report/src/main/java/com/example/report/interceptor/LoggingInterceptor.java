package com.example.report.interceptor;

import com.example.report.dto.ApiLogDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    public LoggingInterceptor(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        startTime.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
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

            // Send to ActiveMQ topic
            String logMessage = objectMapper.writeValueAsString(logDto);
            System.out.println("Sending log message: " + logMessage);
            jmsTemplate.convertAndSend("zoo.service.logs", logMessage);

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
