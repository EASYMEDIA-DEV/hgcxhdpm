package com.easymedia.service;

import com.easymedia.dto.EmfMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <pre>
 * 로그인/로그아웃 Service
 * </pre>
 *
 * @ClassName		: COBLgnService.java
 * @Description		: 로그인/로그아웃 Service
 * @author 허진영
 * @since 2019.01.15
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		 since			 author				   description
 *   ============    ==============    =============================
 *    2019.01.15	 	 허진영					최초 생성
 * </pre>
 */
public interface COBLgnService {

	/**
	 * 로그인을 처리한다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public EmfMap actionLogin(EmfMap emfMap, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
	 * 로그아웃을 처리한다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public void actionLogout(EmfMap emfMap, HttpServletRequest request) throws Exception;

    /**
     * 비밀번호를 변경한다.
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap updatePwd(EmfMap emfMap) throws Exception;
    
    /**
     * 비밀번호를 변경한다.2
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int resetPwd(EmfMap emfMap) throws Exception;
    
    /**
	 * 로그인 실패 처리
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public void actionLgnFail(EmfMap emfMap, HttpServletRequest request) throws Exception;
	
	/**
	 * 로그인 실패 카운트 초기화
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public void actionLgnFailReset(EmfMap emfMap, HttpServletRequest request) throws Exception;

    /**
	 * 메뉴 리스트를 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public List<EmfMap> getMenuList(EmfMap emfMap) throws Exception;

    /**
	 * 상위 부모의 메뉴를 가져온다.
	 *
	 * @param pageNo
	 * @return
	 * @exception Exception
	 */
    public List<EmfMap> getParntMenuList(int pageNo) throws Exception;

    /**
	 * 외부에서 접근하는 관리자 확인
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public EmfMap getAuthPddg(EmfMap emfMap) throws Exception;
    
    /**
	 * 마지막 접속 정보 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public EmfMap getLastLgnInfo(EmfMap emfMap, HttpServletRequest request) throws Exception;
    
    /**
	 * 비밀번호 초기화 메일 발송
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public EmfMap sendMailResetPwd(EmfMap emfMap) throws Exception;

	/**
	 *  비밀번호 초기화 UUID 조회
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public EmfMap getEmailResetUuid(EmfMap emfMap) throws Exception;
}