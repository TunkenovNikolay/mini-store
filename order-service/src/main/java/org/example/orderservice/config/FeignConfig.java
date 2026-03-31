package org.example.orderservice.config;

import feign.codec.ErrorDecoder;
import org.example.orderservice.integration.payment.exception.PaymentErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.example.orderservice.integration")
public class FeignConfig {

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new PaymentErrorDecoder();
    }

}
