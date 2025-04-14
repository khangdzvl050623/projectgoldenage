package com.khang.goldenage.mapper;

import com.khang.goldenage.dto.UserRegisterDto;
import com.khang.goldenage.dto.UserResponseDto;
import com.khang.goldenage.modal.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")

public interface UserMapper {

  User toEntity(UserRegisterDto userRegisterDto); // Chuyển từ DTO đăng ký sang entity

  @Mapping(source = "role", target = "userRole") // anh xa truong entity map voi response (de bi mac loi~)
  UserResponseDto toResponseDto(User user); // tu entity sang DTO trả về
}
