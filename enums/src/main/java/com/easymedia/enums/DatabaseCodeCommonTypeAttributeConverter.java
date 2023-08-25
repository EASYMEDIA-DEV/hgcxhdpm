package com.easymedia.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import javax.persistence.AttributeConverter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;

@Getter
@RequiredArgsConstructor
public class DatabaseCodeCommonTypeAttributeConverter<E extends Enum<E> & DatabaseCodeCommonType> implements AttributeConverter<E, String>, TypeHandler<E> {

    private final String enumName;
    private final Class<E> targetEnumClass;

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : attribute.getDatabaseCode();
    }

    @Override
    public E convertToEntityAttribute(String databaseCode) {
        if (StringUtils.isBlank(databaseCode)) {
            return null;
        }
        return EnumSet.allOf(targetEnumClass).stream()
                .filter(v -> v.getDatabaseCode().equals(databaseCode))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s[%s], code[%s]가 존재하지 않습니다.", enumName, targetEnumClass.getName(), databaseCode)));

    }

    @Override
    public void setParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getDatabaseCode());
    }

    @Override
    public E getResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return convertToEntityAttribute(code);
    }

    @Override
    public E getResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return convertToEntityAttribute(code);
    }

    @Override
    public E getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return convertToEntityAttribute(code);
    }
}
