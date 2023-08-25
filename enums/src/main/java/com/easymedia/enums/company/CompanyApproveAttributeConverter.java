package com.easymedia.enums.company;

import com.easymedia.enums.DatabaseCodeCommonTypeAttributeConverter;
import org.apache.ibatis.type.MappedTypes;

import javax.persistence.Converter;

@MappedTypes(CompanyApprove.class)
@Converter
public class CompanyApproveAttributeConverter extends DatabaseCodeCommonTypeAttributeConverter<CompanyApprove> {

    public static final String ENUM_NAME = "회사 승인";

    public CompanyApproveAttributeConverter() {
        super(ENUM_NAME, CompanyApprove.class);
    }
}

