package com.easymedia.enums.menu;

import com.easymedia.enums.DatabaseCodeCommonTypeAttributeConverter;
import org.apache.ibatis.type.MappedTypes;

import javax.persistence.Converter;

@MappedTypes(Site.class)
@Converter
public class SiteAttributeConverter extends DatabaseCodeCommonTypeAttributeConverter<Site> {
    public static final String ENUM_NAME = "사이트 구분";

    public SiteAttributeConverter() {
        super(ENUM_NAME, Site.class);
    }
}

