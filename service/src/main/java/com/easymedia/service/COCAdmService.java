package com.easymedia.service;

import com.easymedia.dto.EmfMap;

/**
 * <pre>
 * 관리자 계정 관리 Service
 * </pre>
 *
 * @ClassName		: COCAdmService.java
 * @Description		: 관리자 계정 관리 Service
 * @author 허진영
 * @since 2019.01.14
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.01.14		허진영				    최초 생성
 * </pre>
 */
public interface COCAdmService {

    /**
     * 관리자 계정 관리 List
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectAdmList(EmfMap emfMap) throws Exception;

    /**
     * 관리자 강제 변경을 위한 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap getAdmAllList(EmfMap emfMap) throws Exception;

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
     * 관리자를 계정 관리 Update (내 정보변경)
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int updatePrsnData(EmfMap emfMap) throws Exception;

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
}