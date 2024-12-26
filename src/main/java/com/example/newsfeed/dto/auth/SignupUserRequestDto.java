package com.example.newsfeed.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupUserRequestDto {

    @NotBlank
    @Size(min = 1, max = 4, message = "이름은 1-4자 사이로 작성해 주세요.")
    private final String name;

    @NotBlank @Size(min = 5, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "올바른 이메일 형식이 아닙니다.")
    private final String email;

    @NotBlank @Size(min = 6, max = 15, message = "비밀번호는 6-15자 사이입니다.")
    private final String password;
}
