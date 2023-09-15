package com.easymedia.service.impl;

import java.util.ArrayList;
import java.util.List;


import com.easymedia.dto.EmfMap;
import com.easymedia.service.EgovCmmUseService;
import com.easymedia.service.EgovUserDetailsHelper;
import com.easymedia.service.SBACountryService;
import com.easymedia.service.dao.SBACountryDAO;
import com.easymedia.utility.COJsonUtil;
import com.easymedia.utility.COPaginationUtil;
import com.easymedia.utility.EgovFileMngUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;


/**
 * <pre> 
 * SBACountryServiceImpl ServiceImpl
 * </pre>
 * 
 * @ClassName		: SBACountryServiceImpl.java
 * @Description		: 국가관리 ServiceImpl
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
@Slf4j
@Service
@RequiredArgsConstructor
public class SBACountryServiceImpl  implements SBACountryService {
	
   	private final EgovFileMngUtil fileMngUtil;
	
//    private final EgovFileMngService fileMngService;
	
    private final EgovCmmUseService cmmUseService;	
	
	/**국가관리 DAO**/
    private final SBACountryDAO sBACountryDAO;

//	@Value("${Globals.atchFileSize}")
//	private String atchFileSize;
//	
//	@Value("${Globals.UploadMimeType}")
//	private String atchFileExtns;
	
	/**
     * 국가관리 List
     * 
     * @param emfMap 데이터
	 * @return
	 * @throws 비지니스 로직이나 DAO 처리 중 에러가 발생할 경우 Exception을 Throw 한다.
     */
    public EmfMap selectCountryList(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();
    	
    	PaginationInfo paginationInfo = new COPaginationUtil().getPage(emfMap);
    	
		// 리스트 가져오기
		List<EmfMap> list = sBACountryDAO.selectCountryList(emfMap);

		// 총 건수 가져오기
		int totCnt = sBACountryDAO.selectCountryListTotCnt(emfMap);

		paginationInfo.setTotalRecordCount(totCnt);
		
		rtnMap.put("totCnt", totCnt);
		rtnMap.put("pageIndex", paginationInfo.getCurrentPageNo());
		rtnMap.put("recordPerPage", paginationInfo.getRecordCountPerPage());
		rtnMap.put("totPage", paginationInfo.getTotalPageCount());
		rtnMap.put("list", COJsonUtil.getJsonArrayFromList(list));
		
    	return rtnMap;
    }
    
	/**
     * 국가관리 등록된 국가가 있는지 조회
     * 
     * @param emfMap 데이터
	 * @return
     */
	public int getNtnCdCnt(EmfMap emfMap) throws Exception
	{		
		return sBACountryDAO.getNtnCdCnt(emfMap);
	}    
    
	/**
     * 국가관리 Insert
     * 
     * @param emfMap 데이터
	 * @return
     */
	public int insertCountry(EmfMap emfMap) throws Exception
	{
		
		int rtnCnt = 0;
		
		try
		{
			EmfMap lgnMap = (EmfMap) EgovUserDetailsHelper.getAuthenticatedUser();
			
			List<String> lgugCdArr = emfMap.getList("lgugCd");	 //언어리스트
			List<String> sortArr = emfMap.getList("sort");	 //언어 정렬리스트
			
			List<String> hS1TrgtYearArr = emfMap.getList("hS1Year");	// HGSI Sales Year 리스트
			List<String> hS2TrgtYearArr = emfMap.getList("hS2Year");	// HGSI Service Year 리스트
			List<String> hS1TrgtArr = emfMap.getList("hS1Trgt");		// HGSI Sales Target 값 리스트
			List<String> hS2TrgtArr = emfMap.getList("hS2Trgt");		// HGSI Service Target 값 리스트
			
			List<String> nS1TrgtYearArr = emfMap.getList("nS1Year");	// NPS Sales Year 리스트
			List<String> nS2TrgtYearArr = emfMap.getList("nS2Year");	// NPS Service Year 리스트
			List<String> nS1TrgtArr = emfMap.getList("nS1Trgt");		// NPS Sales Target 값 리스트
			List<String> nS2TrgtArr = emfMap.getList("nS2Trgt");		// NPS Service Target 값 리스트
						
			emfMap.put("regId", lgnMap.get("id"));
			emfMap.put("regIp", lgnMap.get("loginIp"));
			emfMap.put("modId", lgnMap.get("id"));
			emfMap.put("modIp", lgnMap.get("loginIp"));			

			if(lgugCdArr.size()>0){

				emfMap.put("lgugCd", lgugCdArr.get(0));
				rtnCnt = sBACountryDAO.insertCountry(emfMap);				
				
				for(int i=0; i<lgugCdArr.size(); i++){
					emfMap.put("lgugCd", lgugCdArr.get(i));
					emfMap.put("sort", sortArr.get(i));
					rtnCnt = sBACountryDAO.insertCountryLgug(emfMap);
				}
				
				// HGSI Sales Target 설정
				if(hS1TrgtYearArr.size() > 0 && !"0".equals(hS1TrgtYearArr.get(0))){
					for(int i = 0, max=hS1TrgtYearArr.size(); i < max; i++ ){
						emfMap.put("year", hS1TrgtYearArr.get(i));
						emfMap.put("asgnTaskCd", "Sales");
						emfMap.put("annualTrgtTypeCd","H");
						emfMap.put("trgtVal", hS1TrgtArr.get(i));
						sBACountryDAO.insertCountryTarget(emfMap);
					}
				}
				
				// HGSI Service Target 설정
				if(hS2TrgtYearArr.size() > 0 && !"0".equals(hS2TrgtYearArr.get(0))){
					for(int i = 0, max=hS2TrgtYearArr.size(); i < max; i++ ){
						emfMap.put("year", hS2TrgtYearArr.get(i));
						emfMap.put("asgnTaskCd", "Service");
						emfMap.put("annualTrgtTypeCd","H");
						emfMap.put("trgtVal", hS2TrgtArr.get(i));
						sBACountryDAO.insertCountryTarget(emfMap);
					}
				}
				
				// NPS Sales Target 설정
				if(nS1TrgtYearArr.size() > 0 && !"0".equals(nS1TrgtYearArr.get(0))){
					for(int i = 0, max=nS1TrgtYearArr.size(); i < max; i++ ){
						emfMap.put("year", nS1TrgtYearArr.get(i));
						emfMap.put("asgnTaskCd", "Sales");
						emfMap.put("annualTrgtTypeCd","N");
						emfMap.put("trgtVal", nS1TrgtArr.get(i));
						sBACountryDAO.insertCountryTarget(emfMap);
					}
				}
				
				// NPS Service Target 설정
				if(nS2TrgtYearArr.size() > 0 && !"0".equals(nS2TrgtYearArr.get(0))){
					for(int i = 0, max=nS2TrgtYearArr.size(); i < max; i++ ){
						emfMap.put("year", nS2TrgtYearArr.get(i));
						emfMap.put("asgnTaskCd", "Service");
						emfMap.put("annualTrgtTypeCd","N");
						emfMap.put("trgtVal", nS2TrgtArr.get(i));
						sBACountryDAO.insertCountryTarget(emfMap);
					}
				}
			}			
			
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw new Exception(he.getMessage());
		}
		return rtnCnt;		
	}
	
	/**
     * 국가관리 update
     * 
     * @param emfMap 데이터
	 * @return
     */
	public int updateCountry(EmfMap emfMap) throws Exception
	{
		
		int rtnCnt = 0;
		
		try
		{
			if(!"".equals(emfMap.getString("detailsKey")))
			{
				EmfMap lgnMap = (EmfMap) EgovUserDetailsHelper.getAuthenticatedUser();
				List<String> lgugCdArr = emfMap.getList("lgugCd");	 		//언어리스트
				List<String> sortArr = emfMap.getList("sort");	 			//언어 정렬리스트
				List<String> hS1TrgtYearArr = emfMap.getList("hS1Year");	// HGSI Sales Year 리스트
				List<String> hS2TrgtYearArr = emfMap.getList("hS2Year");	// HGSI Service Year 리스트
				List<String> hS1TrgtArr = emfMap.getList("hS1Trgt");		// HGSI Sales Target 값 리스트
				List<String> hS2TrgtArr = emfMap.getList("hS2Trgt");		// HGSI Service Target 값 리스트
				
				List<String> nS1TrgtYearArr = emfMap.getList("nS1Year");	// NPS Sales Year 리스트
				List<String> nS2TrgtYearArr = emfMap.getList("nS2Year");	// NPS Service Year 리스트
				List<String> nS1TrgtArr = emfMap.getList("nS1Trgt");		// NPS Sales Target 값 리스트
				List<String> nS2TrgtArr = emfMap.getList("nS2Trgt");		// NPS Service Target 값 리스트
				
				emfMap.put("ntnCd", emfMap.getString("detailsKey"));
				emfMap.put("regId", lgnMap.get("id"));
				emfMap.put("regIp", lgnMap.get("loginIp"));
				emfMap.put("modId", lgnMap.get("id"));
				emfMap.put("modIp", lgnMap.get("loginIp"));		
				
				// Target 삭제
				sBACountryDAO.deleteTargetVal(emfMap);
			
				if(lgugCdArr.size()>0){
					
					emfMap.put("lgugCd", lgugCdArr.get(0));
					rtnCnt = sBACountryDAO.updateCountry(emfMap);
					
					rtnCnt = sBACountryDAO.deleteCountryLgug(emfMap);
					
					for(int i=0; i<lgugCdArr.size(); i++){
						emfMap.put("lgugCd", lgugCdArr.get(i));
						emfMap.put("sort", sortArr.get(i));
						rtnCnt = sBACountryDAO.insertCountryLgug(emfMap);
					}
				}				
				
				// HGSI Sales Target 설정
				if(hS1TrgtYearArr.size() > 0 && !"0".equals(hS1TrgtYearArr.get(0))){
					for(int i = 0, max=hS1TrgtYearArr.size(); i < max; i++ ){
						emfMap.put("year", hS1TrgtYearArr.get(i));
						emfMap.put("asgnTaskCd", "Sales");
						emfMap.put("annualTrgtTypeCd","H");
						emfMap.put("trgtVal", hS1TrgtArr.get(i));
						sBACountryDAO.insertCountryTarget(emfMap);
					}
				}
				
				// HGSI Service Target 설정
				if(hS2TrgtYearArr.size() > 0 && !"0".equals(hS2TrgtYearArr.get(0))){
					for(int i = 0, max=hS2TrgtYearArr.size(); i < max; i++ ){
						emfMap.put("year", hS2TrgtYearArr.get(i));
						emfMap.put("asgnTaskCd", "Service");
						emfMap.put("annualTrgtTypeCd","H");
						emfMap.put("trgtVal", hS2TrgtArr.get(i));
						sBACountryDAO.insertCountryTarget(emfMap);
					}
				}
				
				// NPS Sales Target 설정
				if(nS1TrgtYearArr.size() > 0 && !"0".equals(nS1TrgtYearArr.get(0))){
					for(int i = 0, max=nS1TrgtYearArr.size(); i < max; i++ ){
						emfMap.put("year", nS1TrgtYearArr.get(i));
						emfMap.put("asgnTaskCd", "Sales");
						emfMap.put("annualTrgtTypeCd","N");
						emfMap.put("trgtVal", nS1TrgtArr.get(i));
						sBACountryDAO.insertCountryTarget(emfMap);
					}
				}
				
				// NPS Service Target 설정
				if(nS2TrgtYearArr.size() > 0 && !"0".equals(nS2TrgtYearArr.get(0))){
					for(int i = 0, max=nS2TrgtYearArr.size(); i < max; i++ ){
						emfMap.put("year", nS2TrgtYearArr.get(i));
						emfMap.put("asgnTaskCd", "Service");
						emfMap.put("annualTrgtTypeCd","N");
						emfMap.put("trgtVal", nS2TrgtArr.get(i));
						sBACountryDAO.insertCountryTarget(emfMap);
					}
				}
			}			
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw new Exception(he.getMessage());
		}		
		
		
		return rtnCnt;
	}	
	
	/**
	 * 국가관리 상세 조회
	 * 
	 * @param emfMap 데이터
	 * @return EmfMap 조회조건에 검색된 데이터
	 */
	public EmfMap selectCountryDtl(EmfMap emfMap) throws Exception
	{
    	EmfMap rtnMap = new EmfMap();
    	
    	if (!"".equals(emfMap.getString("detailsKey")))
    	{
    		rtnMap.put("info", COJsonUtil.getJsonStringFromMap(sBACountryDAO.selectCountryDtl(emfMap)));
    		rtnMap.put("lgugDtlList", COJsonUtil.getJsonArrayFromList(sBACountryDAO.selectCountryLgugList(emfMap)));
    		emfMap.put("annualTrgtTypeCd","H");
    		emfMap.put("asgnTaskCd","Sales");
    		rtnMap.put("hSalesTrgtList", COJsonUtil.getJsonArrayFromList(sBACountryDAO.selectTrgtList(emfMap)));
    		emfMap.put("asgnTaskCd","Service");
    		rtnMap.put("hServiceTrgtList", COJsonUtil.getJsonArrayFromList(sBACountryDAO.selectTrgtList(emfMap)));
    		
    		emfMap.put("annualTrgtTypeCd","N");
    		emfMap.put("asgnTaskCd","Sales");
    		rtnMap.put("nSalesTrgtList", COJsonUtil.getJsonArrayFromList(sBACountryDAO.selectTrgtList(emfMap)));
    		emfMap.put("asgnTaskCd","Service");
    		rtnMap.put("nServiceTrgtList", COJsonUtil.getJsonArrayFromList(sBACountryDAO.selectTrgtList(emfMap)));
    	}
    	
		//공통코드 배열 셋팅
		ArrayList<String> cdDtlList = new ArrayList<String>();
		cdDtlList.add("NATN_CD");	//국가코드
		cdDtlList.add("LGUG_CD");	//언어코드
		//정의된 코드id값들의 상세 코드 맵 반환		
		rtnMap.put("natnCdList", cmmUseService.getCmmCodeBindAll(cdDtlList));    	
		
		return rtnMap;
	}
	
	/**
	 * 국가관리 > 등록된 리스트 조회
	 * 
	 * @param emfMap 데이터
	 * @return EmfMap 조회조건에 검색된 데이터
	 */
	public EmfMap getCountryList(EmfMap emfMap) throws Exception
	{
		// 리스트 가져오기
		List<EmfMap> list = sBACountryDAO.getCountryList(emfMap);
		emfMap.put("list", COJsonUtil.getJsonArrayFromList(list));
		return emfMap;
	}
	
	/**
     * 국가관리 List ( 엑셀 다운로드 )
     *
     * @param emfMap 데이터
     * @return
     */
    public EmfMap excelCountryList(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();
    	// 리스트 가져오기
    	List<EmfMap> list = sBACountryDAO.excelCountryList(emfMap);

    	rtnMap.put("list", COJsonUtil.getJsonArrayFromList(list));

    	return rtnMap;
    }

	@Override
	public int deleteTargetVal(EmfMap emfMap) throws Exception {
		
		int result = 0;
		
		if(("0".equals(emfMap.getString("year")) || "".equals(emfMap.getString("year"))) && "".equals(emfMap.getString("trgtVal"))){
			// 삭제할 DB가 없기 때문에 화면상만 삭제
		}else{
			emfMap.put("useYn", "N");
			result = sBACountryDAO.deleteTargetVal(emfMap);
		}
		
		return result;
	}
	
}