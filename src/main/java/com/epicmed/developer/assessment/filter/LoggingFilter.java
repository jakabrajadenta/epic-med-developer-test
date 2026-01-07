package com.epicmed.developer.assessment.filter;

import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.epicmed.developer.assessment.util.constant.GeneralConstant.DOUBLE_DASH;
import static com.epicmed.developer.assessment.util.constant.GeneralConstant.X_TRACE_ID;

@Log4j2
@Component
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {

    private final Tracer tracer;

    // Exclude paths that don't need logging (optional)
    private final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/health", "/actuator", "/favicon.ico"
    );

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws IOException, ServletException {

        var traceId = tracer.currentSpan() != null ? Objects.requireNonNull(tracer.currentSpan()).context().traceId() : DOUBLE_DASH;
        response.setHeader(X_TRACE_ID, traceId);

        var path = request.getRequestURI();
        var method = request.getMethod();

        // Skip logging for excluded paths
        if (shouldExclude(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Wrap request and response for content caching
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            // Log request details
            logRequestDetails(wrappedRequest);

            // Continue filter chain
            chain.doFilter(wrappedRequest, wrappedResponse);

        } finally {
            // Log response details
            long duration = System.currentTimeMillis() - startTime;
            logResponseDetails(wrappedResponse, duration, method, path);

            // Copy response body back to original response
            wrappedResponse.copyBodyToResponse();
        }
    }

    private boolean shouldExclude(String path) {
        return EXCLUDE_PATHS.stream().anyMatch(path::contains);
    }

    private void logRequestDetails(ContentCachingRequestWrapper request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String clientIP = getClientIP(request);

        log.info("====== START REQUEST {} {} ======", method, path);
        log.info("Client IP: {}", clientIP);
        log.info("Query Params: {}", queryString != null ? queryString : "None");

        // Log request headers (optional)
        log.debug("Headers: {}", getRequestHeaders(request));

        // Log request body
        String requestBody = getRequestBody(request);
        if (!requestBody.isEmpty() && !requestBody.trim().equals("{}")) {
            log.info("Request Body: {}", requestBody.replaceAll("\\s+", " "));
        }
    }

    private void logResponseDetails(ContentCachingResponseWrapper response, long duration, String method, String path) {
        int status = response.getStatus();
        String responseBody = getResponseBody(response);

        log.info("Response Status: {}", status);
        log.info("Duration: {} ms", duration);

        if (!responseBody.isEmpty() && !responseBody.trim().equals("{}")) {
            log.info("Response Body: {}", responseBody.replaceAll("\\s+", " "));
        }

        log.info("====== END {} {} ======", method, path);
        log.debug(""); // Empty line for better readability
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    private String getRequestHeaders(ContentCachingRequestWrapper request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> headers.append(headerName).append(": ").append(request.getHeader(headerName)).append("; "));
        return headers.toString();
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        return content.length > 0 ? new String(content, StandardCharsets.UTF_8) : "";
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        return content.length > 0 ? new String(content, StandardCharsets.UTF_8) : "";
    }
}