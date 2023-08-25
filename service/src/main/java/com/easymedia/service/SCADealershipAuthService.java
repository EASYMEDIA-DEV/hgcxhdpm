package com.easymedia.service;

import com.easymedia.dto.EmfMap;
import org.springframework.web.multipart.MultipartHttpServletRequest;
/**
 * <pre>
 * 대리점-계정관리
 * </pre>
 *
 * @ClassName		: SCADealershipAuthService.java
 * @Description		: 대리점-계정관리
 * @author 박주석
 * @since 2019.01.11
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.01.11		  박주석					     최초생성
 * </pre>
 */
public interface SCADealershipAuthService {

    /**
     * Sample List
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
    public EmfMap selectDealershipList(EmfMap emfMap) throws Exception;

    /**
     * Sample Details
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
    public EmfMap selectDealershipDtl(EmfMap emfMap) throws Exception;

    /**
     * 관리자 ID 존재 확인
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
    public Integer getEmailExistenceCnt(EmfMap emfMap) throws Exception;

    /**
     * 관리자 휴대전화번호 확인
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
    public Integer getHpExistenceCnt(EmfMap emfMap) throws Exception;

	/**
     * Insert
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
	public int insertDealershipAuther(EmfMap emfMap) throws Exception;

	/**
     * Update
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
	public int updateDealershipAuther(EmfMap emfMap) throws Exception;

	/**
     * 계정 승인
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
	public int updateDealershipAppyn(EmfMap emfMap) throws Exception;

	/**
     * 비밀번호 초기화
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
	public int updateInitPwd(EmfMap emfMap) throws Exception;

	/**
     * 엑셀다운로드
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
	public EmfMap excelDealershipList(EmfMap emfMap) throws Exception;


	/**
     * 고객관리 파일 업로드
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
    public EmfMap setExcelFile(EmfMap emfMap, MultipartHttpServletRequest multiRequest) throws Exception;


    /**
     * 고객관리 파일 데이터 업로드
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
    public int insertExcelDataUpload(EmfMap emfMap) throws Exception;
}