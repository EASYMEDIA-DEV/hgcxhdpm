package com.easymedia.error.hotel.utility;

import java.time.Period;

public class PeriodUtil {
    public static String toString(Period period) {
        if (period == null) {
            return "";
        }
        String str = "";
        if (period.getYears() > 0) {
            str += period.getYears() + "년";
        }
        if (period.getMonths() > 0) {
            if (str.length() > 0) {
                str += " ";
            }
            str += period.getMonths() + "개월";
        }
        if (period.getDays() > 0) {
            if (str.length() > 0) {
                str += " ";
            }
            str += period.getDays() + "일";
        }
        return str;
    }

    public static String toString(String periodStr) {
        try {
            return toString(Period.parse(periodStr));
        } catch (Exception e) {
            return "";
        }
    }
}
