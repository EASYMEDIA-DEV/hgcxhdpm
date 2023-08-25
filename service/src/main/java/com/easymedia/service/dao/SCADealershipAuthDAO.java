package com.easymedia.service.dao;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre>
 * 대리점-계정관리 DAO
 * </pre>
 *
 * @ClassName		: SCADealershipAuthDAO.java
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
@Mapper
public interface SCADealershipAuthDAO  {

    /**
	 * Sample List
	 *
	 * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
	 */
	public List<EmfMap> selectDealershipList(EmfMap emfMap) throws Exception;

	/**
	 * Sample Total Count
	 *
	 * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
	 */
	public int selectDealershipListTotCnt(EmfMap emfMap) throws Exception;

	/**
     * Sample Details
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
    public EmfMap selectDealershipDtl(EmfMap emfMap) throws Exception;

    /**
	 * 대리점 조회
	 *
	 * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
	 */
	public List<EmfMap> getDealershipList(EmfMap emfMap) throws Exception;

	/**
     * ID 조회
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
     * Sample Insert
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
	public int insertDealershipAutherMst(EmfMap emfMap) throws Exception;

	/**
     * Sample Insert
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
	public int insertDealershipAutherDtl(EmfMap emfMap) throws Exception;

	/**
     * ID와 국가코드로 존재 확인
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
    public EmfMap getNtnDealershipAuth(EmfMap emfMap) throws Exception;

    /**
     * mst 엑셀 수정
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
	public int updateDealershipExcelAutherMst(EmfMap emfMap) throws Exception;

	/**
     * dtl 엑셀 수정
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
	public int updateDealershipExcelAutherDtl(EmfMap emfMap) throws Exception;

	 /**
     * mst 수정
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
	public int updateDealershipAutherMst(EmfMap emfMap) throws Exception;

	/**
     * dtl 수정
     *
     * @param EmfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
	public int updateDealershipAutherDtl(EmfMap emfMap) throws Exception;

	/**
     * 승인처리
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
	public List<EmfMap> excelDealershipList(EmfMap emfMap) throws Exception;
}