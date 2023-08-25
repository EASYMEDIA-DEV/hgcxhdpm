package com.easymedia.common.utility;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * Date 에 대한 Util 클래스
 * @author 공통서비스 개발팀 이중호
 * @since 2009.02.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.01  이중호          최초 생성
 *
 * </pre>
 */
public class CODateUtil {

	/**
	 * <p>yyyyMMdd 혹은 yyyy-MM-dd 형식의 날짜 문자열을 입력 받아 년, 월, 일을
	 * 증감한다. 년, 월, 일은 가감할 수를 의미하며, 음수를 입력할 경우 감한다.</p>
	 */
	public static String addYearMonthDay(String sDate, int year, int month, int day) 
	{
		String dateStr = validChkDate(sDate);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		try 
		{
			cal.setTime(sdf.parse(dateStr));
		} 
		catch (ParseException e) 
		{
			throw new IllegalArgumentException("Invalid date format: " + dateStr);
		}

		if (year != 0) 
		{
			cal.add(Calendar.YEAR, year);
		}
		
		if (month != 0) 
		{
			cal.add(Calendar.MONTH, month);
		}
		
		if (day != 0) 
		{
			cal.add(Calendar.DATE, day);
		}
		
		return sdf.format(cal.getTime());
	}

	/**
	 * <p>yyyyMMdd 혹은 yyyy-MM-dd 형식의 날짜 문자열을 입력 받아 년을
	 * 증감한다. <code>year</code>는 가감할 수를 의미하며, 음수를 입력할 경우 감한다.</p>
	 */
	public static String addYear(String dateStr, int year) 
	{
		return addYearMonthDay(dateStr, year, 0, 0);
	}

	/**
	 * <p>yyyyMMdd 혹은 yyyy-MM-dd 형식의 날짜 문자열을 입력 받아 월을
	 * 증감한다. <code>month</code>는 가감할 수를 의미하며, 음수를 입력할 경우 감한다.</p>
	 */
	public static String addMonth(String dateStr, int month) 
	{
		return addYearMonthDay(dateStr, 0, month, 0);
	}

	/**
	 * <p>yyyyMMdd 혹은 yyyy-MM-dd 형식의 날짜 문자열을 입력 받아 일(day)를
	 * 증감한다. <code>day</code>는 가감할 수를 의미하며, 음수를 입력할 경우 감한다.
	 * <br/><br/>
	 * 위에 정의된 addDays 메서드는 사용자가 ParseException을 반드시 처리해야 하는 불편함이
	 * 있기 때문에 추가된 메서드이다.</p>
	 */
	public static String addDay(String dateStr, int day) 
	{
		return addYearMonthDay(dateStr, 0, 0, day);
	}
	
	public static String addDay(String dateStr, String day) 
	{
        return addYearMonthDay(dateStr, 0, 0, Integer.parseInt(day));
    }

	/**
	 * <p>yyyyMMdd 혹은 yyyy-MM-dd 형식의 날짜 문자열 <code>dateStr1</code>과 <code>
	 * dateStr2</code> 사이의 일 수를 구한다.<br>
	 * <code>dateStr2</code>가 <code>dateStr1</code> 보다 과거 날짜일 경우에는
	 * 음수를 반환한다. 동일한 경우에는 0을 반환한다.</p>
	 */
	public static int getDaysDiff(String sDate1, String sDate2) 
	{
		String dateStr1 = validChkDate(sDate1);
		String dateStr2 = validChkDate(sDate2);
		if (!checkDate(sDate1) || !checkDate(sDate2)) 
		{
			throw new IllegalArgumentException("Invalid date format: args[0]=" + sDate1 + " args[1]=" + sDate2);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		Date date1 = null;
		Date date2 = null;
		try 
		{
			date1 = sdf.parse(dateStr1);
			date2 = sdf.parse(dateStr2);
		}
		catch (ParseException e) 
		{
			throw new IllegalArgumentException("Invalid date format: args[0]=" + dateStr1 + " args[1]=" + dateStr2);
		}
		if (date1 != null && date2 != null) 
		{
			int days1 = (int) ((date1.getTime() / 3600000) / 24);
			int days2 = (int) ((date2.getTime() / 3600000) / 24);
			return days2 - days1;
		} 
		else 
		{
			return 0;
		}
	}

	/**
	 * <p>yyyyMMdd 혹은 yyyy-MM-dd 형식의 날짜 문자열을 입력 받아 유효한 날짜인지 검사.</p>
	 */
	public static boolean checkDate(String sDate) 
	{
		String dateStr = validChkDate(sDate);
		String year = dateStr.substring(0, 4);
		String month = dateStr.substring(4, 6);
		String day = dateStr.substring(6);
		return checkDate(year, month, day);
	}

	/**
	 * <p>입력한 년, 월, 일이 유효한지 검사.</p>
	 */
	public static boolean checkDate(String year, String month, String day) 
	{
		try 
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
			Date result = formatter.parse(year + "." + month + "." + day);
			String resultStr = formatter.format(result);
			if (resultStr.equalsIgnoreCase(year + "." + month + "." + day))
			{
				return true;
			}
			else
			{
				return false;
			}
		} 
		catch (ParseException e) 
		{
			return false;
		}
	}

