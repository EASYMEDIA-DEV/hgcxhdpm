package com.easymedia.service.impl;

import com.easymedia.dto.EmfMap;
import com.easymedia.service.COAMainService;
import com.easymedia.service.dao.COAMainDAO;
import com.easymedia.service.dao.FRAFrptDAO;
import com.easymedia.utility.EgovDateUtil;
import com.easymedia.utility.EgovStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <pre>
 * COAMainServiceImpl
 * </pre>
 *
 * @ClassName		: COAMainServiceImpl.java
 * @Description		: 메인
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
@Slf4j
@Service
@RequiredArgsConstructor
public class COAMainServiceImpl implements COAMainService {

	// TODO 수정 필요
    //private final RCARscService rCARscService;

    private final COAMainDAO cOAMainDAO;

    private final FRAFrptDAO fRAFrptDAO;

	/**
     * 메인 딜러 및 대리점 정보 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectMainDataDtl(EmfMap emfMap) throws Exception
    {
    	String searchScope = emfMap.getString("searchScope");
    	
    	if ("dlr".equals(searchScope))
		{
			//딜러 정보 조회
    		//페이스북, 구글 비지니스 조회
			emfMap.put("infoMap", cOAMainDAO.getDlearDtl(emfMap));
		}
		else if ("dlrsp".equals(searchScope))
		{
			//대리점 정보 조회
			emfMap.put("infoMap", cOAMainDAO.getDlrspDtl(emfMap));
			
			//딜러 사장 접근 시, 메인 상단에 해당 딜러명 노출을 위함
			List<String> dlrCdList = (List<String>) emfMap.get("admDlrCdList");
			
			if (dlrCdList.size() > 0)
			{
				emfMap.put("dlrInfoMap", cOAMainDAO.getDlearDtl(emfMap));
			}
		}
    	
    	return emfMap;
    }
    
    /**
     * HGSI Status 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap getHgsiStatus(EmfMap emfMap) throws Exception
    {
    	String searchScope = emfMap.getString("searchScope");
    	String trgtVal = "";
    	String dealerTrgtVal = "";
    	
    	// annual target 설정 여부 확인 및 값 가져오기
    	if (!"all".equals(searchScope)){
    		emfMap.put("annualTrgtTypeCd", "H");
    		trgtVal = cOAMainDAO.getAnnualTrgt(emfMap).getString("trgtVal");
    		dealerTrgtVal = cOAMainDAO.getDealerAnnualTrgt(emfMap).getString("trgtVal");
    		
    		if(!"".equals(trgtVal)){
    			emfMap.put("trgtYN", "Y");
    		}else{
    			emfMap.put("trgtYN", "N");
    		}
    		
    		if(!"".equals(dealerTrgtVal)){
    			emfMap.put("trgtDealerYN", "Y");
    		}else{
    			emfMap.put("trgtDealerYN", "N");
    		}
    		
    		emfMap.put("trgtVal",trgtVal);
    		emfMap.put("dealerTrgtVal",dealerTrgtVal);
    	}
    	
    	if ("all".equals(searchScope))
    	{
    		emfMap.put("list", cOAMainDAO.getAllHgsiStatus(emfMap));
    	}
    	else if ("natn".equals(searchScope))
    	{
    		emfMap.put("list", cOAMainDAO.getNatnHgsiStatus(emfMap));
    	}
    	else if ("dlrsp".equals(searchScope))
    	{
    		emfMap.put("list", cOAMainDAO.getDlrspHgsiStatus(emfMap));
    	}
    	else
    	{
    		emfMap.put("list", cOAMainDAO.getHgsiStatus(emfMap));
    	}
    	
    	return emfMap;
    }

    /**
     * HGSI Ranking 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap getHgsiRankingList(EmfMap emfMap) throws Exception
    {
    	String searchScope = emfMap.getString("searchScope");
    	List<EmfMap> list = new ArrayList<EmfMap>();
    	List<EmfMap> list2 = new ArrayList<EmfMap>();

    	if ("all".equals(searchScope))
    	{
    		// 국가 순위
        	//emfMap.put("rnkngList", cOAMainDAO.getNatnHgsiRankingList(emfMap));
    		list = cOAMainDAO.getNatnHgsiRankingList(emfMap);
    	}
    	else if ("dlrsp".equals(searchScope))
    	{
    		// 대리점 순위
        	//emfMap.put("rnkngList", cOAMainDAO.getDlrspHgsiRankingList(emfMap));
    		list = cOAMainDAO.getDlrspHgsiRankingList(emfMap);
    		list2 = cOAMainDAO.getHgsiRankingList(emfMap);
    		
    		if(list2.size()>0){
    			for (int i = 0; i < list2.size(); i++) {
    				list.add(list2.get(i));
    			}
    		}
    		
    	}
    	else
    	{
    		// 딜러 순위
        	//emfMap.put("rnkngList", cOAMainDAO.getHgsiRankingList(emfMap));
    		list = cOAMainDAO.getHgsiRankingList(emfMap);
    	}
    	emfMap.put("rnkngList", list);

    	return emfMap;
    }

    /**
	 * HGSI Trend 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getHgsiMonthList(EmfMap emfMap) throws Exception
	{
		String searchScope = emfMap.getString("searchScope");

		if ("all".equals(searchScope))
    	{	
			emfMap.put("gubun", "A");
			emfMap.put("hgsiMonthList", cOAMainDAO.getNatnHgsiMonthList(emfMap));
    	}
		else if ("natn".equals(searchScope))
		{
			//지역 본부 관리자 > 선택한 국가 HGSI 평균 조회
			emfMap.put("targetYn", "Y");
			emfMap.put("gubun", "A");
			emfMap.put("parntHgsiMonthList", cOAMainDAO.getNatnHgsiMonthList(emfMap));
			emfMap.put("targetYn", "N");
			emfMap.put("gubun", "B");
    		emfMap.put("hgsiMonthList", cOAMainDAO.getNatnHgsiMonthList(emfMap));
		}
		else if ("dlrsp".equals(searchScope))
		{
			//지역 본부 관리자 > 선택한 국가 > 대리점 HGSI 평균 조회
			emfMap.put("targetYn", "Y");
			emfMap.put("gubun", "A");
			emfMap.put("parntHgsiMonthList", cOAMainDAO.getNatnHgsiMonthList(emfMap));
			emfMap.put("targetYn", "N");
			emfMap.put("gubun", "B");
    		emfMap.put("hgsiMonthList", cOAMainDAO.getNatnHgsiMonthList(emfMap));
		} 
		else if ("dlr".equals(searchScope))
		{	
			//지역 본부 관리자 > 선택한 국가 > 대리점 > 딜러 HGSI 평균 조회
			emfMap.put("targetYn", "Y");
			emfMap.put("gubun", "C");
			emfMap.put("parntHgsiMonthList", cOAMainDAO.getNatnHgsiMonthList(emfMap));
			emfMap.put("targetYn", "N");
			emfMap.put("gubun", "D");
    		emfMap.put("hgsiMonthList", cOAMainDAO.getNatnHgsiMonthList(emfMap));
    	}
		
		return emfMap;
	}
	
	/**
     * NPS Status 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap getNpsStatus(EmfMap emfMap) throws Exception
    {
    	String searchScope = emfMap.getString("searchScope");

    	String trgtVal;
    	String dealerTrgtVal = "";
    	
    	// annual target 설정 여부 확인 및 값 가져오기
    	if (!"all".equals(searchScope)){
    		emfMap.put("annualTrgtTypeCd", "N");
    		trgtVal = cOAMainDAO.getAnnualTrgt(emfMap).getString("trgtVal");
    		dealerTrgtVal = cOAMainDAO.getDealerAnnualTrgt(emfMap).getString("trgtVal");
    		
    		if(!"".equals(trgtVal)){
    			emfMap.put("trgtYN", "Y");
    		}else{
    			emfMap.put("trgtYN", "N");
    		}
    		
    		if(!"".equals(dealerTrgtVal)){
    			emfMap.put("trgtDealerYN", "Y");
    		}else{
    			emfMap.put("trgtDealerYN", "N");
    		}
    		
    		emfMap.put("trgtVal",trgtVal);
    		emfMap.put("dealerTrgtVal",dealerTrgtVal);
    	}

		if ("all".equals(searchScope) || "natn".equals(searchScope))
    	{
			if ("natn".equals(searchScope))
			{
				emfMap.put("isNatn", "1");
			}
			
			emfMap.put("list" , cOAMainDAO.getNatnNpsStatus(emfMap));
    	}
		else
    	{
			emfMap.put("list" , cOAMainDAO.getNpsStatus(emfMap));
    	}

		return emfMap;
    }
    
    /**
     * NPS Ranking 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap getNpsRankingList(EmfMap emfMap) throws Exception
    {
    	String searchScope = emfMap.getString("searchScope");
    	List<EmfMap> list = new ArrayList<EmfMap>();
    	List<EmfMap> list2 = new ArrayList<EmfMap>();

    	if ("all".equals(searchScope))
    	{
    		// 국가 순위
        	//emfMap.put("rnkngList", cOAMainDAO.getNatnNpsRankingList(emfMap));
    		list = cOAMainDAO.getNatnNpsRankingList(emfMap);
    	}
    	else if ("dlrsp".equals(searchScope))
    	{
    		// 대리점 순위
        	//emfMap.put("rnkngList", cOAMainDAO.getDlrspNpsRankingList(emfMap));
    		list = cOAMainDAO.getDlrspNpsRankingList(emfMap);
    		list2 = cOAMainDAO.getNpsRankingList(emfMap);
    		
    		if(list2.size()>0){
    			for (int i = 0; i < list2.size(); i++) {
    				list.add(list2.get(i));
    			}
    		}
    		
    	}
    	else
    	{
    		// 딜러 순위
        	//emfMap.put("rnkngList", cOAMainDAO.getNpsRankingList(emfMap));
    		list = cOAMainDAO.getNpsRankingList(emfMap);
    	}
    	
    	emfMap.put("rnkngList", list);
    	return emfMap;
    }
	
	/**
	 * NPS Trend 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getNpsMonthList(EmfMap emfMap) throws Exception
	{
		String searchScope = emfMap.getString("searchScope");

		if ("all".equals(searchScope))
    	{
			emfMap.put("gubun", "A");
			emfMap.put("npsMonthList", cOAMainDAO.getNatnNpsMonthList(emfMap));
    	}
		else if ("natn".equals(searchScope))
		{
			//지역 본부 관리자 > 선택한 국가 NPS 평균 조회
			emfMap.put("targetYn", "Y");
			emfMap.put("gubun", "A");
			emfMap.put("parntNpsMonthList", cOAMainDAO.getNatnNpsMonthList(emfMap));
			emfMap.put("targetYn", "N");
			emfMap.put("gubun", "B");
			emfMap.put("npsMonthList", cOAMainDAO.getNatnNpsMonthList(emfMap));
		}
		else if ("dlrsp".equals(searchScope))
		{
			//지역 본부 관리자 > 선택한 국가 > 대리점 NPS 평균 조회
			emfMap.put("targetYn", "Y");
			emfMap.put("gubun", "A");
			emfMap.put("parntNpsMonthList", cOAMainDAO.getNatnNpsMonthList(emfMap));
			emfMap.put("targetYn", "N");
			emfMap.put("gubun", "B");
			emfMap.put("npsMonthList", cOAMainDAO.getNatnNpsMonthList(emfMap));
    	}
		else if ("dlr".equals(searchScope))
		{
			//지역 본부 관리자 > 선택한 국가 > 대리점 > 딜러 NPS 평균 조회
			emfMap.put("targetYn", "Y");
			emfMap.put("gubun", "C");
			emfMap.put("parntNpsMonthList", cOAMainDAO.getNatnNpsMonthList(emfMap));
			emfMap.put("targetYn", "N");
			emfMap.put("gubun", "D");
			emfMap.put("npsMonthList", cOAMainDAO.getNatnNpsMonthList(emfMap));
		}
		
		return emfMap;
	}

	/**
	 * Key Question Performance 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getKeyQuestionPerformanceList(EmfMap emfMap) throws Exception
	{
		String searchScope = emfMap.getString("searchScope");

		if ("all".equals(searchScope) || "natn".equals(searchScope))
    	{
			if ("natn".equals(searchScope))
			{
				emfMap.put("isNatn", "1");
			}
			
    		emfMap.put("keyQuestionPerformanceList", cOAMainDAO.getNatnKeyQuestionPerformanceList(emfMap));    		
    	}
		else
    	{
    		emfMap.put("keyQuestionPerformanceList", cOAMainDAO.getKeyQuestionPerformanceList(emfMap));    		
    	}

		return emfMap;
	}
	
	/**
	 * Key Question Performance 추가 질문 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getKeyQuestionPerformanceAddResultList(EmfMap emfMap) throws Exception
	{
		String searchScope = emfMap.getString("searchScope");
		
		List<EmfMap> list = null;
		List<EmfMap> testDriveList = null;
		
		if ("all".equals(searchScope) || "natn".equals(searchScope))
    	{
			if ("natn".equals(searchScope))
			{
				emfMap.put("isNatn", "1");
			}
			
			list = cOAMainDAO.getNatnKeyQuestionPerformanceAddResultList(emfMap);
			// test drive인 경우만
			if ("81".equals(emfMap.getString("lCtgSeq"))){
				testDriveList = cOAMainDAO.getNatnKeyQuestionPerformanceTestDriveResultList(emfMap);
			}
		}
		else
		{
			list = cOAMainDAO.getKeyQuestionPerformanceAddResultList(emfMap);
			// test drive인 경우만
			if ("81".equals(emfMap.getString("lCtgSeq"))){
				testDriveList = cOAMainDAO.getKeyQuestionPerformanceTestDriveResultList(emfMap);
			}
		}
		
		int totCnt = 0;

		for (int i = 0, size = list.size(); i < size; i++) 
		{
			totCnt += Integer.parseInt(list.get(i).getString("exAnswCnt"));
		}
		
		emfMap.put("list", list);
		emfMap.put("testDriveList", testDriveList);
		emfMap.put("totCnt", totCnt);

		return emfMap;
	}

	/**
	 * Key Question Performance Trend (Latest 3 Months) 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getKeyLatestQuestionPerformanceList(EmfMap emfMap) throws Exception
	{
		String searchScope = emfMap.getString("searchScope");

		switch(searchScope){
		case "natn":
			emfMap.put("natnCd",emfMap.getString("searchCd"));
			break;
		case "dlrsp":
			emfMap.put("dlrspCd",emfMap.getString("searchCd"));
			emfMap.put("natnCd",emfMap.getString("searchCd").substring(0,2));
			break;
		case "dlr":
			emfMap.put("dlrCd",emfMap.getString("searchCd"));
			emfMap.put("dlrspCd",emfMap.getString("searchCd").substring(0,5));
			emfMap.put("natnCd",emfMap.getString("searchCd").substring(0,2));
			break;
		}
		
		emfMap.put("keyLatestQuestionPerformanceList", cOAMainDAO.getKeyLatestQuestionPerformanceList(emfMap));
		return emfMap;
	}

	/**
	 * Survey Response Status 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getSurveyResponseStatusList(EmfMap emfMap) throws Exception
	{
		String searchScope = emfMap.getString("searchScope");
		
		if("all".equals(searchScope)){
			emfMap.put("gubun", "A");
		}else if("natn".equals(searchScope)){
			emfMap.put("gubun", "B");
		}else if("dlrsp".equals(searchScope)){
			emfMap.put("gubun", "C");
		}else if("dlr".equals(searchScope)){
			emfMap.put("gubun", "D");
		}
		emfMap.put("monthList", cOAMainDAO.getNatnSurveyResponseStatusList(emfMap));
		emfMap.put("deliveredPkgInfo", cOAMainDAO.selectDeliveredPkgInfo(emfMap));

		return emfMap;
	}

	/**
	 * HGSI Feedback 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getFeedbackList(EmfMap emfMap) throws Exception
	{
		String searchScope = emfMap.getString("searchScope");
		int authCd = Integer.parseInt( emfMap.getString("admAuthCd") );
		String gubun = "";
		List<EmfMap> list = new ArrayList<EmfMap>();
		List<EmfMap> list2 = new ArrayList<EmfMap>();
		
		String kpiCd = emfMap.getString("kpiCd");    	
		
//		System.out.println("searchScope = " + searchScope);
		if ("all".equals(searchScope)){
			gubun = "A";
			emfMap.put("gubun", gubun);
			
			//KPI 국가 조회시 국가코드 값 비우도록 수정
			if("KP".equals(kpiCd)){
				emfMap.put("ntnCd","");
			}
			
			list = cOAMainDAO.getFeedbackList(emfMap);
    	}
		/*else if ("natn".equals(searchScope)){
			gubun = "B";
			emfMap.put("gubun", gubun);
			list = cOAMainDAO.getFeedbackList(emfMap);
			
			gubun = "C";
			emfMap.put("gubun", gubun);
			list2 = cOAMainDAO.getFeedbackList(emfMap);
			
		}*/
    	else if ("dlrsp".equals(searchScope)){
    		// 대리점 이상 권한은 대리점, 딜러 같이 표시
    		if(authCd <= 30){
    			gubun = "B";
    			emfMap.put("gubun", gubun);
    			list = cOAMainDAO.getFeedbackList(emfMap);
    			
    			gubun = "C";
    			emfMap.put("gubun", gubun);
    			list2 = cOAMainDAO.getFeedbackList(emfMap);
    			
    			if(list2.size()>0){
    				for (int i = 0; i < list2.size(); i++) {
        				list.add(list2.get(i));
        			}
    			}
    			
    		}else{
    			gubun = "C";
    			emfMap.put("gubun", gubun);
    			list = cOAMainDAO.getFeedbackList(emfMap);
    		}
    		
		}
    	else if ("dlr".equals(searchScope)){
			gubun = "D";
			emfMap.put("gubun", gubun);
			list = cOAMainDAO.getFeedbackList(emfMap);
		}
		
