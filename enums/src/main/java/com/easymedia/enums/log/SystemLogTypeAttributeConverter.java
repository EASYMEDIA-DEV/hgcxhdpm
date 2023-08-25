package com.easymedia.enums.log;

import com.easymedia.enums.DatabaseCodeCommonTypeAttributeConverter;
import org.apache.ibatis.type.MappedTypes;

import javax.persistence.Converter;

@MappedTypes(SystemLogType.class)
@Converter
public class SystemLogTypeAttributeConverter extends DatabaseCodeCommonTypeAttributeConverter<SystemLogType> {

    public static final String ENUM_NAME = "시스템로그 타입";

    public SystemLogTypeAttributeConverter() {
        super(ENUM_NAME, SystemLogType.class);
    }
}

