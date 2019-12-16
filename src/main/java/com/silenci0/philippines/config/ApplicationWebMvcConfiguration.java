package com.silenci0.philippines.config;

import com.silenci0.philippines.web.interceptors.LoggedInMessageInterceptor;
import com.silenci0.philippines.web.interceptors.LoggerInterceptor;
import com.silenci0.philippines.web.interceptors.RequestProcessingTimeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationWebMvcConfiguration implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new RequestProcessingTimeInterceptor())
    .excludePathPatterns("/css/**", "/js/**", "/webfonts/**");

    registry.addInterceptor(new LoggerInterceptor())
      .excludePathPatterns("/css/**", "/js/**", "/webfonts/**");

    registry.addInterceptor(new LoggedInMessageInterceptor());
  }
}
