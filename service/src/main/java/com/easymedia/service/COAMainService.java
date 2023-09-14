package com.easymedia.service;

import com.easymedia.dto.EmfMap;

import java.util.List;

/**
 * <pre>
 * COAMainService
 * </pre>
 *
 * @ClassName		: COASampleService.java
 * @Description		: Sample Service
 * @author 허진영
 * @since 2019.02.14
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.02.14		  허진영					     최초생성
 * </pre>
 */
public interface COAMainService {

    /**
     * 메인 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectMainDataDtl(EmfMap emfMap) throws Exception;

    /**
     * HGSI Status 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap getHgsiStatus(EmfMap emfMap) throws Exception;
    
    /**
     * HGSI Ranking 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap getHgsiRankingList(EmfMap emfMap) throws Exception;
    
    /**
	 * HGSI Trend 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getHgsiMonthList(EmfMap emfMap) throws Exception;

    /**
     * NPS Status 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap getNpsStatus(EmfMap emfMap) throws Exception;

    /**
     * NPS Ranking 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap getNpsRankingList(EmfMap emfMap) throws Exception;

	/**
	 * NPS Trend 조회 
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getNpsMonthList(EmfMap emfMap) throws Exception;

	/**
	 * Key Question Performance 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getKeyQuestionPerformanceList(EmfMap emfMap) throws Exception;
	
	/**
     * Key Question Performance 추가 질문 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap getKeyQuestionPerformanceAddResultList(EmfMap emfMap) throws Exception;

	/**
	 * Key Question Performance Trend (Latest 3 Months) 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getKeyLatestQuestionPerformanceList(EmfMap emfMap) throws Exception;

	/**
	 * Survey Response Status 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getSurveyResponseStatusList(EmfMap emfMap) throws Exception;

	/**
	 * HGSI Feedback 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getFeedbackList(EmfMap emfMap) throws Exception;

	/**
	 * Hot Alert - Latest 3 Months 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getHotAlertList(EmfMap emfMap) throws Exception;

	/**
	 * HGSI Action Planning - Latest 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getHgsiActPlngList(EmfMap emfMap) throws Exception;

	/**
	 * Retail Standard Check Action Planning - Latest 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getRscActPlngList(EmfMap emfMap) throws Exception;

	/**
	 * Profitability Data 현황 (LATEST)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getProfitabilityDataList(EmfMap emfMap) throws Exception;

	/**
	 * 대리점 구글 리뷰
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getGoogleRvwList(EmfMap emfMap) throws Exception;

	/**
	 * 대리점 페이스북 리뷰
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getFacebookRvwList(EmfMap emfMap) throws Exception;

	/**
	 * Tracker 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getTrackerList(EmfMap emfMap) throws Exception;
	
	/**
	 * 국가 설정 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getNatnConfig(String natnCd) throws Exception;
	
	/**
	 * 딜러명 or 딜러코드(현대딜러코드)로 검색하기 위한 모든 딜러List
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getSearchDlrList(EmfMap emfMap) throws Exception;
	
	 /**
     * Flexible Report RSC List Excel
     * 
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap getFrptRscListExcel(EmfMap emfMap) throws Exception;    
    
    /**
     * Flexible Report Dealer KPI List Excel
     * 
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectFrptKpiExcel(EmfMap emfMap) throws Exception;
    
    
    /**
     * Flexible Report Dealer Survey Response List Excel
     * 
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectFrptSurveyExcel(EmfMap emfMap) throws Exception;
    
    
    public String convertDate(String date, boolean start);
    
    /**
     * Flexible Report Survey Response Monthly List Excel
     * 
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public EmfMap selectSurveyMonthlyExcel(EmfMap emfMap)throws Exception;
}