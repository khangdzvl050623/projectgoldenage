package com.khang.goldenage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class UserRegisterDto {
  @NotBlank
  @Size(min = 3, max = 50)
  String name;

  @NotBlank // khong duoc rong~ null va khong dc co dau cach
  @Email
  String email;

  @NotBlank
  @Size(min = 6, max = 60)
  String password;

}
