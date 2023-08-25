package com.easymedia.service.dao;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre>
 * 관리자 계정 관리 DAO
 * </pre>
 *
 * @ClassName		: COCAdmDAO.java
 * @Description		: 관리자 계정 관리 DAO
 * @author 허진영
 * @since 2019.01.14
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.01.14		허진영					최초 생성
 * </pre>
 */
@Mapper
public interface COCAdmDAO {

    /**
	 * 관리자 계정 관리 List
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> selectAdmList(EmfMap emfMap) throws Exception;

	/**
	 * 관리자 강제 변경을 위한 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getAdmAllList(EmfMap emfMap) throws Exception;

	/**
	 * 관리자 계정 관리 Total Count
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int selectAdmListTotCnt(EmfMap emfMap) throws Exception;

	/**
     * 관리자 계정 관리 Details
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectAdmDtl(EmfMap emfMap) throws Exception;

    /**
     * 관리자 계정 관리 Insert
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public int insertAdm(EmfMap emfMap) throws Exception;

	/**
     * 관리자 계정 관리 Update
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public int updateAdm(EmfMap emfMap) throws Exception;

	/**
     * 관리자 계정 관리 Delete
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public int deleteAdm(EmfMap emfMap) throws Exception;

    /**
     * 관리자 계정 중복 Check
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public int getIdCheck(EmfMap emfMap) throws Exception;

	/**
     * 관리자 계정 메뉴 Insert
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public void insertAdmMenu(EmfMap emfMap) throws Exception;

	/**
     * 관리자 계정 메뉴 Delete
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public void deleteAdmMenu(EmfMap emfMap) throws Exception;
}