package com.khang.goldenage.controller;

import com.khang.goldenage.dto.UserLoginDto;
import com.khang.goldenage.dto.UserRegisterDto;
import com.khang.goldenage.dto.UserResponseDto;
import com.khang.goldenage.modal.User;
import com.khang.goldenage.repository.UserRepository;
import com.khang.goldenage.security.JwtUtils;
import com.khang.goldenage.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/api/users")
public class UserController {
  @Autowired
  private UserService userService;
  @Autowired
  private JwtUtils jwtUtils;

  @PostMapping("/register")
  public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRegisterDto userRegisterDto) {// @Valid dung de Kiểm tra và xác thực dữ liệu đầu vào theo các ràng buộc được khai báo( vd : @NotBlank hay @Size,..)
    return ResponseEntity.ok(userService.register(userRegisterDto));  // @RequestBody aánh xạ chuyen doi json thanh doi tuong java
  }

  @PostMapping("/login")
  public ResponseEntity<UserResponseDto> login(@RequestBody UserLoginDto userLoginDto) {
    return ResponseEntity.ok(userService.login(userLoginDto.getEmail(), userLoginDto.getPassword()));
  }
  @PostMapping("/introspect")
  public ResponseEntity<Map<String, Object>> introspectToken(@RequestHeader("Authorization") String authHeader) {
    // Lấy token từ header
    String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

    // Kiểm tra token có hợp lệ không
    if (jwtUtils.validateToken(token)) {
      Claims claims = jwtUtils.getClaimsFromToken(token);
      Map<String, Object> response = new HashMap<>();
      response.put("active", true);
      response.put("sub", claims.getSubject()); //email , id....
      response.put("exp", claims.getExpiration()); // thoi gian het han
      response.put("issuedAt", claims.getIssuedAt()); // thời gian caap
      return ResponseEntity.ok(response);
    } else {
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("active", false);
      errorResponse.put("error", "Invalid token");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
  }

}
