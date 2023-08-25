package com.easymedia.enums.company;

import com.easymedia.enums.DatabaseCodeCommonTypeAttributeConverter;
import org.apache.ibatis.type.MappedTypes;

import javax.persistence.Converter;

@MappedTypes(BankType.class)
@Converter
public class BankTypeAttributeConverter extends DatabaseCodeCommonTypeAttributeConverter<BankType> {

    public static final String ENUM_NAME = "은행 타입";

    public BankTypeAttributeConverter() {
        super(ENUM_NAME, BankType.class);
    }
}

