package com.easymedia.service.dao;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre>
 * Main DAO
 * </pre>
 *
 * @ClassName		: COAMainDAO.java
 * @Description		: Main DAO
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
@Mapper
public interface COAMainDAO {

	/**
	 * 딜러 사용자, 딜러 관리자용 딜러 정보
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getDlearDtl(EmfMap emfMap) throws Exception;

	/**
	 * 대리점 관리자 정보
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getDlrspDtl(EmfMap emfMap) throws Exception;
	
	/**
	 *  HGSI Status 조회 (권역)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getAllHgsiStatus(EmfMap emfMap) throws Exception;
	
	/**
	 * HGSI Status 조회 (나라)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnHgsiStatus(EmfMap emfMap) throws Exception;
	
	/**
	 * HGSI Status 조회 (대리점)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getDlrspHgsiStatus(EmfMap emfMap) throws Exception;

	/**
	 * HGSI Status 조회 (딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getHgsiStatus(EmfMap emfMap) throws Exception;
	
	/**
	 * HGSI Ranking 조회 (나라)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnHgsiRankingList(EmfMap emfMap) throws Exception;
	
	/**
	 * HGSI Ranking 조회 (대리점)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getDlrspHgsiRankingList(EmfMap emfMap) throws Exception;

	/**
	 * HGSI Ranking 조회 (딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getHgsiRankingList(EmfMap emfMap) throws Exception;

	/**
	 * HGSI Trend 조회 (권역, 국가)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnHgsiMonthList(EmfMap emfMap) throws Exception;

	/**
	 * HGSI Trend 조회 (대리점, 딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getHgsiMonthList(EmfMap emfMap) throws Exception;

	/**
	 * NPS Status 조회 (권역, 국가)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnNpsStatus(EmfMap emfMap) throws Exception;
	
	/**
	 * NPS Status 조회 (대리점, 딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNpsStatus(EmfMap emfMap) throws Exception;
	
	/**
	 * NPS Ranking 조회 (국가)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnNpsRankingList(EmfMap emfMap) throws Exception;
	
	/**
	 * NPS Ranking 조회 (대리점)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getDlrspNpsRankingList(EmfMap emfMap) throws Exception;

	/**
	 * NPS Ranking 조회 (딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNpsRankingList(EmfMap emfMap) throws Exception;
	
	/**
	 * NPS Trend 조회 (권역, 국가)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnNpsMonthList(EmfMap emfMap) throws Exception;

	/**
	 * NPS Trend 조회 (대리점, 딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNpsMonthList(EmfMap emfMap) throws Exception;
	
	/**
	 * Key Question Performance 조회 (권역, 국가)
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnKeyQuestionPerformanceList(EmfMap emfMap) throws Exception;

	/**
	 * Key Question Performance 조회 (대리점, 딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getKeyQuestionPerformanceList(EmfMap emfMap) throws Exception;
	
	/**
	 * Key Question Performance 추가 질문 조회 (권역, 국가)
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnKeyQuestionPerformanceAddResultList(EmfMap emfMap) throws Exception;
	
	/**
	 * Key Question Performance 추가 질문 조회 (대리점, 딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getKeyQuestionPerformanceAddResultList(EmfMap emfMap) throws Exception;
	
	/**
	 * Key Question Performance TestDrive(no) 추가 질문 조회 (권역, 국가)
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnKeyQuestionPerformanceTestDriveResultList(EmfMap emfMap) throws Exception;
	
	/**
	 * Key Question Performance TestDrive(no) 추가 질문 조회 (대리점, 딜러)
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getKeyQuestionPerformanceTestDriveResultList(EmfMap emfMap) throws Exception;
	
	/**
	 * Key Question Performance Trend - Latest 3 Months 조회 (권역, 국가)
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnKeyLatestQuestionPerformanceList(EmfMap emfMap) throws Exception;

	/**
	 * Key Question Performance Trend - Latest 3 Months 조회 (대리점, 딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getKeyLatestQuestionPerformanceList(EmfMap emfMap) throws Exception;
	
	/**
	 * Survey Response Status 조회 (권역, 나라)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnSurveyResponseStatusList(EmfMap emfMap) throws Exception;

	/**
	 * Survey Response Status 조회 (대리점, 딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getSurveyResponseStatusList(EmfMap emfMap) throws Exception;
	
	/**
	 * HGSI Feedback 조회 (나라)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnFeedbackList(EmfMap emfMap) throws Exception;
	
	/**
	 * HGSI Feedback 조회 (대리점)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getDlrspFeedbackList(EmfMap emfMap) throws Exception;
	
	/**
	 * HGSI Feedback 조회 (딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getFeedbackList(EmfMap emfMap) throws Exception;
	
	/**
	 * HGSI Feedback 조회 (고객)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getCstmrFeedbackList(EmfMap emfMap) throws Exception;
	
	/**
	 * Hot Alert - Latest 3 Months 조회 (나라)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnHotAlertList(EmfMap emfMap) throws Exception;
	
	/**
	 * Hot Alert - Latest 3 Months 조회 (대리점)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getDlrspHotAlertList(EmfMap emfMap) throws Exception;

	/**
	 * Hot Alert - Latest 3 Months 조회 (딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getHotAlertList(EmfMap emfMap) throws Exception;
	
	/**
	 * Hot Alert - Latest 3 Months 조회 (고객)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getCstmrHotAlertList(EmfMap emfMap) throws Exception;

	/**
	 * Hot Alert - Latest 3 Months 평균 처리일 조회 (고객)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getCstmrHotAlertAvgAcptHour(EmfMap emfMap) throws Exception;

	/**
	 * HGSI Action Planning - Latest 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getHgsiActPlngList(EmfMap emfMap) throws Exception;

	/**
	 * Retail Standard Check Action Planning - Latest 조회 (국가)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnRscActPlngList(EmfMap emfMap) throws Exception;

	/**
	 * Retail Standard Check Action Planning - Latest 조회 (대리점)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getDlrspRscActPlngList(EmfMap emfMap) throws Exception;
	
	/**
	 * Retail Standard Check Action Planning - Latest 조회 (딜러)
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getRscActPlngList(EmfMap emfMap) throws Exception;
	
	/**
	 * Retail Standard Check Action Planning - Latest 조회 (상세)
	 * 
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getRscActPlngDtl(EmfMap emfMap) throws Exception;
	
	/**
	 * Profitability Data Status - Latest 조회 (나라)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnProfitabilityDataList(EmfMap emfMap) throws Exception;
	
	/**
	 * Profitability Data Status - Latest 조회 (대리점)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getDlrspProfitabilityDataList(EmfMap emfMap) throws Exception;

	/**
	 * Profitability Data Status - Latest 조회 (딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getProfitabilityDataList(EmfMap emfMap) throws Exception;
	
	/**
	 * Profitability Data Status - Latest 조회 (상세)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getProfitabilityDataDtl(EmfMap emfMap) throws Exception;

	/**
	 * 대리점 구글 리뷰
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getGoogleRvwList(EmfMap emfMap) throws Exception;

	/**
	 * 대리점 페이스북 리뷰
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getFacebookRvwList(EmfMap emfMap) throws Exception;

	/**
	 * 지역 본부 관리자 > 모든 국가 구글 리뷰
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getGoogleNatnRvwList(EmfMap emfMap) throws Exception;
	
	/**
	 * 지역 본부 관리자 > 모든 국가 페이스북 리뷰
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getFacebookNatnRvwList(EmfMap emfMap) throws Exception;
	
	/**
	 * 지역 본부 관리자 > 선택한 국가 대리점 구글 리뷰
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getGoogleNatnDlrspRvwList(EmfMap emfMap) throws Exception;
	
	/**
	 * 지역 본부 관리자 > 선택한 국가 대리점 페이스북 리뷰
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getFacebookNatnDlrspRvwList(EmfMap emfMap) throws Exception;
	
	/**
	 * HGSI 평균 (나라 상위 - 권역)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getNatnParntHgsiAvgScrg(EmfMap emfMap) throws Exception;
	
	/**
	 * HGSI 평균 (대리점 상위 - 나라)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getDlrspParntHgsiAvgScrg(EmfMap emfMap) throws Exception;
	
	/**
	 * HGSI 평균 (딜러 상위 - 대리점)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getParntHgsiAvgScrg(EmfMap emfMap) throws Exception;
	
	/**
	 * HGSI 설문 항목
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getHgsiSrvyCol(EmfMap emfMap) throws Exception;
	
	/**
	 * Country Tracker (나라)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getNatnTrackerList(EmfMap emfMap) throws Exception;

	/**
	 * Distributor Tracker (대리점)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getDlrspTrackerList(EmfMap emfMap) throws Exception;
	
	/**
	 * Dealer Tracker (딜러)
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getTrackerList(EmfMap emfMap) throws Exception;
	
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
	 * Survey Response List Excel
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> selectSurveyListExcel(EmfMap emfMap) throws Exception;
	
	
	/**
	 * Survey Response List Excel
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> selectSurveyMonthlyListExcel(EmfMap emfMap) throws Exception;
	
	/**
	 * Annaul target 값 가져오기
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getAnnualTrgt(EmfMap emfMap) throws Exception;
	
	
	/**
	 * Annaul target 값 가져오기
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getDealerAnnualTrgt(EmfMap emfMap) throws Exception;
	
	
	/**
	 * kpi 항목 가져오기
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getKpiCdNmList(EmfMap emfMap) throws Exception;
	
	/**
	 * 권역별 수신율 데이터 패키징 프로시저 최근 이력 정보 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap selectDeliveredPkgInfo(EmfMap emfMap) throws Exception;
}