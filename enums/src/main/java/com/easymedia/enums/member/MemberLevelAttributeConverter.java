package com.easymedia.enums.member;

import com.easymedia.enums.DatabaseCodeCommonTypeAttributeConverter;
import org.apache.ibatis.type.MappedTypes;

import javax.persistence.Converter;

@MappedTypes(MemberLevel.class)
@Converter
public class MemberLevelAttributeConverter extends DatabaseCodeCommonTypeAttributeConverter<MemberLevel> {

    public static final String ENUM_NAME = "사용자 레벨";

    public MemberLevelAttributeConverter() {
        super(ENUM_NAME, MemberLevel.class);
    }
}

