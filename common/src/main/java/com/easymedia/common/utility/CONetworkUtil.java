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

package com.easymedia.common.utility;

import javax.servlet.http.HttpServletRequest;

public class CONetworkUtil {
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
		String loginIp = "";
		try
		{
			loginIp = COWebUtil.removeCRLF(request.getHeader("X-Forwarded-For"));
    		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp))
    		{
    		    loginIp = COWebUtil.removeCRLF(request.getHeader("Proxy-Client-IP"));
    		}
    		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp))
    		{
    			loginIp = COWebUtil.removeCRLF(request.getHeader("WL-Proxy-Client-IP"));
    		}
    		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp))
    		{
    		    loginIp = COWebUtil.removeCRLF(request.getHeader("HTTP_CLIENT_IP"));
    		}
    		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp))
    		{
    		    loginIp = COWebUtil.removeCRLF(request.getHeader("HTTP_X_FORWARDED_FOR"));
    		}
    		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp))
    		{
    			loginIp = COWebUtil.removeCRLF(request.getRemoteAddr());
    		}
    		if (loginIp != null && !"".equals(loginIp))
    		{

    		}
    		else
            {
				loginIp = "0.0.0.0";
            }
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
		return loginIp;
	}
}