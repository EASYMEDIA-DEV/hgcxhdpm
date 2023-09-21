package com.easymedia.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


import com.easymedia.api.annotation.ApiData;
import com.easymedia.dto.EmfMap;
import com.easymedia.service.SBACountryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


/**
 * <pre> 
 * SBABranchController Controller
 * </pre>
 * 
 * @ClassName		: SBABranchController.java
 * @Description		: 국가관리(지역본부) Controller
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

@Tag(name = "국가관리", description = "국가관리")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mngwserc/sb/sba")
public class SBACountryController {
	
	/**국가관리 service**/
	private final SBACountryService sBACountryService;
	
	/**
	 * 국가관리 List Page
	 * 
	 * @param emfMap 데이터
	 * @return String View URL
	 */
	@RequestMapping(value="/list")
	public EmfMap selectCountryListPage(EmfMap emfMap) throws Exception
	{
		try
		{
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw new Exception(he.getMessage());
		} 

		return emfMap;
	}
	
	/**
	 * 국가관리 List Ajax
	 * 
	 * @param emfMap 데이터
	 * @return String View URL
	 */
	@PostMapping(value="/list-axios")
	public EmfMap selectCountryListAjax(@ApiData @RequestBody EmfMap emfMap) throws Exception {
		EmfMap emfMap1;
		try {
			emfMap1 = sBACountryService.selectCountryList(emfMap);
		} catch (Exception he) {
			if (log.isDebugEnabled()) {
				log.debug(he.getMessage());
			}
			throw new Exception(he.getMessage());
		}

		return emfMap1;
	}
	
	/**
	 * 국가관리 등록/수정 Page
	 * 
	 * @param emfMap 데이터
	 * @return String View URL
	 */
	@RequestMapping(value="/write")
	public EmfMap selectCountryPage(@ApiData EmfMap emfMap) throws Exception
	{
		EmfMap emfMap1 = null;
		try
		{
			emfMap1 = sBACountryService.selectCountryDtl(emfMap);
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw new Exception(he.getMessage());
		}

		return emfMap1;
	}
	
	/**
	 * 국가관리 Insert Ajax
	 * 
	 * @param emfMap 데이터
	 * @return String View URL
	 */
	@RequestMapping(value="/insert.ajax", method=RequestMethod.POST)
	public String insertCountry(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("actCnt", sBACountryService.insertCountry(emfMap));
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw new Exception(he.getMessage());
		}

		return "jsonView";
	}	
	
	/**
	 * 국가관리 update Ajax
	 * 
	 * @param emfMap 데이터
	 * @return String View URL
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public void updateCountry(@ApiData @RequestBody EmfMap emfMap) throws Exception
	{
		System.out.println("emfMap:::"+emfMap);
		try
		{
			sBACountryService.updateCountry(emfMap);
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw new Exception(he.getMessage());
		}

	}		
	
	/**
	 * 국가관리 등록된 국가가 있는지 조회 Ajax
	 * 
	 * @param emfMap 데이터
	 * @return String View URL
	 */
	@RequestMapping(value="/getNtnCdCnt.ajax", method=RequestMethod.POST)
	public String getNtnCd(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("actCnt", sBACountryService.getNtnCdCnt(emfMap));
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw new Exception(he.getMessage());
		}

		return "jsonView";
	}	
	
	/**
	 * 국가관리 List 엑셀 다운로드
	 *
	 * @param emfMap 데이터
	 * @return String View URL
	 */
	@RequestMapping(value="/excelDown.do")
	public String selectCountryListExcel(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			//국가정보 리스트 다운로드
			modelMap.addAttribute("rtnData", sBACountryService.excelCountryList(emfMap));
		}
		catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw new Exception(he.getMessage());
		}

		return "mngwserc/sb/sba/SBACountryListExcel.excel";
	}
	
	/**
	 * AnnualTarget update Ajax
	 * 
	 * @param emfMap 데이터
	 * @return String View URL
	 */
	@RequestMapping(value="/delTrgt.ajax", method=RequestMethod.POST)
	public String targetUpdate(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			emfMap.put("actCnt", sBACountryService.deleteTargetVal(emfMap));
			modelMap.addAttribute("rtnData", emfMap);
			
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw new Exception(he.getMessage());
		}

		return "jsonView";
	}	

}