	/**
	 * 날짜형태의 String의 날짜 포맷 및 TimeZone을 변경해 주는 메서드
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

	/**
	 * yyyyMMdd 형식의 날짜문자열을 원하는 캐릭터(ch)로 쪼개 돌려준다<br/>
	 */
	public static String formatDate(String sDate, String ch) 
	{
		String dateStr = validChkDate(sDate);
		String str = dateStr.trim();
		String yyyy = "", mm = "", dd = "";
		if (str.length() == 8) 
		{
			yyyy = str.substring(0, 4);
			if (yyyy.equals("0000")) 
			{
				return "";
			}

			mm = str.substring(4, 6);
			
			if (mm.equals("00")) 
			{
				return yyyy;
			}

			dd = str.substring(6, 8);
			
			if (dd.equals("00")) 
			{
				return yyyy + ch + mm;
			}
			
			return yyyy + ch + mm + ch + dd;
		} 
		else if (str.length() == 6) 
		{
			yyyy = str.substring(0, 4);
		
			if (yyyy.equals("0000")) 
			{
				return "";
			}

			mm = str.substring(4, 6);
			
			if (mm.equals("00")) 
			{
				return yyyy;
			}

			return yyyy + ch + mm;
		} 
		else if (str.length() == 4) 
		{
			yyyy = str.substring(0, 4);
		
			if (yyyy.equals("0000")) 
			{
				return "";
			} 
			else 
			{
				return yyyy;
			}
		} 
		else 
		{
			return "";
		}
	}

	/**
	 * HH24MISS 형식의 시간문자열을 원하는 캐릭터(ch)로 쪼개 돌려준다
	 */
	public static String formatTime(String sTime, String ch) 
	{
		String timeStr = validChkTime(sTime);
		return timeStr.substring(0, 2) + ch + timeStr.substring(2, 4) + ch + timeStr.substring(4, 6);
	}
	/**
	 * 연도를 입력 받아 해당 연도 2월의 말일(일수)를 문자열로 반환한다.
	 */
	public String leapYear(int year) 
	{
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
		{
			return "29";
		}
		return "28";
	}

