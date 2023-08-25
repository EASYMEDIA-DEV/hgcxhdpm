package com.easymedia.enums.member;

import com.easymedia.enums.DatabaseCodeCommonTypeAttributeConverter;
import org.apache.ibatis.type.MappedTypes;

import javax.persistence.Converter;

@MappedTypes(MemberApprove.class)
@Converter
public class MemberApproveAttributeConverter extends DatabaseCodeCommonTypeAttributeConverter<MemberApprove> {

    public static final String ENUM_NAME = "사용자 승인";

    public MemberApproveAttributeConverter() {
        super(ENUM_NAME, MemberApprove.class);
    }
}

