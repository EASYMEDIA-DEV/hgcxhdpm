package com.easymedia.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /**
     * Server Error.
     */
    INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST.value(), "S001", "서버에서 오류가 발생하였습니다."),
    NOT_IMPLEMENTED(HttpStatus.BAD_REQUEST.value(), "S002", "구현되지 않은 서비스 입니다."),
    SERVICE_UNAVAILABLE(HttpStatus.BAD_REQUEST.value(), "S003", "서비스를 사용할 수 없습니다."),

    /**
     * Client Error.
     */
    // Method Allowed:M
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "M001", "지원하지 않은 HTTP method 입니다."),

    // Forbidden:F
    NOT_AUTHENTICATION(HttpStatus.FORBIDDEN.value(), "F001", "권한이 없습니다."),
    CANNOT_CREATED(HttpStatus.FORBIDDEN.value(), "F002", "등록할 수 없습니다."),
    CANNOT_MODIFIED(HttpStatus.FORBIDDEN.value(), "F003", "수정할 수 없습니다."),
    CANNOT_DELETE(HttpStatus.FORBIDDEN.value(), "F004", "삭제할 수 없습니다."),
    DUPLICATE(HttpStatus.FORBIDDEN.value(), "F005", "중복 되었습니다."),
    NOT_MATCHED(HttpStatus.FORBIDDEN.value(), "F006", "일치하지 않습니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "F007", "권한이 없습니다."),//"접근이 거부되었습니다."
    EXPIRED_TIME(HttpStatus.FORBIDDEN.value(), "F008", "기간이 만료 되었습니다."),
    CANNOT_READ(HttpStatus.FORBIDDEN.value(), "F009", "조회할 수 없습니다."),
    CANNOT_REFUND(HttpStatus.FORBIDDEN.value(), "F013", "취소할 수 없는 예약 건입니다.</br>고객센터에 문의 바랍니다."),
    ACCESS_DENIED_CSRF(HttpStatus.FORBIDDEN.value(), "F010", "권한이 없습니다."),//"접근이 거부되었습니다."

    // Invalid:I
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "I001", "입력값이 유효하지 않습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(), "I002", "타입이 유효하지 않습니다."),
    INVALID_TIME_VALUE(HttpStatus.BAD_REQUEST.value(), "I003", "기간이 유효하지 않습니다."),
    INVALID_CANCEL_TRAVELER_VALUE(HttpStatus.BAD_REQUEST.value(), "I004", "이미 취소된 여행자 입니다."),

    // Time Out:T
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT.value(), "T001", "요청 시간이 초과 되었습니다."),

    // Not Found:N
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "N001", "찾을 수 없습니다."),
    VIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "N002", "화면을 찾을 수 없습니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "N003", "데이터를 찾을 수 없습니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "N004", "엔티티를 찾을 수 없습니다."),
    SEARCH_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "N005", "검색조건타입을 찾을 수 없습니다."),
    CODE_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "N006", "코드타입을 찾을 수 없습니다."),

    // File:FL
    FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST.value(), "FL001", "파일 전송이 실패 하였습니다."),
    FILE_DOWNLOAD_FAIL(HttpStatus.BAD_REQUEST.value(), "FL002", "파일 다운로드를 실패 하였습니다."),
    FILE_DELETE_FAIL(HttpStatus.BAD_REQUEST.value(), "FL003", "파일 삭제를 실패 하였습니다."),
    FILE_COPY_FAIL(HttpStatus.BAD_REQUEST.value(), "FL004", "파일 복사를 실패 하였습니다."),
    FILE_EXCEED_MAX_SIZE(HttpStatus.BAD_REQUEST.value(), "FL005", "파일 크기가 50MB를 초과하였습니다."),

    // Interface
    API_ERROR(HttpStatus.BAD_REQUEST.value(), "ERA001", "API ERROR"),
    HPOINT_ERROR(HttpStatus.BAD_REQUEST.value(), "HP001", "HPoint ERROR"),
    INICIS_ERROR(HttpStatus.BAD_REQUEST.value(), "INI001", "Inicis ERROR"),

    ERA_NOT_INVOICED(HttpStatus.BAD_REQUEST.value(), "ERA002", "ERA NOT INVOICED"),

    // 메일, 알림톡 발송 오류
    SEND_MSG_FAIL(HttpStatus.BAD_REQUEST.value(), "SM001", "메일, 알림톡 발송 실패하였습니다."),

    //로그인
    LGN_NOT_USE_YN(HttpStatus.CONFLICT.value(), "LGN001", "사용할 수 없습니다."),
    LGN_NOT_TEMP_PWD(HttpStatus.CONFLICT.value(), "LGN002", "임시 비밀번호입니다."),
    LGN_CHG_PWD(HttpStatus.CONFLICT.value(), "LGN003", "비밀번호를 변경해야합니다."),
    LGN_FAIL_CNT(HttpStatus.CONFLICT.value(), "LGN004", "관리자 문의 바랍니다."),
    LGN_NOT_FOUND_USER(HttpStatus.CONFLICT.value(), "N005", "이메일 및 패스워드를 확인해주세요."),
    LGN_DUPLICATION_PWD(HttpStatus.CONFLICT.value(), "N006", "이전 비밀번호와 동일합니다."),
    LGN_NOT_FOUND_PWD_ID(HttpStatus.CONFLICT.value() , "N007","비밀번호를 확인 해주세요.");
    ;
    private final int status;
    private final String code;
    private final String message;

    public String code() {
        return code;
    }
}
