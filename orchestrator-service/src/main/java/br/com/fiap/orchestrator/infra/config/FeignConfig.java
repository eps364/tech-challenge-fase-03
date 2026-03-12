package br.com.fiap.orchestrator.infra.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(HttpServletRequest request) {
        return template -> {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && !authorization.isBlank()) {
                template.header("Authorization", authorization);
            }
        };
    }
}

//package br.com.fiap.orchestrator.infra.config;
//
//import feign.RequestInterceptor;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@Configuration
//public class FeignConfig {
//
//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return template -> {
//            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
//            if (attributes instanceof ServletRequestAttributes servletAttributes) {
//                HttpServletRequest request = servletAttributes.getRequest();
//                String authorization = request.getHeader("Authorization");
//                if (authorization != null && !authorization.isBlank()) {
//                    template.header("Authorization", authorization);
//                }
//            }
//        };
//    }
//}