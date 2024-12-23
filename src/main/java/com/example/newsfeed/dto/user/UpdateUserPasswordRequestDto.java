package com.example.newsfeed.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserPasswordRequestDto {

    @NotBlank @Size(min = 6, max = 15)
    private final String currentPassword;

    @NotBlank @Size(min = 6, max = 15)
    private final String updatePassword;
}
