package com.easymedia.service.dao;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre>
 * 로그인/로그아웃 DAO
 * </pre>
 *
 * @ClassName		: COBLgnDAO.java
 * @Description		: 로그인/로그아웃 DAO
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
@Mapper
public interface COBLgnDAO {

	/**
	 * 로그인을 처리한다
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public EmfMap actionLogin(EmfMap emfMap) throws Exception;

    /**
	 * 로그인 처리에 따른 시간을 업데이트한다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public int setLgnLstDtm(EmfMap emfMap) throws Exception;

    /**
   	 * 비밀번호를 변경한다.
   	 *
	 * @param emfMap
	 * @return
   	 * @exception Exception
   	 */
	public int setPwdChng(EmfMap emfMap) throws Exception;

	/**
	 * 로그인 처리에따른 메뉴를 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public List<EmfMap> getMenuList(EmfMap emfMap) throws Exception;

    /**
	 * 딜러 그룹 권한을 가진 관리자의 딜러 리스트
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public List<EmfMap> getAdminDealerGroupList(EmfMap emfMap) throws Exception;

    /**
	 * 관리자의 딜러 리스트(사장은 여러명 관리자, 사용자는 각 1명)
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public List<EmfMap> getAdminDealerAppvRelList(EmfMap emfMap) throws Exception;

    /**
	 * 상위 부모의 메뉴를 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public List<EmfMap> getParntMenuList(int pageNo) throws Exception;

    /**
	 * 로그인을 처리한다
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public EmfMap getAuthPddg(EmfMap emfMap) throws Exception;
    
    
    /**
	 * 마지막 접속 정보를 가져온다
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public EmfMap getLastLgnInfo(EmfMap emfMap) throws Exception;
    
    /**
	 * 로그인 실패 횟수 업데이트 
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public int updateLgnFail(EmfMap emfMap) throws Exception;
    
    /**
	 * 로그인 실패 횟수 초기화
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public int actionLgnFailReset(EmfMap emfMap) throws Exception;
    
    /**
	 * 로그인 실패 횟수 업데이트 
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public int resetPwd(EmfMap emfMap) throws Exception;
    
    /**
   	 * DB에 저장된 세션 초기화 
   	 *
   	 * @param emfMap
   	 * @return
   	 * @exception Exception
   	 */
    public int initSession(EmfMap emfMap) throws Exception;

	/**
	 * 비밀번호 초기화
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public int insertEmailReset(EmfMap emfMap) throws Exception;

	/**
	 * 이메일 초기화 정보 조회
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public EmfMap getEmailResetUuid(EmfMap emfMap) throws Exception;

	/**
	 * 이메일 초기화 사용
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public int updateEmailResetUuid(EmfMap emfMap) throws Exception;
}