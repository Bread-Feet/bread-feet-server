package kr.co.breadfeetserver.infra.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // post 관련 에러
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."),

    // comment 관련 에러
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    // like 관련 에러
    LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 좋아요를 누른 게시글입니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 찾을 수 없습니다."),

    // archive 관련 에러
    ARCHIVE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 보관한 게시글입니다."),
    ARCHIVE_NOT_FOUND(HttpStatus.NOT_FOUND, "보관함에서 게시글을 찾을 수 없습니다."),

    // media 관련 에러
    MEDIA_NOT_FOUND(HttpStatus.NOT_FOUND, "미디어를 찾을 수 없습니다."),

    // storage 관련 에러
    STORAGE_FILE_EMPTY(HttpStatus.BAD_REQUEST, "파일이 비어있습니다."),
    STORAGE_FILE_INVALID_NAME(HttpStatus.BAD_REQUEST, "파일명이 유효하지 않습니다."),
    STORAGE_FILE_INVALID_EXTENSION(HttpStatus.BAD_REQUEST, "허용되지 않은 파일 확장자입니다."),
    STORAGE_FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기가 제한을 초과했습니다."),
    STORAGE_FILE_INVALID_TYPE(HttpStatus.BAD_REQUEST, "파일의 MIME 타입을 확인할 수 없습니다."),
    STORAGE_FILE_CORRUPTED(HttpStatus.BAD_REQUEST, "파일이 손상되었거나 유효하지 않습니다."),
    STORAGE_FILE_FORGED(HttpStatus.BAD_REQUEST, "파일 형식이 확장자와 일치하지 않습니다."),
    STORAGE_FILE_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "파일 검증 중 오류가 발생했습니다."),
    STORAGE_FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    STORAGE_FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다."),
    STORAGE_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    STORAGE_DIRECTORY_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "디렉토리 생성에 실패했습니다."),

    // payout 관련 에러
    PAYOUT_NOT_FOUND(HttpStatus.NOT_FOUND, "정산 내역을 찾을 수 없습니다."),

    // subscription 관련 에러
    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "구독 정보를 찾을 수 없습니다."),
    SUBSCRIPTION_TIER_NOT_FOUND(HttpStatus.NOT_FOUND, "구독 등급을 찾을 수 없습니다."),
    SELF_SUBSCRIPTION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "자기 자신을 구독할 수 없습니다."),

    // transaction 관련 에러
    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "거래 내역을 찾을 수 없습니다."),

    // 유저 관련 에러
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USER_INFO_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 정보입니다."),
    USER_NOT_ACCESS_FORBIDDEN(HttpStatus.BAD_REQUEST, "접근 권한이 없는 사용자입니다"),
    USER_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 저장에 실패했습니다."),

    // room 관련 에러
    DO_NOT_SELF_CREATE_ROOM(HttpStatus.BAD_REQUEST, "자기자신에게 채팅을 할 수 없습니다."),

    // poll 관련 에러
    POLL_NOT_FOUND(HttpStatus.NOT_FOUND, "투표를 찾을 수 없습니다."),
    POLL_ENDED(HttpStatus.BAD_REQUEST, "종료된 투표입니다."),
    ALREADY_VOTED(HttpStatus.CONFLICT, "이미 투표했습니다."),

    // notification 관련 에러
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다."),

    // 인증 관련 에러
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    // 공통 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값입니다."),

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
