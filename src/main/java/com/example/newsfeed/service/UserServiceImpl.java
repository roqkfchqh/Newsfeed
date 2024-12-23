package com.example.newsfeed.service;

import com.example.newsfeed.bcrypt.Encoder;
import com.example.newsfeed.dto.user.*;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
//hibernate 에 대한 스킬적인 부분에서 이점을 얻겠다 , 싶을 때 사용
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
//select for update라는 쿼리 쓰지마라 그냥
    private final UserRepository userRepository;
    private final Encoder encoder;

    @Override
    @Transactional
    //user라는 db엔티티만 잘 처리해주는 역할을 하면 됨. transactional 비추. 트랜잭션을 언제 "열어야" 할까?
    //하나만 락 걸어야하는데 범위로 락을 걸어서 , 대용량 트래픽을 고민할 떄 중요한 부분임
    //필요한 부분에만 락을 걸어야 함
    public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        // 중복 이메일 검증
        Optional<User> checkUser = this.userRepository.findByEmail(createUserRequestDto.getEmail());

        //createUser 를 두 번 호출했을 때
        //A가 캐시를 사용하고, B가 캐시를 사용할 수 없으니 이점이 없는거아님? <- 1차캐시 (메서드 단에서 사용)
        //모두 공유하는게 2차캐시 <- app 단에서 사용하는
        //readOnly -> repository 에서 같은 객체를 여러번 호출하는가???? -> 그런 경우 잘 없음

        //1차 캐시 -> LAZY LOADING 사용 가능
        //로직 안에서 insert 로직이 5번 있을 때
        //5번 sql 문을 호출 하는거보다 한번에 commit 하는 것이 lazy loading
        //1차 캐시를 기준으로 스냅샷을 보며 데이터의 변경점을 알 수 있음. <- 1차 캐시를 통해서 이런 기능을 사용 가능.

        //db 입장에서의 클라이언트 : 우리 서비스(이거).

        //정해진 답은 없으니 항상 객관적인 시선으로 바라보도록

        //"유지보수가 쉬운가 ?" "확장성이 좋은가 ?" "코드가 직관적으로 잘 읽히는가 ?" <- 책처럼 읽히는 코드
        //구현체들에서 메서드들의 캡슐화 이점을 사용해야 함 (private 위주)
        //테스트 코드 공부 해라!!!!!!!! 위의 객체지향적인 코드를 짜는 데에 있어서 정말 도움을 준다.
        //왜 ?? -> 프로젝트가 너무 커져서, api 가 너무 엮여있어서 테스트 효율이 너무 떨어지는 일이 생김.
        //테스트 코드를 잘 짠다면 이러한 과정이 굉장히 압축됨.
        //앵그라인더(네이버에서 개발함)

        //클래스 -> 패키지 -> 모듈(멀티모듈링) -> 이 모듈 자체가 하나의 서비스로 빠져나갈 수 있는가?
        //적절한 객체를 선언하는 것 부터, 스코프를 늘려서 MSA(마이크로 서비스 아키텍처) 에 도달하자

        //이미 대용량트래픽을 처리할 수 있는 해결법은 너무나도 많으니, 기본기에 집중해보자.

        // 유저가 없어야 로직 수행함 -> isPresent()로 null check
        if (checkUser.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_USED_EMAIL);
        }

        // 암호화 후 유저 엔티티 생성
        User user = User.builder()
                .name(createUserRequestDto.getName())
                .email(createUserRequestDto.getEmail())
                .password(encoder.encode(createUserRequestDto.getPassword()))
                .build();

        User savedUser = this.userRepository.save(user);

        return CreateUserResponseDto.of(savedUser);
    }

    @Override
    @Transactional
    public UpdateUserNameResponseDto updateUserName(Long userId, UpdateUserNameRequestDto updateUserReqDto) {
        // 검사
        User findUser = getUserById(userId);

        // 수정
        findUser.setName(updateUserReqDto.getName());

        return UpdateUserNameResponseDto.of(findUser);
    }

    @Override
    @Transactional
    public void updateUserPassword(Long userId, UpdateUserPasswordRequestDto updateUserPasswordRequestDto) {
        // 검사
        User findUser = getUserById(userId);

        String findHashedEmail = findUser.getPassword();

        // 입력받은 현재 비밀번호와 암호화 된 비밀번호 비교
        if (!encoder.matches(updateUserPasswordRequestDto.getCurrentPassword(), findHashedEmail)) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        // 비밀번호 수정
        String updatedHashedPassword = encoder.encode(updateUserPasswordRequestDto.getUpdatePassword());
        findUser.setPassword(updatedHashedPassword);
    }

    @Override
    public FetchUserResponseDto fetchOneById(Long userId) {
        User user = getUserById(userId);

        return FetchUserResponseDto.of(user);
    }

    @Override
    @Transactional
    //우리서비스에서 가장 오래 걸리는 api. 자주 사용되는 api 를 중점적으로 봐야 함
    //deleteuser 같은 경우는 자주 사용되는 부분은 아니므로 transactinal에 관해서 크게 고민안해도 됨.
    public void deleteUser(Long userId, DeleteUserRequestDto deleteUserRequestDto) {
        User findUser = getUserById(userId);

        String findHashedEmail = findUser.getPassword();

        // 입력받은 현재 비밀번호와 암호화 된 비밀번호 비교
        if (!encoder.matches(deleteUserRequestDto.getCurrentPassword(), findHashedEmail)) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        //soft delete
        findUser.softDelete();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
