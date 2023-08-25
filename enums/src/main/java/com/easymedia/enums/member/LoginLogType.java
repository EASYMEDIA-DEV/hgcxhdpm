package com.easymedia.enums.member;

import com.easymedia.enums.DatabaseCodeCommonType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

/**
 * 로그인로그 타입
 */
@Schema(title = "로그인로그 타입")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor
public enum LoginLogType implements DatabaseCodeCommonType {
    SUCCESS("성공", "OK", null, "로그인 성공"),
    NOT_FOUND_MEMBER("사용자 ID 없음", "NM", null, "사용자 ID 없음"),
    BAD_CREDENTIALS("비밀번호 오류", "BC", null, "비밀번호 오류"),
    LOCKED_MEMBER("잠금된 사용자 ID", "LM", null, "잠금된 사용자 ID 입니다\n고객센터 02-3014-2498/2499로 연락부탁드립니다."),
    DELETED_MEMBER("사용중지된 사용자 ID", "DM", null, "사용중지된 사용자 ID"),
    DELETED_COMPANY("사용중지된 고객사", "DC", null, "사용중지된 고객사"),
    WITHDRAWN_MEMBER("탈퇴된 사용자 ID", "WM", null, "탈퇴된 사용자 ID"),
    WITHDRAWN_COMPANY("탈퇴된 고객사", "WC", null, "탈퇴된 고객사"),
    NOT_APPROVED_MEMBER("승인되지 않은 ID", "AM", null, "승인되지 않은 ID"),
    NOT_APPROVED_COMPANY("승인되지 않은 고객사", "AC", null, "승인되지 않은 고객사"),
    NOT_ALLOWED_ADMIN_SITE("관리자 사이트 접근 허가되지 않음", "SA", null, "관리자 사이트 접근 허가되지 않음"),
    NOT_ALLOWED_CLIENT_SITE("제휴사 사이트 접근 허가되지 않음", "SC", null, "제휴사 사이트 접근 허가되지 않음"),
    ETC("기타", "ET", null, "기타"),
    ;

    private final String label;
    @JsonIgnore
    private final String databaseCode;
    private final String customValue;
    private final String description;

    @JsonCreator
    public static LoginLogType fromCode(Object value) {
        return value instanceof Map ? valueOf(MapUtils.getString((Map) value, "code"))
                : valueOf((String) value);
    }

    public static LoginLogType of(AuthenticationException failed) {
        if (failed instanceof UsernameNotFoundException) {
            return NOT_FOUND_MEMBER;

        } else if (failed instanceof BadCredentialsException) {
            return BAD_CREDENTIALS;

        } else if (failed instanceof LockedException) {
            return LOCKED_MEMBER;

        } else if (failed instanceof DisabledException) {
            return DELETED_MEMBER;
        }
        return ETC;
    }

    public String getCode() {
        return this.name();
    }

    public String getNameForFront() {
        return (this == BAD_CREDENTIALS || this == NOT_FOUND_MEMBER) ? "NOT_FOUND_MEMBER_AND_PASSWORD" : this.name();
    }

    public String getLabelForFront() {
        return (this == BAD_CREDENTIALS || this == NOT_FOUND_MEMBER) ? "사용자 ID와 비밀번호를 찾을 수 없습니다." : this.getLabel();
    }

}