		//emfMap.put("gubun", gubun);
		emfMap.put("list", list);
		
		return emfMap;
	}

	/**
	 * Hot Alert - Latest 3 Months 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getHotAlertList(EmfMap emfMap) throws Exception
	{
		String searchScope = emfMap.getString("searchScope");
		
		if ("all".equals(searchScope))
    	{
			emfMap.put("list", cOAMainDAO.getNatnHotAlertList(emfMap));
    	}
		else if ("natn".equals(searchScope))
		{
			emfMap.put("list", cOAMainDAO.getDlrspHotAlertList(emfMap));
		}
		else if ("dlrsp".equals(searchScope))
		{
			emfMap.put("list", cOAMainDAO.getHotAlertList(emfMap));
		}
		else if ("dlr".equals(searchScope))
		{
			emfMap.put("list", cOAMainDAO.getCstmrHotAlertList(emfMap));
			emfMap.put("info", cOAMainDAO.getCstmrHotAlertAvgAcptHour(emfMap));
		}

		return emfMap;
	}

	/**
	 * HGSI Action Planning - Latest 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getHgsiActPlngList(EmfMap emfMap) throws Exception
	{
		String searchScope = emfMap.getString("searchScope");
		
		// 최근 분기 가져오기
    	emfMap = getRcntQrtr(emfMap);

    	if ("dlr".equals(searchScope))
		{
    		emfMap.put("list", cOAMainDAO.getHgsiActPlngList(emfMap));
		}
		
		return emfMap;
	}

	/**
	 * Retail Standard Check Action Planning - Latest 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getRscActPlngList(EmfMap emfMap) throws Exception
	{
		String searchScope = emfMap.getString("searchScope");
		int authCd = Integer.parseInt( emfMap.getString("admAuthCd") );
		List<EmfMap> list = new ArrayList<EmfMap>();
		List<EmfMap> list2 = new ArrayList<EmfMap>();
		
		// 최근 분기 가져오기
		String srvyTypeCd = emfMap.getString("srvyTypeCd");
		
		// RSC
		if ("STC2".equals(srvyTypeCd))
		{
			emfMap = getRcntQrtr2(emfMap);
		}
		// Mystery Shopping
		else if ("STC3".equals(srvyTypeCd))
		{
			emfMap = getRcntQrtr(emfMap);
		}
		
		if ("all".equals(searchScope))
    	{
			//emfMap.put("list", cOAMainDAO.getNatnRscActPlngList(emfMap));
			list = cOAMainDAO.getNatnRscActPlngList(emfMap);
    	}
		/*else if ("natn".equals(searchScope))
    	{
			emfMap.put("list", cOAMainDAO.getDlrspRscActPlngList(emfMap));
    	}*/
		else if ("dlrsp".equals(searchScope))
		{
			if(authCd <= 30){
				list = cOAMainDAO.getDlrspRscActPlngList(emfMap);
				list2 = cOAMainDAO.getRscActPlngList(emfMap);
				
				if(list2.size()>0){
					for (int i = 0; i < list2.size(); i++) {
						list.add(list2.get(i));
					}
				}
				
			}else{
				list = cOAMainDAO.getRscActPlngList(emfMap);
			}
			//emfMap.put("list", cOAMainDAO.getRscActPlngList(emfMap));
			
		}
		else if ("dlr".equals(searchScope))
		{
			//emfMap.put("list", cOAMainDAO.getRscActPlngDtl(emfMap));
			list = cOAMainDAO.getRscActPlngDtl(emfMap);
		}
		
		emfMap.put("list", list);		
		return emfMap;
	}

	/**
	 * Profitability Data Status - Latest 조회 
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getProfitabilityDataList(EmfMap emfMap) throws Exception
	{
		int authCd = Integer.parseInt( emfMap.getString("admAuthCd") );
		String searchScope = emfMap.getString("searchScope");
		List<EmfMap> list = new ArrayList<EmfMap>();
		List<EmfMap> list2 = new ArrayList<EmfMap>();
		
		if ("all".equals(searchScope))
    	{
			//emfMap.put("list", cOAMainDAO.getNatnProfitabilityDataList(emfMap));
			list = cOAMainDAO.getNatnProfitabilityDataList(emfMap);
    	}
		/*else if ("natn".equals(searchScope))
    	{
			emfMap.put("list", cOAMainDAO.getDlrspProfitabilityDataList(emfMap));
    	}*/
		else if ("dlrsp".equals(searchScope))
		{
			//emfMap.put("list", cOAMainDAO.getProfitabilityDataList(emfMap));
			if(authCd <= 30){
				list = cOAMainDAO.getDlrspProfitabilityDataList(emfMap);
				list2 = cOAMainDAO.getProfitabilityDataList(emfMap);
				
				if(list2.size()>0){
					for (int i = 0; i < list2.size(); i++) {
						list.add(list2.get(i));
					}
				}
				
			}else{
				list = cOAMainDAO.getProfitabilityDataList(emfMap);
			}
		}
		else if ("dlr".equals(searchScope))
		{
			//emfMap.put("list", cOAMainDAO.getProfitabilityDataDtl(emfMap));
			list = cOAMainDAO.getProfitabilityDataDtl(emfMap);
		}
		emfMap.put("list", list);
		return emfMap;
	}

	/**
	 * 대리점 구글 리뷰
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getGoogleRvwList(EmfMap emfMap) throws Exception
	{
		String searchScope = emfMap.getString("searchScope");
		int authCd = Integer.parseInt( emfMap.getString("admAuthCd") );
		
		List<EmfMap> googleRvwList = null;
		List<EmfMap> googleRvwList2 = null;
		
		if ("all".equals(searchScope))
    	{
			//지역 본부 관리자 > 모든 국가 대리점 구글 리뷰
			googleRvwList = cOAMainDAO.getGoogleNatnRvwList(emfMap);
    	}
		else if ("dlrsp".equals(searchScope) && authCd <= 30)
    	{
			//지역 본부 관리자 > 선택한 국가 대리점 구글 리뷰
			googleRvwList = cOAMainDAO.getGoogleNatnDlrspRvwList(emfMap);
			googleRvwList2 = cOAMainDAO.getGoogleRvwList(emfMap);

			for (EmfMap map : googleRvwList) { // 국가 : 대리점 = 1 : 1
				emfMap.put("rvwCnt", map.getString("rvwCnt"));
				emfMap.put("scrg", map.getString("scrg"));
				emfMap.put("prcpnCnt", map.getString("prcpnCnt"));
			}

			if (googleRvwList2.size() > 0) {
				for (int i = 0; i < googleRvwList2.size(); i++) {
					googleRvwList.add(googleRvwList2.get(i));
				}
			}
			
    	}
		else
		{
			googleRvwList = cOAMainDAO.getGoogleRvwList(emfMap);
		}
		
		emfMap.put("list", googleRvwList);
		
		return emfMap;
	}

	/**
	 * 대리점 페이스북 리뷰
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getFacebookRvwList(EmfMap emfMap) throws Exception
	{
		String searchScope = emfMap.getString("searchScope");
		int authCd = Integer.parseInt( emfMap.getString("admAuthCd") );
		
		List<EmfMap> facebookRvwList = null;
		List<EmfMap> facebookRvwList2 = null;
		
		if ("all".equals(searchScope))
    	{
			//지역 본부 관리자 > 모든 국가 대리점 페이스북 리뷰
			facebookRvwList = cOAMainDAO.getFacebookNatnRvwList(emfMap);
    	}
		else if ("dlrsp".equals(searchScope) && authCd <= 30)
    	{
			//지역 본부 관리자 > 선택한 국가 대리점 페이스북 리뷰
			facebookRvwList = cOAMainDAO.getFacebookNatnDlrspRvwList(emfMap);
			facebookRvwList2 = cOAMainDAO.getFacebookRvwList(emfMap);
			
			if(facebookRvwList2.size()>0){
				for (int i = 0; i < facebookRvwList2.size(); i++) {
					facebookRvwList.add(facebookRvwList2.get(i));
				}
			}
			
    	}
		else
		{
			facebookRvwList = cOAMainDAO.getFacebookRvwList(emfMap);
		}
		
		emfMap.put("list", facebookRvwList);
		
		return emfMap;
	}

	/**
	 * Tracker 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap getTrackerList(EmfMap emfMap) throws Exception
	{
		String authCd = emfMap.getString("searchScope");
		
		emfMap = getRcntQrtr2(emfMap);
		
		if ("all".equals(authCd))
    	{
			emfMap.put("parntArea", cOAMainDAO.getNatnParntHgsiAvgScrg(emfMap));
			emfMap.put("trackerList", cOAMainDAO.getNatnTrackerList(emfMap));
    	}
		else if ("natn".equals(authCd))
    	{
			emfMap.put("parntArea", cOAMainDAO.getDlrspParntHgsiAvgScrg(emfMap));
			emfMap.put("trackerList", cOAMainDAO.getDlrspTrackerList(emfMap));
    	}
		else if ("dlrsp".equals(authCd))
		{
			emfMap.put("parntArea", cOAMainDAO.getParntHgsiAvgScrg(emfMap));
			emfMap.put("trackerList", cOAMainDAO.getTrackerList(emfMap));
		}
		
		emfMap.put("srvyCol", cOAMainDAO.getHgsiSrvyCol(emfMap));

		return emfMap;
	}
	
	/**
	 * 국가 설정 조회
	 *
	 * @param natnCd
	 * @return
	 * @throws Exception
	 */
	public EmfMap getNatnConfig(String natnCd) throws Exception
	{
		return cOAMainDAO.getNatnConfig(natnCd);
	}
	
    /**
     * 최근 분기 가져오기
     *
     * @param emfMap
	 * @return
     * @throws Exception
	 * @throws Exception
     */
    private EmfMap getRcntQrtr(EmfMap emfMap) throws Exception
    {
    	String today = EgovDateUtil.getToday();

    	int quarter = EgovStringUtil.quarterYear(today);

    	if (quarter == 1)
    	{
    		emfMap.put("strtYm", today.substring(0, 4) + "01");
        	emfMap.put("endYm", today.substring(0, 4) + "03");
    	}
    	else if (quarter == 2)
    	{
    		emfMap.put("strtYm", today.substring(0, 4) + "04");
        	emfMap.put("endYm", today.substring(0, 4) + "06");
    	}
    	else if (quarter == 3)
    	{
    		emfMap.put("strtYm", today.substring(0, 4) + "07");
        	emfMap.put("endYm", today.substring(0, 4) + "09");
    	}
    	else if (quarter == 4)
    	{
    		emfMap.put("strtYm", today.substring(0, 4) + "10");
        	emfMap.put("endYm", today.substring(0, 4) + "12");
    	}

    	return emfMap;
    }
    
    /**
     * 최근 분기 가져오기 (RSC 전용)
     *
     * @param emfMap
	 * @return
     * @throws Exception
	 * @throws Exception
     */
    private EmfMap getRcntQrtr2(EmfMap emfMap) throws Exception
    {
    	String today = EgovDateUtil.getToday();
    	
    	// RSC 배포 주기
    	String sendCd = "2";
    	
    	if (Integer.parseInt(emfMap.getString("admAuthCd")) >= 30)
    	{
			// TODO 수정 필요
    		/*EmfMap tmpMap = rCARscService.getRscDeployCycle(emfMap);

    		if (tmpMap != null)
    		{
    			sendCd = tmpMap.getString("sendCd");
    		}*/
    	}

    	int quarter = EgovStringUtil.quarterYear(today);

    	// 분기
    	if ("1".equals(sendCd))
    	{
    		if (quarter == 1)
        	{
        		emfMap.put("strtYm", today.substring(0, 4) + "01");
            	emfMap.put("endYm", today.substring(0, 4) + "03");
        	}
        	else if (quarter == 2)
        	{
        		emfMap.put("strtYm", today.substring(0, 4) + "04");
            	emfMap.put("endYm", today.substring(0, 4) + "06");
        	}
        	else if (quarter == 3)
        	{
        		emfMap.put("strtYm", today.substring(0, 4) + "07");
            	emfMap.put("endYm", today.substring(0, 4) + "09");
        	}
        	else if (quarter == 4)
        	{
        		emfMap.put("strtYm", today.substring(0, 4) + "10");
            	emfMap.put("endYm", today.substring(0, 4) + "12");
        	}
    	}
    	// 반기
    	else if ("2".equals(sendCd))
    	{
    		if (quarter == 1 || quarter == 2)
        	{
        		emfMap.put("strtYm", today.substring(0, 4) + "01");
            	emfMap.put("endYm", today.substring(0, 4) + "06");
        	}
        	else if (quarter == 3 || quarter == 4)
        	{
        		emfMap.put("strtYm", today.substring(0, 4) + "07");
            	emfMap.put("endYm", today.substring(0, 4) + "12");
        	}
    	}
    	// 1년
    	else if ("3".equals(sendCd))
    	{
    		emfMap.put("strtYm", today.substring(0, 4) + "01");
        	emfMap.put("endYm", today.substring(0, 4) + "12");
    	}

    	return emfMap;
    }
    
    /**
     * 딜러명 or 딜러코드(현대딜러코드)로 검색하기 위한 모든 딜러List
     *
     * @param emfMap
     * @return
     * @throws Exception
     * @throws Exception
     */
    public List<EmfMap> getSearchDlrList(EmfMap emfMap) throws Exception
    {
    	return cOAMainDAO.getSearchDlrList(emfMap);
    }
    
    /**
     * RSC report Excel
     * 
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public EmfMap getFrptRscListExcel(EmfMap emfMap) throws Exception {
		int authCd = Integer.parseInt( emfMap.getString("admAuthCd") );
		// 연도 +반기 로 해당되는 딜러들을 검색을 위한 선행조건
		String sYear = emfMap.getString("endDt").substring(0,4);
		int half = 0;

		String strStartYm = "";
		String strEndYm = "";
		String searchScope = "";
		String searchScopeOrg = emfMap.getString("searchScope");

		// 검색 조건 반기
		half = Integer.parseInt(emfMap.getString("endDt").substring(5,7));

		if (half < 7) {
			strStartYm = sYear + "01";
			strEndYm = sYear + "06";
		} else{
			strStartYm = sYear + "07";
			strEndYm = sYear + "12";
		}

		// 2020922 수정
		if (!"".equals(emfMap.getString("dlrCd"))) {
			searchScope = "dlr";
		} else if (!"".equals(emfMap.getString("dlspCd"))) {
			// 20200917 대리점 가능하게 수정
			searchScope = "dlrsp";
		} else {
			searchScope = "natn";
		}

		// 대리점관리자 일 때, 딜러검색과 동일
		if (Integer.parseInt(emfMap.getString("admAuthCd")) >= 30) {
			searchScope = "dlr";
		}

		emfMap.put("searchScope", searchScope);
		List<EmfMap> list1 = new ArrayList<EmfMap>();
		List<EmfMap> listTemp = new ArrayList<EmfMap>();

			if ("natn".equals(searchScope)) {
				// 국가별 조회
				list1 = fRAFrptDAO.selectFrptNatnList(emfMap);
			} else if ("dlrsp".equals(searchScope)) {
				// 대리점 조회
				if(authCd <= 30){
					list1 = fRAFrptDAO.selectFrptDealerShipList(emfMap);
					listTemp = fRAFrptDAO.selectFrptDealerList(emfMap);
					if(listTemp.size()>0){
						for (int i = 0; i < listTemp.size(); i++) {
							list1.add(listTemp.get(i));
						}
					}
					
				}else{
					list1 = fRAFrptDAO.selectFrptDealerShipList(emfMap);
				}
				
			} else if("dlr".equals(searchScope)){
				// 딜러 리스트 조회
				list1 = fRAFrptDAO.selectFrptDealerList(emfMap);
			}

			List<EmfMap> thCateList = new ArrayList<EmfMap>();
			List<EmfMap> list = new ArrayList<EmfMap>();

			int chk = 0;
			if (list1.size() > 0) {
				for (int i = 0, max=list1.size(); i < max; i++) {
					EmfMap dtlMap = list1.get(i);

					dtlMap.put("asgnTaskCd", emfMap.getString("asgnTaskCd"));
					dtlMap.put("searchScope", searchScope);
					dtlMap.put("strStartYm", strStartYm);
					dtlMap.put("strEndYm", strEndYm);
					
					List<EmfMap> list2 = new ArrayList<EmfMap>();

					if ("natn".equals(searchScope) ) {
						// 딜러에 따른 RSC 대 카테고리 준수율 조회(국가, 대리점 종합)
						list2 = fRAFrptDAO.selectFrptNatnDealerRscScoreList(dtlMap);
					} else if("dlrsp".equals(searchScope)){
						if(i==0){
							list2 = fRAFrptDAO.selectFrptNatnDealerRscScoreList(dtlMap);
						}else{
							list2 = fRAFrptDAO.selectFrptDealerRscScoreList(dtlMap);
						}
					} else if("dlr".equals(searchScope)){
						// 딜러에 따른 RSC 대 카테고리 준수율 조회
						list2 = fRAFrptDAO.selectFrptDealerRscScoreList(dtlMap);
					}

					if (list2.size() > 0) {
						for (int k = 0, max2=list2.size(); k < max2 ; k++) {
							EmfMap dtlMap2 = list2.get(k);

							switch (dtlMap2.getString("lCtgSeq")) {
							case "102":
								dtlMap.put("marketing",dtlMap2.getString("marketing"));
								break;
							case "103":
								dtlMap.put("customerData",dtlMap2.getString("customerData"));
								break;
							case "104":
								dtlMap.put("facility",dtlMap2.getString("facility"));
								break;
							case "105":
								dtlMap.put("salesProcess",dtlMap2.getString("salesProcess"));
								break;
							case "106":
								dtlMap.put("customerCare",dtlMap2.getString("customerCare"));
								break;
							case "107":
								dtlMap.put("business",dtlMap2.getString("business"));
								break;
							case "108":
								dtlMap.put("hr", dtlMap2.getString("hr"));
								break;
							case "144":
								dtlMap.put("serviceProcess",dtlMap2.getString("serviceProcess"));
								break;
							case "145":
								dtlMap.put("serviceOperation",dtlMap2.getString("serviceOperation"));
								break;
							case "146":
								dtlMap.put("serviceFacility",dtlMap2.getString("serviceFacility"));
								break;
							}
						}
					} else {
						dtlMap.put("marketing", 0);
						dtlMap.put("customerData", 0);
						dtlMap.put("facility", 0);
						dtlMap.put("salesProcess", 0);
						dtlMap.put("customerCare", 0);
						dtlMap.put("business", 0);
						dtlMap.put("hr", 0);
						dtlMap.put("serviceProcess", 0);
						dtlMap.put("serviceOperation", 0);
						dtlMap.put("serviceFacility", 0);
					}

					// 딜러에 따른 RSC 문항별 답 조회
					List<EmfMap> list3 = fRAFrptDAO.selectFrptDealerRscScoreQstnList(dtlMap);
					// 테이블 th 항목
					if (list3.size() > 0) {
						if (chk == 0) {
							for (int j = 0, max3 = list3.size(); j < max3; j++) {
								EmfMap dtlMap3 = list3.get(j);
								thCateList.add(j, dtlMap3);
							}
							chk++;
						}
						dtlMap.put("qstnList", list3);
					}
					list.add(i, dtlMap);
				}
			}
			
			emfMap.put("searchScope", searchScopeOrg);
			emfMap.put("thCateList", thCateList);
			emfMap.put("reportList", list);

		return emfMap;
	}

	/**
     * Flexible Report Dealer KPI List Excel
     * 
     * @param emfMap
     * @return
     * @throws Exception
     */
    public EmfMap selectFrptKpiExcel(EmfMap emfMap) throws Exception
    {
    	int authCd = Integer.parseInt( emfMap.getString("admAuthCd") );
    	//KPI 호출 시 딜러리스트 조건절을 위한 값 담기
    	emfMap.put("currentPage", "kpi");
    	//연도 +(월/분기)로 해당되는 딜러들을 검색을 위한 선행조건
		String curtYear = emfMap.getString("endDt").substring(0,4);
		int month	 = 0;
		
    	String strStartYm = "";
    	String strEndYm = "";
    	
    	if(!"".equals(emfMap.getString("endDt").substring(5,6)))
    	{
    		month         = Integer.parseInt(emfMap.getString("endDt").substring(5,7));

    		if(month <= 3)
    		{
    			strStartYm= curtYear + "01";
    			strEndYm= curtYear + "03";
    		}
    		else if(month <= 6)
    		{
    			strStartYm= curtYear + "04";
    			strEndYm= curtYear + "06";
    		}
    		else if(month <= 9)
    		{
    			strStartYm= curtYear + "07";
    			strEndYm= curtYear + "09";
    		}
    		else
    		{
    			strStartYm= curtYear + "10";
    			strEndYm= curtYear + "12";
        	}
    		emfMap.put("strStartYm", strStartYm);
    		emfMap.put("strEndYm", strEndYm);
    	}
    	
    	//KPI 문항만 조회
    	List<EmfMap> list = fRAFrptDAO.selectFrptKpiQstnList(emfMap);
    	if(list.size() > 0)
    	{
    		emfMap.put("qstnList", list);
    	}
    	
    	String searchScope = "";
    	String searchScopeOrg = emfMap.getString("searchScope");
    	
    	if("".equals(emfMap.getString("ntnCd"))){
    		searchScope = "natn";
    	}else if(!"".equals(emfMap.getString("ntnCd"))&&!"".equals(emfMap.getString("dlspCd"))){
    		// 20200917 대리점 가능하게 수정
    		searchScope = "dlrsp";
    	}else{
    		searchScope = "dlr";
    	}
    	
    	//대리점관리자 일 때, 딜러검색과 동일
    	if(Integer.parseInt(emfMap.getString("admAuthCd")) >= 30)
    	{
    		searchScope = "dlr";
    	}
    	
    	emfMap.put("searchScope", searchScope);
    	
		if(list.size() > 0)
		{
			
			List<EmfMap> list1 = new ArrayList<EmfMap>();
			List<EmfMap> listTemp = new ArrayList<EmfMap>();
	    	
	    	if("natn".equals(searchScope)){
				//국가별 조회
				list1 = fRAFrptDAO.selectFrptNatnList(emfMap);
			}else if("dlrsp".equals(searchScope)){
				//대리점 조회
				if(authCd <= 30){
					list1 = fRAFrptDAO.selectFrptDealerShipList(emfMap);
					listTemp = fRAFrptDAO.selectFrptDealerList(emfMap);
					if(listTemp.size()>0){
						for (int i = 0; i < listTemp.size(); i++) {
							list1.add(listTemp.get(i));
						}
					}
					
				}else{
					list1 = fRAFrptDAO.selectFrptDealerShipList(emfMap);
				}
				
			}else{
		    	//딜러 리스트 조회
		    	list1 = fRAFrptDAO.selectFrptDealerList(emfMap);
			}
	    	
	    	List<EmfMap> list2 = new ArrayList<EmfMap>();
	    	List<EmfMap> list3 = new ArrayList<EmfMap>();
	    	
	    	if(list1.size()>0){
	    		for(int i=0; i < list1.size(); i++){
	    			EmfMap dtlMap = list1.get(i);
	    			dtlMap.put("asgnTaskCd", emfMap.getString("asgnTaskCd"));
	    			dtlMap.put("searchScope", emfMap.getString("searchScope"));
	    			dtlMap.put("strStartYm", strStartYm);
	    			dtlMap.put("strEndYm", strEndYm);
	    			
	    			//딜러에 따른 KPI 문항별 점수 
	    			list3 = fRAFrptDAO.selectFrptDealerKpiScoreQstnList(dtlMap);
	    			
    				dtlMap.put("scoreList", list3);
	    			list2.add(i, dtlMap);
	    		}
	    	}
	    	emfMap.put("searchScope", searchScopeOrg);
	    	emfMap.put("reportList", list2);
		}
		
		EmfMap tmpMap = new EmfMap();
		//ArrayList<String> cdDtlList = new ArrayList<String>();
		
		if (emfMap.getString("siteLanguage") != null) {
			tmpMap.put("siteLanguage",emfMap.getString("siteLanguage"));
		}else{
			tmpMap.put("siteLanguage","en");
		}
		
		if("sales".equals(emfMap.getString("asgnTaskCd")))
		{
			//cdDtlList.add("KPI_SALES_TYPE_CD");
			tmpMap.put("cdDtl","KPI_SALES_TYPE_CD");
		}
		else
		{
			//cdDtlList.add("KPI_SERVICE_TYPE_CD");
			tmpMap.put("cdDtl","KPI_SERVICE_TYPE_CD");
		}
		List<EmfMap> cdDtlList = cOAMainDAO.getKpiCdNmList(tmpMap);
		//modelMap.put("cdDtlList", cmmUseService.getCmmCodeBindAll(cdDtlList));
		emfMap.put("cdDtlList", cdDtlList);
    	return emfMap;
    }
    
    
    @Override
	public EmfMap selectSurveyMonthlyExcel(EmfMap emfMap) throws Exception {
		
		String searchScope = emfMap.getString("searchScope");
		int authCd = Integer.parseInt( emfMap.getString("admAuthCd") );
		String gubun = "";		// 프로시저 호출 하는 구분 값
		List<EmfMap> list = new ArrayList<EmfMap>();
		    	
    	
		if("all".equals(searchScope)){
			gubun = "A";
			emfMap.put("gubun",gubun);			
			list = cOAMainDAO.selectSurveyMonthlyListExcel(emfMap);
		}else if("natn".equals(searchScope)){
			gubun = "B";
			emfMap.put("gubun",gubun);
			list = cOAMainDAO.selectSurveyMonthlyListExcel(emfMap);
		}else if("dlrsp".equals(searchScope) && authCd <= 30){
			gubun = "B";
			emfMap.put("gubun",gubun);
			list = cOAMainDAO.selectSurveyMonthlyListExcel(emfMap);
			
		}else if("dlr".equals(searchScope)){
			gubun = "D";
			emfMap.put("gubun",gubun);
			list = cOAMainDAO.selectSurveyMonthlyListExcel(emfMap);
		}
		
		emfMap.put("reportList", list);
		
		return emfMap;
	}
    
	@Override
	public EmfMap selectFrptSurveyExcel(EmfMap emfMap) throws Exception {
		
		String searchScope = emfMap.getString("searchScope");
		int authCd = Integer.parseInt( emfMap.getString("admAuthCd") );
		String gubun = "";		// 프로시저 호출 하는 구분 값
		List<EmfMap> list = new ArrayList<EmfMap>();
    	List<EmfMap> list2 = new ArrayList<EmfMap>();
		    	
    	String kpiCd = emfMap.getString("kpiCd");
    	
		if("all".equals(searchScope)){
			gubun = "A";
			emfMap.put("gubun",gubun);
			//KPI 국가 조회시 국가코드 값 비우도록 수정
			if("KP".equals(kpiCd)){
				emfMap.put("ntnCd","");
			}

			list = cOAMainDAO.selectSurveyListExcel(emfMap);
		}else if("natn".equals(searchScope)){
			gubun = "B";
			emfMap.put("gubun",gubun);
			list = cOAMainDAO.selectSurveyListExcel(emfMap);
		}else if("dlrsp".equals(searchScope) && authCd <= 30){
			gubun = "B";
			emfMap.put("gubun",gubun);
			list = cOAMainDAO.selectSurveyListExcel(emfMap);
			
			gubun = "C";
			emfMap.put("gubun",gubun);
			list2 = cOAMainDAO.selectSurveyListExcel(emfMap);
			
			if(list2.size()>0){
				for (int i = 0; i < list2.size(); i++) {
					list.add(list2.get(i));
				}
			}
			
			
		}else{
			gubun = "C";
			emfMap.put("gubun",gubun);
			list = cOAMainDAO.selectSurveyListExcel(emfMap);
		}
		
		emfMap.put("reportList", list);
		
		return emfMap;
	}

	@Override
	public String convertDate(String date, boolean start) {
		String resulDtm = "";
		if(!"".equals(date)){
			String[] buffDtm = date.split("/");
			if(start){
				resulDtm = buffDtm[0] + "/01/" + buffDtm[1];
			}else{
				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(buffDtm[1]), Integer.parseInt(buffDtm[0])-1, 1);
				resulDtm = buffDtm[0] + "/"+cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/" + buffDtm[1];
			}
		}
		
		return resulDtm;
	}		

}