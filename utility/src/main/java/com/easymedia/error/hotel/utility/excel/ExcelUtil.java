package com.easymedia.error.hotel.utility.excel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import com.easymedia.error.hotel.utility.BigDecimalUtil;

import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtil {

	private static String cellReader(Cell cell) {
		String value = "";
		CellType ct = cell.getCellType();
		if(ct != null) {
			switch(ct) {
			case FORMULA:
				value = cell.getCellFormula();
				break;
			case NUMERIC:
			    value=cell.getNumericCellValue()+"";
			    break;
			case STRING:
			    value=cell.getStringCellValue()+"";
			    break;
			case BOOLEAN:
			    value=cell.getBooleanCellValue()+"";
			    break;
			case ERROR:
			    value=cell.getErrorCellValue()+"";
			    break;
			}
		}
		return value;
	}

    public static String getString(Row row, int cellIndex) {
    	String value = cellReader(row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
    	return value;
//        return row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();

    }

    public static Integer getInteger(Row row, int cellIndex) {
    	String value = cellReader(row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
//        String value = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
        return StringUtils.isBlank(value) ? null : BigDecimalUtil.of(value, 0).intValue();
    }

    public static Long getLong(Row row, int cellIndex) {
    	String value = cellReader(row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
//        String value = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
        return StringUtils.isBlank(value) ? null : BigDecimalUtil.of(value, 0).longValue();
    }

    public static Float getFloat(Row row, int cellIndex) {
    	String value = cellReader(row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
//        String value = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
        return StringUtils.isBlank(value) ? null : BigDecimalUtil.of(value, 2).floatValue();
    }

    public static Boolean getBoolean(Row row, int cellIndex) {
    	String value = cellReader(row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
//        String value = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
        return BooleanUtils.toBooleanObject(StringUtils.strip(value));
    }

    public static LocalDate getLocalDate(Row row, int cellIndex) {
        LocalDateTime localDateTime = getLocalDateTime(row, cellIndex);
        return localDateTime == null ? null : localDateTime.toLocalDate();
    }

    public static LocalDateTime getLocalDateTime(Row row, int cellIndex) {
    	String value = cellReader(row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
//        String value = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
        value = RegExUtils.removeAll(value, "[\\D]");
        return StringUtils.isBlank(value) ? null
                : value.length() == 8 ? LocalDateTime.of(LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd")), LocalTime.of(0, 0))
                : value.length() == 10 ? LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyyMMddHH"))
                : value.length() == 12 ? LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
                : value.length() == 14 ? LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                : value.length() == 16 ? LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : null;
    }
}
