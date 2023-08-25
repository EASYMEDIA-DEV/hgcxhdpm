package com.easymedia.enums.company;

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
 * 회사 승인
 */
@Schema(title = "회사 승인")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor
public enum CompanyApprove implements DatabaseCodeCommonType {
    Wait("신청", "W", null, "신청"),
    Approve("승인", "A", null, "승인"),
    Reject("거절", "R", null, "승인 거절"),
    Withdraw("탈퇴", "D", null, "탈퇴"),
    ;

    private final String label;
    @JsonIgnore
    private final String databaseCode;
    private final String customValue;
    private final String description;

    @JsonCreator
    public static CompanyApprove fromCode(Object value) {
        return value instanceof Map ? valueOf(MapUtils.getString((Map) value, "code"))
                : valueOf((String) value);
    }

    public String getCode() {
        return this.name();
    }
}