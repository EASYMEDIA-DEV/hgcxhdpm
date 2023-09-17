package com.easymedia.service.dao;

import java.util.List;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;


/**
 * <pre> 
 * FRAFrptDAO
 * </pre>
 * 
 * @ClassName		: FRAFrptDAO.java
 * @Description		: Flexible Report DAO
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
public interface FRAFrptDAO  {
    
	/**
	 * 국가 리스트 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> selectFrptNatnList(EmfMap emfMap) throws Exception ;
	
	
	/**
	 * 대리점 리스트 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> selectFrptDealerShipList(EmfMap emfMap) throws Exception ;
	
	
	/**
	 * 딜러 리스트 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> selectFrptDealerList(EmfMap emfMap) throws Exception ;
	
	/**
	 * 딜러 리스트 총 건수
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int selectFrptDealerListTotCnt(EmfMap emfMap) throws Exception ;
	
	
    /**
	 * 딜러에 따른 HGSI 점수 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */	
	public List<EmfMap> selectFrptDealerHgsiScoreList(EmfMap emfMap) throws Exception ;
	
	
	
    /**
	 * 딜러에 따른 RSC 대 카테고리 준수율 조회(국가, 대리점 종합)
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */	
	public List<EmfMap> selectFrptNatnDealerRscScoreList(EmfMap emfMap) throws Exception;

	
	
	
	/**
	 * 딜러에 따른 RSC 대 카테고리 준수율 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */	
	public List<EmfMap> selectFrptDealerRscScoreList(EmfMap emfMap) throws Exception ;
	
	
	
    /**
	 * 딜러에 따른 RSC 문항별 답 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */	
	public List<EmfMap> selectFrptDealerRscScoreQstnList(EmfMap emfMap) throws Exception ;
	
	/**
	 * 딜러에 따른 MYS 대 카테고리 준수율 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */	
	public List<EmfMap> selectFrptDealerMysScoreList(EmfMap emfMap) throws Exception ;
	
	/**
	 * 딜러에 따른 MYS 문항별 답 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */	
	public List<EmfMap> selectFrptDealerMysScoreQstnList(EmfMap emfMap) throws Exception ;
	
	/**
	 * KPI 문항만 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */	
	public List<EmfMap> selectFrptKpiQstnList(EmfMap emfMap) throws Exception ;
	
	/**
	 * 딜러에 따른 KPI 문항별 결과값 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */	
	public List<EmfMap> selectFrptDealerKpiScoreQstnList(EmfMap emfMap) throws Exception ;

	/**
	 * Flexible Report FieldWork > 딜러가 관리하는 고객에 email,hp 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getDealerCustomerInfo(EmfMap emfMap) throws Exception ;

	/**
	 * Flexible Report FieldWork > 딜러가 관리하는 고객 상세 정보 조회(국가, 대리점 종합)
	 * 
	 * @param  emfMap
	 * @return EmfMap 조회조건에 검색된 데이터
	 * @throws Exception
	 */
	public EmfMap getNatnDealerCustomerDtlInfo(EmfMap emfMap) throws Exception;
	
	/**
	 * Flexible Report FieldWork > 딜러가 관리하는 고객 상세 정보 조회 (국가 or 대리점종합)
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */	
	public List<EmfMap> selectDealerCustomerList(EmfMap emfMap) throws Exception ;
	
	/**
	 * Flexible Report FieldWork > 딜러가 관리하는 고객 상세 정보 조회
	 * 
	 * @param  emfMap
	 * @return EmfMap 조회조건에 검색된 데이터
	 * @throws Exception
	 */
	public List<EmfMap> getDealerCustomerDtlInfo(EmfMap emfMap) throws Exception;
	
	/**
	 * Flexible Report Vin 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */	
	public List<EmfMap> selectFrptVinList(EmfMap emfMap) throws Exception ;
	
	/**
	 * HGSI 리스트 조회
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> selectHgsiList(EmfMap emfMap) throws Exception ;
}