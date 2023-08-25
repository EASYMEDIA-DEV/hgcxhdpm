package com.easymedia.enums.common;

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
 * 프로젝트 카테고리
 */
@Schema(title = "프로젝트 카테고리")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor
public enum ProjectCategory implements DatabaseCodeCommonType {
    API("API", "A", null, null),
    MNGWSERC("관리자", "M", null, null),
    FRONT("사용자", "F", null, null),
    ;

    private final String label;
    @JsonIgnore
    private final String databaseCode;
    private final String customValue;
    private final String description;

    @JsonCreator
    public static ProjectCategory fromCode(Object value) {
        return value instanceof Map ? valueOf(MapUtils.getString((Map) value, "code"))
                : valueOf((String) value);
    }

    public String getCode() {
        return this.name();
    }
}