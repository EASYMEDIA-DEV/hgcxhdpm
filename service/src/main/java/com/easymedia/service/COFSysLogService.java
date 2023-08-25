package com.easymedia.service;

import com.easymedia.dto.EmfMap;

/**
 * <pre> 
 * 로그관리(시스템)를 위한 Service
 * </pre>
 * 
 * @ClassName		: COFSysLogService.java
 * @Description		: 로그관리(시스템)를 위한 Service
 * @author 김진수
 * @since 2019.01.14
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		 since		  author			   description
 *    ==========    ==========    ==============================
 *    2019.01.14	  김진수			    최초 생성
 * </pre>
 */
public interface COFSysLogService {

	/**
	 * 로그 목록을 조회한다.
	 * 
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public EmfMap logSelectSysLogList(EmfMap emfMap) throws Exception;
	
	/**
	 * 로그 목록을 조회한다. (엑셀 다운로드)
	 * 
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public EmfMap logExcelSysLogList(EmfMap emfMap) throws Exception;
	
	/**
	 * 시스템 로그정보를 생성한다.
	 * 
	 * @param emfMap
	 * @return 
	 * @exception Exception
	 */
	public void logInsertSysLog(EmfMap emfMap) throws Exception;
	
	/**
	 * 시스템 에러 등록
	 * 
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public void logInsertErrLog(EmfMap emfMap) throws Exception;
}