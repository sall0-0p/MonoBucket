package com.lordbucket.bucketbank.config;

import com.lordbucket.bucketbank.middleware.SessionAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/**");
    }
}
