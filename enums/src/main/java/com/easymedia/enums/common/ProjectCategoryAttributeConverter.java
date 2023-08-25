package com.easymedia.enums.common;

import com.easymedia.enums.DatabaseCodeCommonTypeAttributeConverter;
import org.apache.ibatis.type.MappedTypes;

import javax.persistence.Converter;

@MappedTypes(ProjectCategory.class)
@Converter
public class ProjectCategoryAttributeConverter extends DatabaseCodeCommonTypeAttributeConverter<ProjectCategory> {
    public static final String ENUM_NAME = "프로젝트 카테고리";

    public ProjectCategoryAttributeConverter() {
        super(ENUM_NAME, ProjectCategory.class);
    }
}
