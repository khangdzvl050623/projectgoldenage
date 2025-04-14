package com.khang.goldenage.service;


import com.khang.goldenage.dto.UserRegisterDto;
import com.khang.goldenage.dto.UserResponseDto;
import com.khang.goldenage.mapper.UserMapper;
import com.khang.goldenage.modal.User;
import com.khang.goldenage.repository.UserRepository;
import com.khang.goldenage.security.JwtUtils;
import com.khang.goldenage.type.EUserRole;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private JwtUtils jwtUtils;


  public UserResponseDto register(UserRegisterDto userRegisterDTO) {
    //Kiểm tra email đã tồn tại
    if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
      throw new EntityExistsException("Email đã tồn tại!");
    }

    //Mã hóa mật khẩu và lưu user
    User user = userMapper.toEntity(userRegisterDTO);
    user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
    user.setRole(EUserRole.CLIENT);
    User savedUser = userRepository.save(user);
    // tao jwt
    String token = jwtUtils.generateToken(savedUser.getEmail(), savedUser.getRole().name());
    UserResponseDto userResponseDto = userMapper.toResponseDto(savedUser); // regis tra ve userreponse voi token
    String bearerToken = "Bearer " + token;
    userResponseDto.setToken(bearerToken);
    return userResponseDto;
  }

  public UserResponseDto login(String email, String password) {
    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new EntityNotFoundException("User not found"));
    // trích test dữ liệu user
    System.out.println("User tim dc:" + user);
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new IllegalArgumentException("Wrong password");

    }
    String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
    UserResponseDto userResponseDto = userMapper.toResponseDto(user); // login tra ve userresponse voi token
    String bearerToken = "Bearer " + token; // thêm tieền tố bearer
    userResponseDto.setToken(bearerToken);
    System.out.println("User :" + userResponseDto);
    System.out.println("Token tim dc:" + token);
    return userResponseDto;
  }

  public List<UserResponseDto> getAllUsers() {
    // Lấy tất cả người dùng từ cơ sở dữ liệu
    List<User> users = userRepository.findAll();

    return users.stream()
      .map(userMapper::toResponseDto)
      .collect(Collectors.toList());
  }

  @PostConstruct // hoac CommandLineRunner de them va kiem tra co admin khong trong lan dau khoi tao,
  public void initCreateAdmin() {
    if (!userRepository.existsByEmail("admin@gmail.com")) {
      User admin = User.builder()
        .email("admin@gmail.com")
        .name("admin")
        .password(passwordEncoder.encode("admindeptrais1"))
        .role(EUserRole.ADMIN)
        .build();
      userRepository.save(admin);
      System.out.println("da them admin" + admin);
    }

  }

}
