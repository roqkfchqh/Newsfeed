package com.example.newsfeed.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400
    PAGING_ERROR(HttpStatus.BAD_REQUEST, "페이지 입력값이 잘못되었습니다."),
    ALREADY_USED_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호를 잘못 입력하였습니다."),
    NICKNAME_REQUIRED(HttpStatus.BAD_REQUEST, "닉네임 입력값이 잘못되었습니다."),
    PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "비밀번호 입력값이 잘못되었습니다."),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 눌렀습니다."),
    NOT_LIKED(HttpStatus.BAD_REQUEST, "좋아요 수를 업데이트 할 수 없습니다."),
    ALREADY_FRIEND(HttpStatus.BAD_REQUEST, "이미 친구관계입니다."),
    ALREADY_NOT_FRIEND(HttpStatus.BAD_REQUEST, "이미 친구관계가 아닙니다."),
    ALREADY_FRIEND_REQUEST(HttpStatus.BAD_REQUEST, "이미 요청 진행중인 상태입니다."),

    //401
    WRONG_EMAIL_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "이메일이나 비밀번호를 잘못 입력하였습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    //403
    FORBIDDEN_OPERATION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    FORBIDDEN_OPERATION_LIKE(HttpStatus.FORBIDDEN, "본인의 게시글 또는 댓글에는 좋아요 기능을 사용할 수 없습니다."),

    //404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),
    CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "없는 페이지입니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 누른 기록이 없습니다."),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 요청입니다."),

    //500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 관리를 못해서 줴송합니다.."),

    //502
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "잘못된 접근입니다.");

    private final HttpStatus status;
    private final String message;
}