	/**
	 * <p>입력받은 연도가 윤년인지 아닌지 검사한다.</p>
	 */
	public static boolean isLeapYear(int year) 
	{
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) 
		{
			return false;
		}
		return true;
	}

	/**
	 * 현재(한국기준) 날짜정보를 얻는다.
	 */
	public static String getToday() 
	{
		return getCurrentDate("");
	}
	/**
     * 현재(한국기준) 날짜정보를 얻는다.
     */
    public static String getToday(String dateType)
    {
    	Calendar aCalendar = Calendar.getInstance();
        int year = aCalendar.get(Calendar.YEAR);
        int month = aCalendar.get(Calendar.MONTH) + 1;
        int date = aCalendar.get(Calendar.DATE);
        String strDate = Integer.toString(year) + ((month<10) ? "0" + Integer.toString(month) : Integer.toString(month)) + ((date<10) ? "0" + Integer.toString(date) : Integer.toString(date));
        if (dateType.length() == 12)
        {
        	int hour = aCalendar.get(Calendar.HOUR_OF_DAY);
        	int min  = aCalendar.get(Calendar.MINUTE);
        	strDate = strDate + ((hour<10) ? "0" + Integer.toString(hour) : Integer.toString(hour)) + ((min<10) ? "0" + Integer.toString(min) : Integer.toString(min));
        }
        return strDate;
    }
	/**
	 * 현재(한국기준) 날짜정보를 얻는다.
	 */
	public static String getCurrentDate(String dateType) 
	{
		Calendar aCalendar = Calendar.getInstance();
		int year = aCalendar.get(Calendar.YEAR);
		int month = aCalendar.get(Calendar.MONTH) + 1;
		int date = aCalendar.get(Calendar.DATE);
		String strDate = Integer.toString(year) + ((month < 10) ? "0" + Integer.toString(month) : Integer.toString(month)) + ((date < 10) ? "0" + Integer.toString(date) : Integer.toString(date));
		if (!"".equals(dateType))
		{
			strDate = convertDate(strDate, "0000", dateType);
		}
		return strDate;
	}

	/**
	 * 날짜형태의 String의 날짜 포맷만을 변경해 주는 메서드
	 */
	public static String convertDate(String sDate, String sTime, String sFormatStr) 
	{
		String dateStr = validChkDate(sDate);
		String timeStr = validChkTime(sTime);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr.substring(6, 8)));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr.substring(0, 2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(timeStr.substring(2, 4)));
		SimpleDateFormat sdf = new SimpleDateFormat(sFormatStr, Locale.ENGLISH);
		return sdf.format(cal.getTime());
	}

	/**
	 * 입력받은 일자 사이의 임의의 일자를 반환
	 */
	public static String getRandomDate(String sDate1, String sDate2) 
	{
		String dateStr1 = validChkDate(sDate1);
		String dateStr2 = validChkDate(sDate2);
		String randomDate = null;
		int sYear, sMonth, sDay;
		int eYear, eMonth, eDay;
		sYear = Integer.parseInt(dateStr1.substring(0, 4));
		sMonth = Integer.parseInt(dateStr1.substring(4, 6));
		sDay = Integer.parseInt(dateStr1.substring(6, 8));
		eYear = Integer.parseInt(dateStr2.substring(0, 4));
		eMonth = Integer.parseInt(dateStr2.substring(4, 6));
		eDay = Integer.parseInt(dateStr2.substring(6, 8));
		GregorianCalendar beginDate = new GregorianCalendar(sYear, sMonth - 1, sDay, 0, 0);
		GregorianCalendar endDate = new GregorianCalendar(eYear, eMonth - 1, eDay, 23, 59);
		if (endDate.getTimeInMillis() < beginDate.getTimeInMillis()) 
		{
			throw new IllegalArgumentException("Invalid input date : " + sDate1 + "~" + sDate2);
		}
		SecureRandom r = new SecureRandom();
		r.setSeed(new Date().getTime());
		long rand = ((r.nextLong() >>> 1) % (endDate.getTimeInMillis() - beginDate.getTimeInMillis() + 1)) + beginDate.getTimeInMillis();
		GregorianCalendar cal = new GregorianCalendar();
		SimpleDateFormat calformat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
		cal.setTimeInMillis(rand);
		randomDate = calformat.format(cal.getTime());
		return randomDate;
	}

	/**
	 * 입력받은 요일의 영문명을 국문명의 요일로 반환
	 */
	public static String convertWeek(String sWeek) 
	{
		String retStr = null;
		if (sWeek.equals("SUN")) 
		{
			retStr = "일요일";
		}
		else if (sWeek.equals("MON")) 
		{
			retStr = "월요일";
		}
		else if (sWeek.equals("TUE")) 
		{
			retStr = "화요일";
		} 
		else if (sWeek.equals("WED")) 
		{
			retStr = "수요일";
		}
		else if (sWeek.equals("THR")) 
		{
			retStr = "목요일";
		}
		else if (sWeek.equals("FRI")) 
		{
			retStr = "금요일";
		}
		else if (sWeek.equals("SAT")) 
		{
			retStr = "토요일";
		}
		return retStr;
	}

	/**
	 * 입력일자의 유효 여부를 확인
	 */
	public static boolean validDate(String sDate) 
	{
		String dateStr = validChkDate(sDate);
		Calendar cal;
		boolean ret = false;
		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr.substring(6, 8)));
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String pad4Str = "0000";
		String pad2Str = "00";
		String retYear = (pad4Str + year).substring(year.length());
		String retMonth = (pad2Str + month).substring(month.length());
		String retDay = (pad2Str + day).substring(day.length());
		String retYMD = retYear + retMonth + retDay;
		if (sDate.equals(retYMD)) 
		{
			ret = true;
		}
		return ret;
	}

	/**
	 * 입력일자, 요일의 유효 여부를 확인
	 */
	public static boolean validDate(String sDate, int sWeek)
	{
		String dateStr = validChkDate(sDate);
		Calendar cal;
		boolean ret = false;
		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr.substring(6, 8)));
		int Week = cal.get(Calendar.DAY_OF_WEEK);
		if (validDate(sDate)) 
		{
			if (sWeek == Week) 
			{
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * 입력시간의 유효 여부를 확인
	 */
	public static boolean validTime(String sTime) 
	{
		String timeStr = validChkTime(sTime);
		Calendar cal;
		boolean ret = false;
		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr.substring(0, 2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(timeStr.substring(2, 4)));
		String HH = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		String MM = String.valueOf(cal.get(Calendar.MINUTE));
		String pad2Str = "00";
		String retHH = (pad2Str + HH).substring(HH.length());
		String retMM = (pad2Str + MM).substring(MM.length());
		String retTime = retHH + retMM;
		if (sTime.equals(retTime))
		{
			ret = true;
		}
		return ret;
	}

	/**
	 * 입력된 일자에 연, 월, 일을 가감한 날짜의 요일을 반환
	 */
	public static String addYMDtoWeek(String sDate, int year, int month, int day)
	{
		String dateStr = validChkDate(sDate);
		dateStr = addYearMonthDay(dateStr, year, month, day);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
		try 
		{
			cal.setTime(sdf.parse(dateStr));
		}
		catch (ParseException e)
		{
			throw new IllegalArgumentException("Invalid date format: " + dateStr);
		}
		SimpleDateFormat rsdf = new SimpleDateFormat("E", Locale.ENGLISH);
		return rsdf.format(cal.getTime());
	}

	/**
	 * 입력된 일자에 연, 월, 일, 시간, 분을 가감한 날짜, 시간을 포멧스트링 형식으로 반환
	 */
	public static String addYMDtoDayTime(String sDate, String sTime, int year, int month, int day, int hour, int minute, String formatStr) 
	{
		String dateStr = validChkDate(sDate);
		String timeStr = validChkTime(sTime);
		dateStr = addYearMonthDay(dateStr, year, month, day);
		dateStr = convertDate(dateStr, timeStr, "yyyyMMddHHmm");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.ENGLISH);
		try 
		{
			cal.setTime(sdf.parse(dateStr));
		} 
		catch (ParseException e) 
		{
			throw new IllegalArgumentException("Invalid date format: " + dateStr);
		}
		if (hour != 0) 
		{
			cal.add(Calendar.HOUR, hour);
		}
		if (minute != 0) 
		{
			cal.add(Calendar.MINUTE, minute);
		}
		SimpleDateFormat rsdf = new SimpleDateFormat(formatStr, Locale.ENGLISH);
		return rsdf.format(cal.getTime());
	}

	/**
	 * 입력된 일자를 int 형으로 반환
	 * @param sDate 일자
	 * @return int(일자)
	 */
	public static int datetoInt(String sDate) 
	{
		return Integer.parseInt(convertDate(sDate, "0000", "yyyyMMdd"));
	}

	/**
	 * 입력된 시간을 int 형으로 반환
	 * @param sTime 시간
	 * @return int(시간)
	 */
	public static int timetoInt(String sTime)
	{
		return Integer.parseInt(convertDate("00000101", sTime, "HHmm"));
	}

	/**
	 * 입력된 일자 문자열을 확인하고 8자리로 리턴
	 */
	public static String validChkDate(String dateStr)
	{
		if (dateStr == null || !(dateStr.trim().length() == 8 || dateStr.trim().length() == 10)) 
		{
			throw new IllegalArgumentException("Invalid date format: " + dateStr);
		}
		if (dateStr.length() == 10) 
		{
			return COStringUtil.remove(dateStr, '-');
		}
		return dateStr;
	}

	/**
	 * 입력된 일자 문자열을 확인하고 8자리로 리턴
	 */
	public static String validChkTime(String timeStr) 
	{
		if (timeStr == null || !(timeStr.trim().length() == 4)) 
		{
			throw new IllegalArgumentException("Invalid time format: " + timeStr);
		}
		if (timeStr.length() == 5) 
		{
			timeStr = COStringUtil.remove(timeStr, ':');
		}
		return timeStr;
	}
}