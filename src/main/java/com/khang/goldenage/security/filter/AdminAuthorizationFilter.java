package com.khang.goldenage.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;

@Component
public class AdminAuthorizationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // Kiểm tra nếu endpoint cần quyền admin
    String path = request.getRequestURI();
    if (path.startsWith("/api/users") && !path.equals("/api/users/login") && !path.equals("/api/users/register")) {
      // Lấy thông tin xác thực từ SecurityContext
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null || authentication.getAuthorities().stream()
        .noneMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
        // Nếu không phải admin, trả về lỗi 403
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Admin only");
        return;
      }
    }
    // Tiếp tục xử lý request nếu hợp lệ
    filterChain.doFilter(request, response);
  }
}
