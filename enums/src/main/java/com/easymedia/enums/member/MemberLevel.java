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

import java.util.Map;

/**
 * 사용자 레벨
 */
@Schema(title = "사용자 레벨")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor
public enum MemberLevel implements DatabaseCodeCommonType {
    Master("마스터", "M", null, "통합 관리자"),
    AgentMaster("에이전트 마스터", "A", null, "고객사 관리자"),
    AgentAspMaster("에이전트 ASP 담당", "P", null, "고객사 ASP 관리자"),
    AgentMember("에이전트 회원", "U", null, "고객사 회원"),

    AgentNonMember("에이전트 비 회원", "N", null, "고객사 비 회원"),
    TopManager("최고 관리자", "99", null, "최고 관리자"),
    GeneralManger("일반 관리자", "10", null, "일반 관리자"),
    ;

    private final String label;
    @JsonIgnore
    private final String databaseCode;
    private final String customValue;
    private final String description;

    @JsonCreator
    public static MemberLevel fromCode(Object value) {
        return value instanceof Map ? valueOf(MapUtils.getString((Map) value, "code"))
                : valueOf((String) value);
    }

    public String getCode() {
        return this.name();
    }

    public static MemberLevel lookup(String code) {
        for(MemberLevel level : MemberLevel.values()){
            if(level.databaseCode.equalsIgnoreCase(code)){
                return level;
            }
        }
        return null;
    }
}