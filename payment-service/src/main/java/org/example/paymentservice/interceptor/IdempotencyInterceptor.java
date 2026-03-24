package org.example.paymentservice.interceptor;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.paymentservice.domain.IdempotencyStatus;
import org.example.paymentservice.domain.entity.IdempotencyKey;
import org.example.paymentservice.exception.ServiceException;
import org.example.paymentservice.service.IdempotencyService;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Optional;

import static org.example.paymentservice.constant.WebConstants.WRAPPED_RESPONSE_ATTRIBUTE_NAME;

@Component
@RequiredArgsConstructor
public class IdempotencyInterceptor implements HandlerInterceptor {

    private static final String IDEMPOTENT_KEY_HEADER_NAME = "X-Idempotency-Key";

    private final IdempotencyService idempotencyService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpMethod method = HttpMethod.valueOf(request.getMethod());

        if (HttpMethod.POST.equals(method)) {
            String idempotencyKey = request.getHeader(IDEMPOTENT_KEY_HEADER_NAME);

            if (StringUtil.isNullOrEmpty(idempotencyKey)) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().println("X-Idempotency-Key header is not present");
                return false;
            }

            return processIdempotency(idempotencyKey, response);
        }

        return true;
    }

    private boolean processIdempotency(String idempotencyKey, HttpServletResponse response) throws IOException {
        Optional<IdempotencyKey> existingKey = idempotencyService.getPendingKey(idempotencyKey);

        if (existingKey.isPresent()) {
            return processExistingKey(existingKey.get(), response);
        } else {
            return createNewKey(idempotencyKey, response);
        }
    }

    private boolean createNewKey(String idempotencyKey, HttpServletResponse response) throws IOException {
        try {
            idempotencyService.createPendingKey(idempotencyKey);
            return true;
        } catch (ServiceException e) {
            response.setStatus(HttpStatus.CONFLICT.value());
            response.getWriter().println("Same request is already in progress...");
            return false;
        }
    }

    private boolean processExistingKey(IdempotencyKey idempotencyKey, HttpServletResponse response) throws IOException {
        IdempotencyStatus status = idempotencyKey.getStatus();

        if (status == IdempotencyStatus.PENDING) {
            response.setStatus(HttpStatus.CONFLICT.value());
            response.getWriter().println("Same request is already in progress...");
        } else if (status == IdempotencyStatus.COMPLETED) {
            response.setStatus(idempotencyKey.getStatusCode());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().println(idempotencyKey.getResponse());
        } else {
            throw new IllegalArgumentException("Invalid status of idempotency key: " + status);
        }

        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, @Nullable Exception ex) throws Exception {
        HttpMethod method = HttpMethod.valueOf(request.getMethod());

        if (HttpMethod.POST.equals(method)) {
            //Обертка необходима для возможности повторных чтений response-a
            ContentCachingResponseWrapper wrappedResponse = (ContentCachingResponseWrapper) request.getAttribute(
                WRAPPED_RESPONSE_ATTRIBUTE_NAME);
            String responseBody = new String(wrappedResponse.getContentAsByteArray(),
                wrappedResponse.getCharacterEncoding());

            String idempotencyKey = request.getHeader(IDEMPOTENT_KEY_HEADER_NAME);
            idempotencyService.markAsCompleted(idempotencyKey, responseBody, response.getStatus());
        }
    }
}
