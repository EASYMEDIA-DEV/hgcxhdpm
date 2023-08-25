package com.easymedia.service;

import com.easymedia.dto.EmfMap;
import com.easymedia.dto.login.LoginUser;

/**
 * EgovUserDetails Helper 클래스
 * 
 * @author sjyoon
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    -------------    ----------------------
 *   2009.03.10  sjyoon    최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */

public class EgovUserDetailsHelper 
{
	/**
	 * 인증된 사용자객체를 VO형식으로 가져온다.
	 * @return Object - 사용자 ValueObject
	 */
	public static EmfMap getAuthenticatedUser()
	{
		EmfMap emfMap = new EmfMap();
		LoginUser loginUser = AuthChecker.getLoginUser();

		//
		emfMap.put("admSeq", loginUser.getAdmSeq());
		emfMap.put("id", loginUser.getMemberId());
		emfMap.put("loginIp", loginUser.getLoginIp());

		return emfMap;
	}
	
	/**
	 * 인증된 사용자 여부를 체크한다.
	 * @return Boolean - 인증된 사용자 여부(TRUE / FALSE)	
	 */
	public static Boolean isAuthenticated() {
		boolean isAuth = false;
		try
		{
			LoginUser loginUser = AuthChecker.getLoginUser();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
}