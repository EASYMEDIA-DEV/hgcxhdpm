/**
 *  Class Name : EgovNetworkState.java
 *  Description : 네트워크(Network)상태 체크 Business Interface class
 *  Modification Information
 *
 *     수정일         수정자                   수정내용
 *   -------    --------    ---------------------------
 *   2009.02.02    이 용          최초 생성
 *
 *  @author 공통 서비스 개발팀 이 용
 *  @since 2009. 02. 02
 *  @version 1.0
 *  @see
 * The type com.sun.star.lang.XeventListener cannot be resolved. It is indirectly referenced from required .class files
 *  Copyright (C) 2009 by EGOV  All right reserved.
 */

package com.easymedia.utility;

import javax.servlet.http.HttpServletRequest;
import java.io.File;


public class EgovNetworkState
{
	public static String addrIP = "";
	
    static final char FILE_SEPARATOR = File.separatorChar;
    
    // 최대 문자길이
    static final int MAX_STR_LEN = 1024;

	public static final int BUFF_SIZE = 2048;

	
	/**
     * <pre>
     * Comment : Local IPAddress를 확인한다.
     * </pre>
     * @return String mac Local IPAddress를 리턴한다.
     * @version 1.0 (2017.03.16.)
     * @see
     */
	public static String getMyIPaddress(HttpServletRequest request)
	{
		try
		{
			String loginIp = request.getHeader("X-Forwarded-For");

    		if(loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) 
    		{ 
    		    loginIp = request.getHeader("Proxy-Client-IP"); 
    		} 

    		if(loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) 
    		{ 
    			loginIp = request.getHeader("WL-Proxy-Client-IP"); 
    		} 

    		if(loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) 
    		{ 
    		    loginIp = request.getHeader("HTTP_CLIENT_IP"); 
    		} 

    		if(loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) 
    		{ 
    		    loginIp = request.getHeader("HTTP_X_FORWARDED_FOR"); 
    		} 

    		if(loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) 
    		{ 
    			loginIp = request.getRemoteAddr();
    		}
    		
    		addrIP = loginIp;
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);	// 2011.10.10 보안점검 후속조치
		}		
		
		return addrIP;
	}


	/**
	 * <pre>
	 * Comment : String 타입의 str값 중 숫자 정보만 필터링, 담아서 리턴.
	 * </pre>
     * @param str        필터링 대상 정보
	 * @return String outValue   숫자 정보를 필터링 리턴한다.
	 * @version 1.0 (2009.02.07.)
	 * @see
	 */
	private static String getCharFilter(String str)
	{
		String outValue = "";

		for (int i = 0; i < str.length(); i++) 
		{
			char c = str.charAt(i);

			if (c > 45 && c < 59) 
			{
				Character cr = new Character(c);
            
				outValue += cr.toString();
			}
		}
		
		return outValue;
	}
}