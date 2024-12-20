package com.example.newsfeed.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserNameRequestDto {

    @NotBlank @Size(min = 1, max = 4)
    private final String name;
}
