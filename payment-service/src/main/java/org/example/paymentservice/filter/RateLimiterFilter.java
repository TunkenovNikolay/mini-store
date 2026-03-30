package org.example.paymentservice.filter;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

@Component
@Order(1)
@Slf4j
public class RateLimiterFilter implements Filter {

    private final Cache<String, Bucket> BUCKETS;

    public RateLimiterFilter(Cache<String, Bucket> bucketCache) {
        this.BUCKETS = bucketCache;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String remoteAddr = servletRequest.getRemoteAddr();

        Bucket bucket = Objects.requireNonNull(
            BUCKETS.get(remoteAddr, this::newBucket),
            "Bucket creation failed"
        );

        httpResponse.setHeader("X-RateLimit-Limit", "2");
        httpResponse.setHeader("X-RateLimit-Remaining", String.valueOf(bucket.getAvailableTokens()));

        log.debug("Доступно {} токенов", bucket.getAvailableTokens());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpResponse.setHeader("X-RateLimit-Reset", "5");
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write("Too Many Requests");
        }
    }

    @NotNull
    private Bucket newBucket(String ipAddress) {
        Bandwidth limit = Bandwidth.builder()
            .capacity(10)
            .refillIntervally(1, Duration.ofSeconds(5))
            .build();

        return Bucket.builder()
            .addLimit(limit)
            .build();
    }

    @Bean
    public Cache<String, Bucket> bucketCache() {
        return Caffeine.newBuilder()
            .maximumSize(10000)  // храним только 10000 IP
            .expireAfterAccess(Duration.ofHours(1))
            .recordStats()
            .build();
    }
}
