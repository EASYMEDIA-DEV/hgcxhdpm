package com.easymedia.api.controller;

import com.easymedia.dto.EmfMap;
import com.easymedia.service.COFSysLogService;
import com.easymedia.service.EgovCmmUseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * <pre> 
 * 시스템로그(이용로그)를 위한 Controller
 * </pre>
 * 
 * @ClassName		: COFSysLogController.java
 * @Description		: 시스템로그(이용로그)를 위한 Controller
 * @author 김진수
 * @since 2019.01.14
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		 since		  author			   description
 *    ==========    ==========    ==============================
 *    2019.01.14	  김진수			    최초 생성
 * </pre>
 */
@Tag(name = "시스템로그", description = "시스템로그")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/co/cof")
public class COFSysLogController {

	/** 코드 서비스 **/
    private final EgovCmmUseService cmmUseService;

	/** 시스템 로그 서비스 **/
	private final COFSysLogService cOFSysLogService;

	/**
	 * 로그 목록을 조회한다. [ List Page ]
	 * 
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
	@RequestMapping(value="/list.do")
	public String logSelectSysLogList(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("paramMap", emfMap);
			
			// 공통코드 배열 셋팅
			ArrayList<String> codeList = new ArrayList<String>();
			
			// 관리자 권한
			codeList.add("SYSTEM_LOG");
			codeList.add("LIST_CNT");
			
			// 정의된 코드id값들의 상세 코드 맵 반환		
			modelMap.addAttribute("rtnCode", cmmUseService.getCmmCodeBindAll(codeList));
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw he;
		} 
		
		return "mngwserc/co/cof/COFSysLogList";
	}
	
	/**
	 * 로그 목록을 조회한다. [ List Ajax ]
	 * 
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
	@RequestMapping(value="/list.ajax")
	public String selectSampleListAjax(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("rtnData", cOFSysLogService.logSelectSysLogList(emfMap));
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw he;
		} 

		return "mngwserc/co/cof/COFSysLogListAjax";
	}
	
	/**
	 * 로그 목록을 조회한다. (엑셀 다운로드)
	 * 
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
	@RequestMapping(value="/excel.do")
	public String logExcelSysLogList(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("rtnData", cOFSysLogService.logExcelSysLogList(emfMap));
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw he;
		} 
		
		return "mngwserc/co/cof/COFSysLogExcel.excel";
	}
}