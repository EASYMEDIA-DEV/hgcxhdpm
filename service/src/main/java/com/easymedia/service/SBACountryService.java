package com.easymedia.service;


import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;

/**
 * <pre> 
 * SBACountryService Service
 * </pre>
 * 
 * @ClassName		: SBACountryService.java
 * @Description		: 국가관리(지역본부) Service
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
public interface SBACountryService {
	
    /**
     * 국가관리 List
     * 
     * @param emfMap 데이터
	 * @return
     */
    public EmfMap selectCountryList(EmfMap emfMap) throws Exception;
    
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
     * 국가관리 Update
     * 
     * @param emfMap 데이터
	 * @return
     */
	public int updateCountry(EmfMap emfMap) throws Exception;    	
    
	/**
	 * 국가관리 상세
	 * 
	 * @param emfMap 데이터
	 * @return List<EmfMap> 조회조건에 검색된 데이터
	 */
	public EmfMap selectCountryDtl(EmfMap emfMap) throws Exception;
	
    /**
     * 국가관리 > 등록된 리스트 조회
     * 
     * @param emfMap 데이터
	 * @return
     */
    public EmfMap getCountryList(EmfMap emfMap) throws Exception;
    
    /**
     * 국가관리 List ( 엑셀 다운로드 )
     *
     * @param emfMap 데이터
     * @return
     */
    public EmfMap excelCountryList(EmfMap emfMap) throws Exception;
    	
    
    /**
     * Annual Target Update
     * 
     * @param emfMap 데이터
	 * @return
     */
	public int deleteTargetVal(EmfMap emfMap) throws Exception;	
}