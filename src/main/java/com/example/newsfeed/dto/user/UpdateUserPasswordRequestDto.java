package com.example.newsfeed.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserPasswordRequestDto {

    @NotBlank @Size(min = 6, max = 15, message = "비밀번호는 6-15자 사이입니다.")
    private final String currentPassword;

    @NotBlank @Size(min = 6, max = 15, message = "변경할 비밀번호는 6-15자 사이로 입력해 주세요.")
    private final String updatePassword;
}
