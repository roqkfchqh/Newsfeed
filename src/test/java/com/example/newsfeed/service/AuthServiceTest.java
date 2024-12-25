package com.example.newsfeed.service;

import com.example.newsfeed.bcrypt.Encoder;
import com.example.newsfeed.dto.auth.LoginUserRequestDto;
import com.example.newsfeed.dto.auth.SignupUserRequestDto;
import com.example.newsfeed.dto.auth.SignupUserResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[/auth] - 단위 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock Encoder encoder;
    @InjectMocks AuthService authService;

    private final String name = "testA";
    private final String email = "test@test.com";
    private final String plainPassword = "123456";
    private final String hashedPassword = "htjiadssadas1$!@4waSD2$!@#%T#>Q@kewa124htkjolhrioq412!@#l";

    @Nested
    @Order(1)
    @DisplayName("[/signup] - 회원 가입")
    class Signup {

        @Test
        @Order(1)
        @DisplayName("[성공]: 회원가입에 성공하면 동일한 이름과 이메일을 가진 객체를 반환")
        public void signup_success() {
            SignupUserRequestDto request = new SignupUserRequestDto(name, email, plainPassword);

            when(encoder.encode(plainPassword)).thenReturn(hashedPassword);

            User user = User.builder()
                    .name(name)
                    .email(email)
                    .password(encoder.encode(request.getPassword()))
                    .build();

            when(userRepository.save(any(User.class))).thenReturn(user);

            SignupUserResponseDto response = authService.executeSignup(request);

            Assertions.assertNotNull(response);
            Assertions.assertEquals(hashedPassword, user.getPassword());
            Assertions.assertEquals(name, response.getUserName());
            Assertions.assertEquals(email, response.getEmail());
        }

        @Test
        @Order(2)
        @DisplayName("[실패]: 사용중인 이메일로 회원가입 시도하면 실패")
        public void signup_fail_duplicate_email() {
            when(encoder.encode(plainPassword)).thenReturn(hashedPassword);

            User user = User.builder()
                    .name(name)
                    .email(email)
                    .password(encoder.encode(plainPassword))
                    .build();

            when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(user));

            SignupUserRequestDto request = new SignupUserRequestDto(name, email, plainPassword);

            CustomException exception = Assertions.assertThrows(CustomException.class, () -> authService.validateExistUserEmail(request.getEmail()));
            Assertions.assertEquals("이미 사용중인 이메일입니다.", exception.getMessage());
            Assertions.assertEquals(ErrorCode.ALREADY_USED_EMAIL.getStatus(), exception.getStatus());
        }
    }


    @Nested
    @Order(2)
    @DisplayName("[/login] - 로그인")
    class Login {

        @Test
        @Order(1)
        @DisplayName("[성공]: 로그인에 성공하면 해당 유저의 키 값(ID) 값을 반환")
        public void login_success() {
            User user = User.builder()
                    .name(name)
                    .email(email)
                    .password(hashedPassword)
                    .build();

            User mUser = Mockito.spy(user); // user.getId()의 값 반환을 위해 spy 사용
            Mockito.doReturn(1L).when(mUser).getId();

            when(userRepository.findByEmail(mUser.getEmail())).thenReturn(Optional.of(mUser));
            when(userRepository.findById(1L)).thenReturn(Optional.of(mUser));
            when(encoder.matches(plainPassword, hashedPassword)).thenReturn(true);

            LoginUserRequestDto request = new LoginUserRequestDto(email, plainPassword);

            Long userid = authService.getUserIdByEmail(request.getEmail());
            authService.validateUserPassword(userid, request.getPassword());
            Long loginId = authService.login(request);

            Assertions.assertNotNull(loginId);
            Assertions.assertEquals(mUser.getId(), loginId);
        }

        @Test
        @Order(2)
        @DisplayName("[실패]: 틀린(없는) 아이디(이메일)로 요청하는 경우")
        public void login_fail_not_exist_mail() {
            LoginUserRequestDto request = new LoginUserRequestDto(email, plainPassword);

            CustomException exception = Assertions.assertThrows(CustomException.class, () -> authService.getUserIdByEmail(request.getEmail()));
            Assertions.assertEquals("이메일이나 비밀번호를 잘못 입력하였습니다.", exception.getMessage());
            Assertions.assertEquals(ErrorCode.WRONG_EMAIL_OR_PASSWORD.getStatus(), exception.getStatus());
        }

        @Test
        @Order(3)
        @DisplayName("[실패]: 잘못된 비밀번호로 요청하는 경우")
        public void login_fail_wrong_password() {
            LoginUserRequestDto request = new LoginUserRequestDto(email, plainPassword);

            User user = User.builder()
                    .name(name)
                    .email(email)
                    .password(hashedPassword)
                    .build();

            User mUser = Mockito.spy(user); // user.getId()의 값 반환을 위해 spy 사용
            Mockito.doReturn(1L).when(mUser).getId();

            when(userRepository.findById(mUser.getId())).thenReturn(Optional.of(mUser));
            when(encoder.matches(plainPassword, hashedPassword)).thenReturn(false);

            CustomException exception = Assertions.assertThrows(CustomException.class, () -> authService.validateUserPassword(mUser.getId(), request.getPassword()));
            Assertions.assertEquals("이메일이나 비밀번호를 잘못 입력하였습니다.", exception.getMessage());
            Assertions.assertEquals(ErrorCode.WRONG_EMAIL_OR_PASSWORD.getStatus(), exception.getStatus());
        }
    }
}