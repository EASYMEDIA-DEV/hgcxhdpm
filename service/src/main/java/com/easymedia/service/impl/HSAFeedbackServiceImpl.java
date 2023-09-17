package com.easymedia.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import com.easymedia.EgovProperties;
import com.easymedia.dto.EmfMap;
import com.easymedia.error.hotel.utility.sim.SeedCipher;
import com.easymedia.service.HSAFeedbackService;
import com.easymedia.service.dao.HSAFeedbackDAO;
import com.easymedia.utility.COJsonUtil;
import com.easymedia.utility.COPaginationUtil;
import com.easymedia.utility.EgovStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;


/**
 * <pre> 
 * HGSI - Feedback ServiceImpl
 * </pre>
 * 
 * @ClassName		: HSAFeedbackServiceImpl.java
 * @Description		: HGSI - Feedback ServiceImpl
 * @author 허진영
 * @since 2019.01.21
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				  description
 *   ===========    ==============    =============================
 *    2019.01.21		허진영				   최초 생성
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HSAFeedbackServiceImpl implements HSAFeedbackService {
	
    private final HSAFeedbackDAO hSAFeedbackDAO;
	
//	private String ENCODE = EgovProperties.getProperty("UTF-8");
	private String ENCODE = "UTF-8";

	/**
     * 딜러 List
     * 
     * @param emfMap 데이터
	 * @return
     */
    public EmfMap HSAFeedbackList(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();
    	
    	String[] strtDt = emfMap.getString("strtDt").split("/");
    	String[] endDt = emfMap.getString("endDt").split("/");
    	
    	Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(endDt[1]), Integer.parseInt(endDt[0])-1, 1);
    	
    	emfMap.put("strtDt", strtDt[0] + "/01/" + strtDt[1]);
    	emfMap.put("endDt", endDt[0] + "/"+cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/" + endDt[1]);
    	
    	PaginationInfo paginationInfo = new COPaginationUtil().getPage(emfMap);
    	
    	// 질문 가져오기
    	List<EmfMap> srvyList = hSAFeedbackDAO.selectSrvyWeakList(emfMap);
    	
		// 리스트 가져오기
		List<EmfMap> list = hSAFeedbackDAO.HSAFeedbackList(emfMap);
		List<EmfMap> list2 = new ArrayList<EmfMap>();
		// 추가질문 답변 가져오기
		List<EmfMap> addAnswList = hSAFeedbackDAO.getAddAnswList(emfMap);
		
		// 총 건수 가져오기
		int totCnt = 0;
		
		if("dealer".equals(emfMap.getString("gubun"))){
			totCnt = hSAFeedbackDAO.selectDlrListTotCnt(emfMap);
			list2 = list;
		}else if("customer".equals(emfMap.getString("gubun"))){
			totCnt = hSAFeedbackDAO.selectCstmrListTotCnt(emfMap);
			
			//2020.06.08 암호화
			for(int i = 0, max = list.size(); i < max; i++){
				EmfMap tmpMap = list.get(i);
				tmpMap.put("email", "".equals(tmpMap.getString("email")) ? "" : SeedCipher.decrypt(tmpMap.getString("email"),ENCODE));
				tmpMap.put("hp", "".equals(tmpMap.getString("hp")) ? "" : SeedCipher.decrypt(tmpMap.getString("hp"),ENCODE));
				
				list2.add(tmpMap);
			}
		}
		else if("customerAll".equals(emfMap.getString("gubun"))){
			totCnt = hSAFeedbackDAO.selectCstmrListTotCnt(emfMap);
			
			//2020.06.08 암호화
			for(int i = 0, max = list.size(); i < max; i++){
				EmfMap tmpMap = list.get(i);
				tmpMap.put("email", "".equals(tmpMap.getString("email")) ? "" : SeedCipher.decrypt(tmpMap.getString("email"),ENCODE));
				tmpMap.put("hp", "".equals(tmpMap.getString("hp")) ? "" : SeedCipher.decrypt(tmpMap.getString("hp"),ENCODE));
				
				list2.add(tmpMap);
			}
		}
		
		paginationInfo.setTotalRecordCount(totCnt);
		
		emfMap.put("strtDt", strtDt[0] + "/" + strtDt[1]);
    	emfMap.put("endDt", endDt[0] + "/" + endDt[1]);
		
		rtnMap.put("totCnt", totCnt);
		rtnMap.put("pageIndex", paginationInfo.getCurrentPageNo());
		rtnMap.put("recordPerPage", paginationInfo.getRecordCountPerPage());
		rtnMap.put("totPage", paginationInfo.getTotalPageCount());
		rtnMap.put("list", COJsonUtil.getJsonArrayFromList(list2));
		rtnMap.put("srvyList", COJsonUtil.getJsonArrayFromList(srvyList));
		rtnMap.put("asgnTaskCd", emfMap.getString("asgnTaskCd"));
		rtnMap.put("addAnswList", COJsonUtil.getJsonArrayFromList(addAnswList));
		
    	return rtnMap;
    }
    
    /**
     * 딜러 Status Count
     * 
     * @param emfMap 데이터
	 * @return
     */
    public EmfMap getDlrSttsCnt(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();
    	
    	rtnMap.put("stts", COJsonUtil.getJsonStringFromMap(hSAFeedbackDAO.getDlrSttsCnt(emfMap)));
    	
    	return rtnMap;
    }
    
    /**
     * 고객 List
     * 
     * @param emfMap 데이터
	 * @return
     */
    public EmfMap selectCstmrList(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();
    	
    	String[] strtDt = emfMap.getString("strtDt").split("/");
    	String[] endDt = emfMap.getString("endDt").split("/");
    	
    	Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(endDt[1]), Integer.parseInt(endDt[0])-1, 1);
    	
    	emfMap.put("strtDt", strtDt[0] + "/01/" + strtDt[1]);
    	emfMap.put("endDt", endDt[0] + "/"+cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/" + endDt[1]);
    	
    	PaginationInfo paginationInfo = new COPaginationUtil().getPage(emfMap);
    	
		// 리스트 가져오기
		List<EmfMap> list = hSAFeedbackDAO.selectCstmrList(emfMap);
		
		//2020.06.08 암호화
		List<EmfMap> list2 = new ArrayList<EmfMap>();
		for(int i = 0, max = list.size(); i < max; i++){
			EmfMap tmpMap = list.get(i);
			tmpMap.put("email", "".equals(tmpMap.getString("email")) ? "" : SeedCipher.decrypt(tmpMap.getString("email"),ENCODE));
			tmpMap.put("hp", "".equals(tmpMap.getString("hp")) ? "" : SeedCipher.decrypt(tmpMap.getString("hp"),ENCODE));
			
			list2.add(tmpMap);
		}
		
		// 총 건수 가져오기
		int totCnt = hSAFeedbackDAO.selectCstmrListTotCnt(emfMap);
		
		paginationInfo.setTotalRecordCount(totCnt);
		
		emfMap.put("strtDt", strtDt[0] + "/" + strtDt[1]);
        emfMap.put("endDt", endDt[0] + "/" + endDt[1]);
		
		rtnMap.put("totCnt", totCnt);
		rtnMap.put("pageIndex", paginationInfo.getCurrentPageNo());
		rtnMap.put("recordPerPage", paginationInfo.getRecordCountPerPage());
		rtnMap.put("totPage", paginationInfo.getTotalPageCount());
		rtnMap.put("list", COJsonUtil.getJsonArrayFromList(list2));
		
    	return rtnMap;
    }
    
    /**
     * 고객 Status Count
     * 
     * @param emfMap 데이터
	 * @return
     */
    public EmfMap getCstmrSttsCnt(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();
    	
    	rtnMap.put("stts", COJsonUtil.getJsonStringFromMap(hSAFeedbackDAO.getCstmrSttsCnt(emfMap)));
    	
    	return rtnMap;
    }
    
    /**
     * 고객 Details
     * 
     * @param emfMap 데이터
	 * @return
     */
    public EmfMap selectCstmrDtl(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();
    	
    	String cstmrCnctKey = emfMap.getString("cstmrCnctKey");
		
    	if (!"".equals(cstmrCnctKey))
    	{
    		
    		if(emfMap.containsKey("strtDt") && emfMap.containsKey("endDt") && emfMap.getString("strtDt")!= null && emfMap.getString("endDt") != null){
    			String[] strtDt = emfMap.getString("strtDt").split("/");
            	String[] endDt = emfMap.getString("endDt").split("/");
            	
            	Calendar cal = Calendar.getInstance();
        		cal.set(Integer.parseInt(endDt[1]), Integer.parseInt(endDt[0])-1, 1);
            	
            	emfMap.put("strtDt", strtDt[0] + "/01/" + strtDt[1]);
            	emfMap.put("endDt", endDt[0] + "/"+cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/" + endDt[1]);
    		}
    		
    		
    		String [] tempArr = EgovStringUtil.split(cstmrCnctKey, "_");
			
    		emfMap.put("sendPddg", tempArr[0]);
    		emfMap.put("vhcltNo", tempArr[1]);
    		emfMap.put("vhcltSort", tempArr[2]);
    		
    		EmfMap info = hSAFeedbackDAO.selectCstmrDtl(emfMap);
    		
    		if (info != null)
    		{
    			if (emfMap.getString("siteLanguage") != null && !"".equals(emfMap.getString("siteLanguage"))) {
    			}else{
    				emfMap.put("siteLanguage","en");
    			}
    			// 2020.06.08 암호화
    			info.put("email", "".equals(info.getString("email")) ? "" : SeedCipher.decrypt(info.getString("email"),ENCODE));
    			info.put("hp", "".equals(info.getString("hp")) ? "" : SeedCipher.decrypt(info.getString("hp"),ENCODE));
    			
    			rtnMap.put("info", COJsonUtil.getJsonStringFromMap(info));
    			rtnMap.put("bscSrvyAnsw", COJsonUtil.getJsonArrayFromList(hSAFeedbackDAO.selectCstmrBscSrvyAnsw(emfMap)));
    			rtnMap.put("addSrvyAnsw", COJsonUtil.getJsonArrayFromList(hSAFeedbackDAO.selectCstmrAddSrvyAnsw(emfMap)));
    			
				List<String> dlrCdList = (List<String>) emfMap.get("admDlrCdList");

				if (dlrCdList.contains(info.getString("dlrCd")))
    			{
    				hSAFeedbackDAO.updateCstmrSrvyOpnYn(emfMap);
    			}
    		}
    	}
    	
    	return rtnMap;
    }
    
    /**
     * 엑셀다운로드
     * 
     * @param emfMap 데이터
	 * @return
     */
    public EmfMap excelCstmrList(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();
    	
    	if(emfMap.containsKey("strtDt") && emfMap.containsKey("endDt") && emfMap.getString("strtDt")!= null && emfMap.getString("endDt") != null){
			String[] strtDt = emfMap.getString("strtDt").split("/");
        	String[] endDt = emfMap.getString("endDt").split("/");
        	
        	Calendar cal = Calendar.getInstance();
    		cal.set(Integer.parseInt(endDt[1]), Integer.parseInt(endDt[0])-1, 1);
        	
        	emfMap.put("strtDt", strtDt[0] + "/01/" + strtDt[1]);
        	emfMap.put("endDt", endDt[0] + "/"+cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/" + endDt[1]);
		}
		
    	rtnMap.put("asgnTaskCd", emfMap.getString("asgnTaskCd"));
    	List<EmfMap> list = hSAFeedbackDAO.excelCstmrList(emfMap);
    	//2020.06.08 암호화
    	List<EmfMap> list2 = new ArrayList<EmfMap>();
    	for(int i = 0, max = list.size(); i < max; i++){
    		EmfMap tmpMap = list.get(i);
    		tmpMap.put("email", "".equals(tmpMap.getString("email")) ? "" : SeedCipher.decrypt(tmpMap.getString("email"),ENCODE));
    		tmpMap.put("hp", "".equals(tmpMap.getString("hp")) ? "" : SeedCipher.decrypt(tmpMap.getString("hp"),ENCODE));
    			
    		list2.add(tmpMap);
    	}
		rtnMap.put("list", COJsonUtil.getJsonArrayFromList(list2));
		
		return rtnMap;
    }
}