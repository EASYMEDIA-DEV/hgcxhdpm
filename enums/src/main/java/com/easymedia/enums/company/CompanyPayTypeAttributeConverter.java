package com.easymedia.enums.company;

import com.easymedia.enums.DatabaseCodeCommonTypeAttributeConverter;
import org.apache.ibatis.type.MappedTypes;

import javax.persistence.Converter;

@MappedTypes(CompanyPayType.class)
@Converter
public class CompanyPayTypeAttributeConverter extends DatabaseCodeCommonTypeAttributeConverter<CompanyPayType> {

    public static final String ENUM_NAME = "결제방식";

    public CompanyPayTypeAttributeConverter() {
        super(ENUM_NAME, CompanyPayType.class);
    }
}

