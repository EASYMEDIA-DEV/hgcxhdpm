package com.easymedia.service.dao;

import java.util.List;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;


/**
 * <pre> 
 * SBACountryDAO
 * </pre>
 * 
 * @ClassName		: SBACountryDAO.java
 * @Description		: 국가관리(지역본부) DAO
 * @author 안진용
 * @since 2019.01.09
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.01.09		  안진용					     최초생성
 * </pre>
 */
@Mapper
public interface SBACountryDAO {

	/**
	 * 국가관리  List
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public List<EmfMap> selectCountryList(EmfMap emfMap) throws Exception;

	/**
	 * 국가관리 Total Count
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public int selectCountryListTotCnt(EmfMap emfMap) throws Exception;

	/**
	 * 국가관리 등록된 국가가 있는지 조회
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public int getNtnCdCnt(EmfMap emfMap) throws Exception;

	/**
	 * 국가관리 Insert
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public int insertCountry(EmfMap emfMap) throws Exception;


	/**
	 * 해당 국가에 엮인 언어 delete
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public int deleteCountryLgug(EmfMap emfMap) throws Exception;


	/**
	 * 해당 국가에 엮인 언어 Insert
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public int insertCountryLgug(EmfMap emfMap) throws Exception;


	/**
	 * 국가관리 Update
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public int updateCountry(EmfMap emfMap) throws Exception;

	/**
	 * 국가관리 상세 조회
	 *
	 * @param emfMap 데이터
	 * @return EmfMap 조회조건에 검색된 데이터
	 */
	public EmfMap selectCountryDtl(EmfMap emfMap) throws Exception;

	/**
	 * 해당 국가에 엮인 언어 리스트 조회
	 *
	 * @param EmfMap emfMap
	 * @return EmfMap 조회조건에 검색된 데이터
	 */
	public List<EmfMap> selectCountryLgugList(EmfMap emfMap) throws Exception;

	/**
	 * 국가관리 > 등록된 리스트 조회
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public List<EmfMap> getCountryList(EmfMap emfMap) throws Exception;


	/**
	 * 국가관리 List ( 엑셀 다운로드 )
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public List<EmfMap> excelCountryList(EmfMap emfMap) throws Exception;

	/**
	 * 해당 국가에 엮인 AnnualTaget Insert
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public int insertCountryTarget(EmfMap emfMap) throws Exception;

	/**
	 * 해당 국가에 엮인 Annual Target 리스트 조회
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public List<EmfMap> selectTrgtList(EmfMap emfMap) throws Exception;

	/**
	 * AnnualTarget Update
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public int deleteTargetVal(EmfMap emfMap) throws Exception;
}