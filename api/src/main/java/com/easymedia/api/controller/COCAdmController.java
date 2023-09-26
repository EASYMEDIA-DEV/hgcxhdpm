package com.easymedia.api.controller;

import com.easymedia.api.annotation.ApiData;
import com.easymedia.dto.EmfMap;
import com.easymedia.error.ErrorResponse;
import com.easymedia.service.COCAdmService;
import com.easymedia.service.CODMenuService;
import com.easymedia.service.EgovCmmUseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre> 
 * 관리자 계정 관리 Controller
 * </pre>
 * 
 * @ClassName		: COCAdmController.java
 * @Description		: 관리자 계정 관리 Controller
 * @author 허진영
 * @since 2019.01.14
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.01.14		허진영					최초 생성
 * </pre>
 */
@Controller
@Tag(name = "관리자 계정 관리", description = "관리자 계정 관리")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/co/coc")
public class COCAdmController {
	
	/*코드*/
    private final EgovCmmUseService cmmUseService;
	/*서비스*/
	private final COCAdmService cOCAdmService;
	/*메뉴*/
	private final CODMenuService cODMenuService;
	
	/**
	 * 관리자 계정 관리 List Page
	 * 
	 * @param emfMap
	 * @return String View URL
	 * @throws 
	 */
	@RequestMapping(value="/list.do")
	public String selectAdmListPage(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("paramMap", emfMap);
			
			// 공통코드 배열 셋팅
			ArrayList<String> codeList = new ArrayList<String>();
			
			// 관리자 권한
			codeList.add("AUTH_CD");
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
		
		return "mngwserc/co/coc/COCAdmList";
	}

	@Operation(summary = "관리자 계정 리스트", description = "")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@GetMapping(value="/list")
	public EmfMap selectAdmListAjax(@ApiData EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		EmfMap rtnMap = null;
		try
		{
			rtnMap = cOCAdmService.selectAdmList(emfMap);
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw he;
		} 
		return rtnMap;
	}

	@Operation(summary = "관리자 계정 삭제", description = "")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@PostMapping(value="/delete")
	public int deleteSample(@ApiData EmfMap emfMap) throws Exception
	{
		int actCnt = 0;
		try
		{
			actCnt = cOCAdmService.deleteAdm(emfMap);
		}
		catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
			}
			throw he;
		}

		return actCnt;
	}

	@Operation(summary = "관리자 계정 상세", description = "")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@PostMapping(value="/detail")
	public EmfMap selectAdmDtl(@ApiData  EmfMap emfMap) throws Exception
	{
		EmfMap rtnMap = null;
		try
		{
			// Details
			rtnMap = cOCAdmService.selectAdmDtl(emfMap);
		}
		catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw he;
		}

		return rtnMap;
	}
	
	/**
	 * 관리자 계정 관리 Insert Ajax
	 * 
	 * @param emfMap
	 * @return String View URL
	 * @throws 
	 */
	@RequestMapping(value="/insert.ajax", method=RequestMethod.POST)
	public String insertSample(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("actCnt", cOCAdmService.insertAdm(emfMap));
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw he;
		}

		return "jsonView";
	}
	
	/**
	 * 관리자 계정 관리 Update Ajax
	 * 
	 * @param emfMap
	 * @return String View URL
	 * @throws 
	 */
	@RequestMapping(value="/update.ajax", method=RequestMethod.POST)
	public String updateAdm(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			emfMap.put("isAdmMng", "Y");
			
			modelMap.addAttribute("actCnt", cOCAdmService.updateAdm(emfMap));
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw he;
		}

		return "jsonView";
	}

	/**
	 * 관리자 계정 중복 Check Ajax
	 * 
	 * @param emfMap
	 * @return String View URL
	 * @throws 
	 */	
	@RequestMapping(value="/id-overap-check.ajax", method=RequestMethod.POST)
	public String getIdCheck(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			int chkCnt = cOCAdmService.getIdCheck(emfMap);
			
			if (chkCnt == 0)
			{
				modelMap.addAttribute("useYn", "N");
			}
			else
			{
				modelMap.addAttribute("useYn", "Y");
			}
		}
		catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw he;
		}

		return "jsonView";
	}
	
	/**
	 * 관리자 계정 메뉴 List Ajax
	 * 
	 * @param emfMap
	 * @return String View URL
	 * @throws 
	 */	
	@RequestMapping(value="/menu-list.ajax")
	public void seleteMenuList(EmfMap emfMap, ModelMap modelMap, HttpServletResponse response) throws Exception
	{
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();

        try 
        {
            emfMap.put("isChkd", "Y");
            emfMap.put("menuType", "admin");
        	
        	List<EmfMap> menuList = cODMenuService.selectMenuList(emfMap);
            
            JSONArray jSONArray = new JSONArray();
            
            int paramSeq = Integer.parseInt(emfMap.getString("menuSeq"));
            int startNum = 0;

            jSONArray = cODMenuService.getJsonData(menuList, startNum, paramSeq);
            
            out.print(jSONArray);
            jSONArray = null;
        }
        catch (Exception he) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug(he.getMessage());
            }
			throw he;
		}
        finally 
        {
            out.close();
        }
	}
}