package com.easymedia.error.hotel.utility;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {

    public static BigDecimal of(String value) {
        return StringUtils.isBlank(value) ? BigDecimal.ZERO : new BigDecimal(value);
    }

    public static BigDecimal of(String value, int scale) {
        return of(value).setScale(scale, RoundingMode.DOWN);
    }

    public static BigDecimal of(Float value) {
        return value == null ? BigDecimal.ZERO : of(String.valueOf(value));
    }

    public static BigDecimal of(Float value, int scale) {
        return of(value).setScale(scale, RoundingMode.DOWN);
    }

    public static BigDecimal of(Long value) {
        return value == null ? BigDecimal.ZERO : of(String.valueOf(value));
    }

    public static BigDecimal of(Long value, int scale) {
        return of(value).setScale(scale, RoundingMode.DOWN);
    }

    public static BigDecimal of(Integer value) {
        return value == null ? BigDecimal.ZERO : of(String.valueOf(value));
    }

    public static BigDecimal of(Integer value, int scale) {
        return of(value).setScale(scale, RoundingMode.DOWN);
    }

    public static float plus(Float value1, Float value2) {
        return of(value1).add(of(value2)).floatValue();
    }

    public static float plus(Float value1, Float value2, int scale) {
        return of(value1).add(of(value2)).setScale(scale, RoundingMode.DOWN).floatValue();
    }

    public static float plus(Float value1, Long value2, int scale) {
        return of(value1).add(of(value2)).setScale(scale, RoundingMode.DOWN).floatValue();
    }

    public static float plus(Float value1, Float value2, Float value3, int scale) {
        return of(value1).add(of(value2)).add(of(value3)).setScale(scale, RoundingMode.DOWN).floatValue();
    }

    public static int plus(Integer value1, Integer value2) {
        return of(value1).add(of(value2)).intValue();
    }

    public static float minus(Float value1, Float value2) {
        return of(value1).subtract(of(value2)).floatValue();
    }

    public static float minus(Float value1, Float value2, int scale) {
        return of(value1).subtract(of(value2)).setScale(scale, RoundingMode.DOWN).floatValue();
    }

    public static float minus(Long value1, Float value2, int scale) {
        return of(value1).subtract(of(value2)).setScale(scale, RoundingMode.DOWN).floatValue();
    }

    public static int minus(Integer value1, Integer value2) {
        return of(value1).subtract(of(value2)).intValue();
    }

    public static float multiply(Float value1, Float value2) {
        return of(value1).multiply(of(value2)).floatValue();
    }

    public static float multiply(Float value1, Float value2, int scale) {
        return of(value1).multiply(of(value2)).setScale(scale, RoundingMode.DOWN).floatValue();
    }

    public static float multiply(Long value1, Float value2, int scale) {
        return of(value1).multiply(of(value2)).setScale(scale, RoundingMode.DOWN).floatValue();
    }

    public static BigDecimal negative(Float value, int scale) {
        return negative(value).setScale(scale, RoundingMode.DOWN);
    }

    public static BigDecimal negative(Float value) {
        return of(value).multiply(of(-1));
    }

    public static BigDecimal negative(Long value, int scale) {
        return negative(value).setScale(scale, RoundingMode.DOWN);
    }

    public static BigDecimal negative(Long value) {
        return of(value).multiply(of(-1));
    }


    /**
     * 나누기
     * @param value1
     * @param value2
     * @param scale
     * @return
     */
    public static float divide(Float value1, Float value2, int scale) {
        return of(value1).divide(of(value2), scale, RoundingMode.DOWN).floatValue();
    }

}
