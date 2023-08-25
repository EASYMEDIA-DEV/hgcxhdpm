package com.easymedia.common.utility;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <pre>
 * 문자열 처리 함수
 * </pre>
 *
 * @ClassName		: COStringUtil.java
 * @Description		: 문자열 처리 함수
 * @author 박주석
 * @since 2022.01.10
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		 since		  author	            description
 *    ==========    ==========    ==============================
 *    2022.01.10	  박주석	             최초 생성
 * </pre>
 */
public class COStringUtil {

    /**
     * 변수를 카멜표기법으로 변환
     * @param str
     * @return String
     * @exception Exception
     */
    public static String haeConvert2Camel(String str)
    {
        if ((str.indexOf('_') < 0) && (Character.isLowerCase(str.charAt(0))))
        {
            return str;
        }

        StringBuilder result = new StringBuilder();

        boolean nextUpper = false;

        int len = str.length();

        for (int i = 0; i < len; i++)
        {
            char currentChar = str.charAt(i);

            if (currentChar == '_')
            {
                nextUpper = true;
            }
            else if (nextUpper)
            {
                result.append(Character.toUpperCase(currentChar));
                nextUpper = false;
            }
            else
            {
                result.append(Character.toLowerCase(currentChar));
            }
        }
        return result.toString();
    }

    /**
     * 변수를 카멜표기법으로 변환
     * @param value
     * @return String
     * @exception Exception
     */
    public static String filePathBlackList(String value)
    {
        String returnValue = value;

        if (returnValue == null || returnValue.trim().equals(""))
        {
            return "";
        }

        returnValue = returnValue.replaceAll("\\.\\.", "");
        returnValue = returnValue.replaceAll("\\.\\.\\\\", "");

        return returnValue;
    }

