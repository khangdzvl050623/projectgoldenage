package com.khang.goldenage.config;

import com.khang.goldenage.security.filter.AdminAuthorizationFilter;
import com.khang.goldenage.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecutiryConfig {

  private final AdminAuthorizationFilter adminAuthorizationFilter;

  public SecutiryConfig(AdminAuthorizationFilter adminAuthorizationFilter) {
    this.adminAuthorizationFilter = adminAuthorizationFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.POST, "/api/users/login", "/api/users/register")
        .permitAll()
        .requestMatchers(HttpMethod.POST, "/api/users/introspect")
        .hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/admin/client")
        .hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.GET,"/api/ test-send-email").permitAll()
        .requestMatchers(HttpMethod.GET,"/api/exchange-rate/current-exchange-rate").permitAll()
        .requestMatchers(HttpMethod.GET,"/api/gold-prices/current-gold-prices").permitAll()
        .requestMatchers(HttpMethod.GET,"/scrape/history").permitAll()
        .anyRequest().authenticated()

      )
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .addFilterAfter(adminAuthorizationFilter, JwtAuthenticationFilter.class);

    return http.build();
  }

}

