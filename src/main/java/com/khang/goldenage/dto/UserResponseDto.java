package com.khang.goldenage.dto;

import com.khang.goldenage.type.EUserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDto {
  Long id;
  String name;
  String email;
  String token;
  @Enumerated(EnumType.STRING)
  EUserRole userRole;
}