    /**
     *<pre>
     * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
     * &#064;param src null값일 가능성이 있는 String 값.
     * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
     *</pre>
     */
    public static String nullConvert(Object src)
    {
        if (src != null && src instanceof BigDecimal)
        {
            return ((BigDecimal) src).toString();
        }
        else if(src != null && src instanceof Integer)
        {
            return ((Integer) src).toString();
        }

        if (src == null || src.equals("null"))
        {
            return "";
        }
        else
        {
            return ((String) src).trim();
        }
    }
    /**
     * <p>
     * String이 비었거나("") 혹은 null 인지 검증한다.
     * </p>
     *
     * <pre>
     *  StringUtil.isEmpty(null)      = true
     *  StringUtil.isEmpty("")        = true
     *  StringUtil.isEmpty(" ")       = false
     *  StringUtil.isEmpty("bob")     = false
     *  StringUtil.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param str - 체크 대상 스트링오브젝트이며 null을 허용함
     * @return <code>true</code> - 입력받은 String 이 빈 문자열 또는 null인 경우
     */
    public static boolean isEmpty(String str)
    {
        return str == null || str.length() == 0;
    }
    /**
     * <p>기준 문자열에 포함된 모든 대상 문자(char)를 제거한다.</p>
     *
     * <pre>
     * StringUtil.remove(null, *)       = null
     * StringUtil.remove("", *)         = ""
     * StringUtil.remove("queued", 'u') = "qeed"
     * StringUtil.remove("queued", 'z') = "queued"
     * </pre>
     *
     * @param str  입력받는 기준 문자열
     * @param remove  입력받는 문자열에서 제거할 대상 문자열
     * @return 제거대상 문자열이 제거된 입력문자열. 입력문자열이 null인 경우 출력문자열은 null
     */
    public static String remove(String str, char remove)
    {
        if (isEmpty(str) || str.indexOf(remove) == -1)
        {
            return str;
        }

        char[] chars = str.toCharArray();

        int pos = 0;

        for (int i = 0, len = chars.length; i < len; i++)
        {
            if (chars[i] != remove)
            {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }

    /**
     * <p>
     * String이 비었거나("") 혹은 null 인지 검증한다.
     * </p>
     *
     * <pre>
     *  StringUtil.isEmpty(null)      = true
     *  StringUtil.isEmpty("")        = true
     *  StringUtil.isEmpty(" ")       = false
     *  StringUtil.isEmpty("bob")     = false
     *  StringUtil.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param str1, str2 - 체크 대상 스트링오브젝트이며 null을 허용함
     * @return <code>true</code> - 입력받은 String 이 빈 문자열 또는 null인 경우
     */
    public static String nvl(String str1, String str2)
    {
        return isEmpty(str1) ? str2 : str1;
    }
    /**
     * <p>오라클의 decode 함수와 동일한 기능을 가진 메서드이다.
     * <code>sourStr</code>과 <code>compareStr</code>의 값이 같으면
     * <code>returStr</code>을 반환하며, 다르면  <code>defaultStr</code>을 반환한다.
     * </p>
     *
     * <pre>
     * StringUtil.decode(null, null, "foo", "bar")= "foo"
     * StringUtil.decode("", null, "foo", "bar") = "bar"
     * StringUtil.decode(null, "", "foo", "bar") = "bar"
     * StringUtil.decode("하이", "하이", null, "bar") = null
     * StringUtil.decode("하이", "하이  ", "foo", null) = null
     * StringUtil.decode("하이", "하이", "foo", "bar") = "foo"
     * StringUtil.decode("하이", "하이  ", "foo", "bar") = "bar"
     * </pre>
     *
     * @param sourceStr 비교할 문자열
     * @param compareStr 비교 대상 문자열
     * @param returnStr sourceStr와 compareStr의 값이 같을 때 반환할 문자열
     * @param defaultStr sourceStr와 compareStr의 값이 다를 때 반환할 문자열
     * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며,
     *         <br/>다르면 defaultStr을 반환한다.
     */
    public static String decode(String sourceStr, String compareStr, String returnStr, String defaultStr)
    {
        if (sourceStr == null && compareStr == null)
        {
            return returnStr;
        }
        if (sourceStr == null && compareStr != null)
        {
            return defaultStr;
        }
        if (sourceStr.trim().equals(compareStr))
        {
            return returnStr;
        }
        return defaultStr;
    }
    /**
     * @param source 원본
     * @param output 자르고 난 뒤 붙일 데이터
     * @param slength 자르려는 숫자
     * @return source를 slength만큼 자른뒤 outpu으로 더하고 반환
     */
    public static String tldCutString(String source, String output, String slength)
    {
        String returnVal = null;
        if (source != null)
        {
            if (source.length() > Integer.parseInt(slength))
            {
                returnVal = source.substring(0, Integer.parseInt(slength)) + output;
            }
            else
            {
                returnVal = source;
            }
        }
        return returnVal;
    }
    /**
     * 날짜형태의 String의 날짜 포맷 및 TimeZone을 변경해 주는 메서드
     *
     * @param  strSource       바꿀 날짜 String
     * @param  fromDateFormat  기존의 날짜 형태
     * @param  toDateFormat    원하는 날짜 형태
     * @param  strTimeZone     변경할 TimeZone(""이면 변경 안함)
     * @return  소스 String의 날짜 포맷을 변경한 String
     */
    public static String convertDate(String strSource, String fromDateFormat, String toDateFormat, String strTimeZone)
    {
        SimpleDateFormat simpledateformat = null;
        Date date = null;
        String _fromDateFormat = fromDateFormat;
        String _toDateFormat   = toDateFormat;
        if (COStringUtil.nullConvert(strSource).trim().equals(""))
        {
            return "";
        }
        if (COStringUtil.nullConvert(fromDateFormat).trim().equals(""))
        {
            _fromDateFormat = "yyyyMMddHHmmss"; // default값
        }
        if (COStringUtil.nullConvert(toDateFormat).trim().equals(""))
        {
            _toDateFormat = "yyyy-MM-dd HH:mm:ss"; // default값
        }
        try
        {
            simpledateformat = new SimpleDateFormat(_fromDateFormat, Locale.getDefault());
            date = simpledateformat.parse(strSource);
            if (!COStringUtil.nullConvert(strTimeZone).trim().equals(""))
            {
                simpledateformat.setTimeZone(TimeZone.getTimeZone(strTimeZone));
            }
            simpledateformat = new SimpleDateFormat(_toDateFormat, Locale.getDefault());
        }
        catch (ParseException exception)
        {
            throw new RuntimeException(exception);
        }
        return simpledateformat.format(date);
    }
}
