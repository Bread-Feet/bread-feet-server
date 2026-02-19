package kr.co.breadfeetserver.infra.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //리뷰 관련 에러
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    REVIEW_LIKE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 좋아요 누른 리뷰입니다."),
    // 다이어리 관련 에러
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "다이어리를 찾을 수 없습니다."),
    // 유저 관련 에러
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USER_INFO_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 정보입니다."),
    USER_NOT_ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없는 사용자입니다"),
    USER_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 저장에 실패했습니다."),

    // 빵집 관련 에러
    BAKERY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 빵집입니다."),
    BAKERY_NOT_FOUND(HttpStatus.NOT_FOUND, "빵집을 찾을 수 없습니다."),

    // 메뉴 관련 에러
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."),

    // like 관련 에러
    BOOKMARK_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 북마크한 게시글입니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크를 찾을 수 없습니다."),

    // media 관련 에러
    MEDIA_NOT_FOUND(HttpStatus.NOT_FOUND, "미디어를 찾을 수 없습니다."),

    // storage 관련 에러
    WRONG_FOLDER_TYPE(HttpStatus.BAD_REQUEST, "잘못된 폴더명 입니다."),
    NOTFOUND_FILE_NAME(HttpStatus.BAD_REQUEST, "파일 이름이 존재하지 않습니다."),

    // 인증 관련 에러
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    // 공통 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값입니다."),

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
