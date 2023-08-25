package com.easymedia.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum SearchUseStatus implements DatabaseCodeCommonType {
    Y("사용", "Y", null, "API"),
    N("미사용", "N", null, "관리자"),
    ALL("모두", "", null, "사용자"),
    ;

    private final String label;
    @JsonIgnore
    private final String databaseCode;
    private final String customValue;
    private final String description;

    @JsonCreator
    public static SearchUseStatus fromCode(Object value) {
        return value instanceof Map ? valueOf(MapUtils.getString((Map) value, "code"))
                : valueOf((String) value);
    }

    public String getCode() {
        return this.name();
    }
}
