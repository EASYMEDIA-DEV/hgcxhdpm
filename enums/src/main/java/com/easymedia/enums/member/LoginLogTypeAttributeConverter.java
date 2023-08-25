package com.easymedia.enums.member;

import com.easymedia.enums.DatabaseCodeCommonTypeAttributeConverter;
import org.apache.ibatis.type.MappedTypes;

import javax.persistence.Converter;

@MappedTypes(LoginLogType.class)
@Converter
public class LoginLogTypeAttributeConverter extends DatabaseCodeCommonTypeAttributeConverter<LoginLogType> {

    public static final String ENUM_NAME = "로그인 로그 타입";

    public LoginLogTypeAttributeConverter() {
        super(ENUM_NAME, LoginLogType.class);
    }
}

