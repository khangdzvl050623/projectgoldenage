package com.khang.goldenage.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
      .allowedOrigins(
        "http://localhost:4000",
        "http://192.168.2.65:4000",
        "https://goldenages.online",
        "http://goldenages.online",
        "https://api.goldenages.online",
        "https://frontend-domain.onrender.com",
        "http://goldenages-3.onrender.com",
        "https://goldenages-3.onrender.com"
      )
      .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
      .allowedHeaders("*")
      .exposedHeaders("Access-Control-Allow-Origin")
      .allowCredentials(true);
  }
}
