package com.easymedia.enums.log;

import com.easymedia.enums.DatabaseCodeCommonType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * 시스템로그 타입
 */
@Schema(title = "시스템로그 타입")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor
public enum SystemLogType implements DatabaseCodeCommonType {
    READ("조회", "R", null, "조회"),
    WRITE("등록", "C", null, "등록"),
    MODIFY("수정", "U", null, "수정"),
    DELETE("삭제", "D", null, "삭제"),
    DOWNLOAD("다운로드", "DW", null, "다운로드"),
    READ_PERSONAL("개인정보 조회", "RP", null, "개인정보 조회"),
    DOWNLOAD_PERSONAL("개인정보 다운로드", "DWP", null, "개인정보 다운로드"),
    LI("로그인", "LOGIN", null, "로그인"),
    LO("로그아웃", "LOGOUT", null, "로그아웃"),
    ;
    private final String label;
    @JsonIgnore
    private final String databaseCode;
    private final String customValue;
    private final String description;

    @JsonCreator
    public static SystemLogType fromCode(Object value) {
        return value instanceof Map ? valueOf(MapUtils.getString((Map) value, "code"))
                : valueOf((String) value);
    }

    public String getCode() {
        return this.name();
    }

}