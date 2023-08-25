package com.easymedia.service.dao;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre> 
 * 로그관리(시스템)를 위한 DAO
 * </pre>
 * 
 * @ClassName		: COFSysLogDAO.java
 * @Description		: 로그관리(시스템)를 위한 DAO
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
@Mapper
public interface COFSysLogDAO {

	/**
	 * 로그 목록을 조회한다.
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception 
	 */
	public List<EmfMap> logSelectSysLogList(EmfMap emfMap) throws Exception;
	
	/**
	 * 로그 목록의 총 갯수를 조회한다.
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int logSelectSysLogListTotCnt(EmfMap emfMap) throws Exception ;
	
	/**
	 * 로그 목록을 조회한다. (엑셀 다운로드)
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception 
	 */
	public List<EmfMap> logExcelSysLogList(EmfMap emfMap) throws Exception;
	
	/**
	 * 시스템 로그정보를 생성한다.
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception 
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