package com.easymedia.api.controller;

import com.easymedia.api.annotation.ApiData;
import com.easymedia.dto.EmfMap;
import com.easymedia.service.COBLgnService;
import com.easymedia.service.COCAdmService;
import com.easymedia.service.EgovUserDetailsHelper;
import com.easymedia.utility.EgovNetworkState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * <pre>
 * 로그인/로그아웃을 위한 Controller
 * </pre>
 *
 * @ClassName		: COBLgnController.java
 * @Description		: 로그인/로그아웃을 위한 Controller
 * @author 허진영
 * @since 2017.04.03
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *   2017.04.03			허진영			 		최초생성
 * </pre>
 */
@Tag(name = "로그인", description = "로그인")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class COBLgnController {

	/** 로그인 서비스 **/
    private final COBLgnService cOBLgnService;

	/** 관리자 서비스 **/
	private final COCAdmService cOCAdmService;

	/** HTTP ADMIN URL **/
	@Value("${globals.http-admin-url}")
	private String httpAdminUrl;

	/** HTTPS ADMIN URL **/
	@Value("${globals.https-admin-url}")
	private String httpsAdminUrl;

	/** HTTP HDPM ADMIN URL **/
	@Value("${globals.http-hdpm-admin-url}")
	private String httpHdpmAdminUrl;

	/** HTTP HDPM PORT **/
	@Value("${globals.http-hdpm-port}")
	private String httpHdpmPort;

    /**
	 * 로그인을 처리한다.
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
	@Operation(summary = "로그인", description = "로그인")
    @RequestMapping(value="/setLogin", method=RequestMethod.POST)
    public EmfMap actionLogin(@ApiData EmfMap emfMap, HttpServletRequest request) throws Exception
    {
		try
		{
			// 보안 처리(로그인 세션 변경)
			if (request.getSession(false) != null)
			{
				request.getSession().invalidate();
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

		return  cOBLgnService.actionLogin(emfMap, request);
	}

    /**
	 * 로그아웃을 처리한다.
	 *
	 * @param emfMap
	 * @return String View URL
	 * @exception Exception
	 */
    @RequestMapping(value="/setLogout")
    public String actionLogout(@ApiData EmfMap emfMap, ModelMap modelMap, HttpServletRequest request) throws Exception
    {
    	try
    	{
    		cOBLgnService.actionLogout(emfMap, request);

    		// 보안 처리(로그인 세션 변경)
    		if (request.getSession() != null)
    		{
    			request.getSession().invalidate();
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

    	return "redirect:/mngwsercgateway/getLogin.do";
    }
    
    /**
   	 * HDIS 페이지 이동
   	 *
   	 * @param emfMap
   	 * @return String View URL
   	 * @exception Exception
   	 */
      @RequestMapping(value="/mngwserc/**/goToHDPM.do")
      public String goToHDPM(@ApiData EmfMap emfMap, ModelMap modelMap, HttpServletRequest request) throws Exception
    {
    	String redirectUrl = "", pddg="";
    	    	    	    	    	  
    	try
    	{
    		cOBLgnService.actionLogout(emfMap, request);
    		pddg = cOBLgnService.getAuthPddg(emfMap).getString("pddg");

    		// 보안 처리(로그인 세션 변경)
    		if (request.getSession() != null)
    		{
    			request.getSession().invalidate();
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
    	
    	StringBuffer url = request.getRequestURL(); 
	    	
    	//개발일때 상위 도메인 호출 방식으로 변경 2022.07.14
    	if(url.indexOf("easymedia") != -1){
    		redirectUrl = httpHdpmAdminUrl;
    	}else{
    		redirectUrl = httpsAdminUrl;
    		redirectUrl = redirectUrl.substring(0, redirectUrl.lastIndexOf(":"))  + ":" + httpHdpmPort;
    	}

    	return "redirect:" + redirectUrl + "/auth/" + pddg + "/set-admin-login.ajax?id=" + emfMap.getString("id") ;
    }
    
    /**
	 * 비밀번호 오류 처리
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
    @RequestMapping(value="/mngwsercgateway/actionLgnFail.ajax", method=RequestMethod.POST)
    public String actionLgnFail(@ApiData EmfMap emfMap, ModelMap modelMap, HttpServletRequest request) throws Exception
    {
    	try
    	{
    		cOBLgnService.actionLgnFail(emfMap, request);
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
	 * 비밀번호 실패 카운트 초기화
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
    @RequestMapping(value="/mngwsercgateway/actionLgnFailReset.ajax", method=RequestMethod.POST)
    public String actionLgnFailReset(@ApiData EmfMap emfMap, ModelMap modelMap, HttpServletRequest request) throws Exception
    {
    	try
    	{
    		cOBLgnService.actionLgnFailReset(emfMap, request);
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
	 * 마지막 접속 정보를 가져온다
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
    @RequestMapping(value="/mngwsercgateway/getLastLgnInfo.ajax", method=RequestMethod.POST)
    public String getLastLgnInfo(@ApiData EmfMap emfMap, ModelMap modelMap, HttpServletRequest request) throws Exception
    {
    	try
    	{
    		EmfMap infoMap = cOBLgnService.getLastLgnInfo(emfMap, request);
    		
    		modelMap.addAttribute("rtnData", infoMap);
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
   	 * 비밀번호변경 페이지
   	 *
   	 * @param emfMap
	 * @return String View URL
   	 * @throws Exception
   	 */
   	@RequestMapping(value="/mngwsercgateway/getPwdChng.do")
   	public String getPwdChngPage(@ApiData EmfMap emfMap, ModelMap modelMap) throws Exception
   	{
   		String url = "";

		try
		{
			EmfMap lgnMap = (EmfMap) RequestContextHolder.getRequestAttributes().getAttribute("tmpLgnMap", RequestAttributes.SCOPE_SESSION);

			if (lgnMap != null && !emfMap.containsKey("reset"))
			{
				url = "mngwserc/co/cob/COBPwdChng";
			}
			else
			{
				if(emfMap.containsKey("reset")){
					url = "mngwserc/co/cob/COBPwdConfirm";
					modelMap.addAttribute("id",emfMap.getString("id"));
				}else{
					url = "redirect:/mngwsercgateway/getLogin.do";
				}
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

		return url;
   	}

   	/**
   	 * 비밀번호를 변경한다.
   	 *
   	 * @param emfMap
	 * @return String View URL
   	 * @throws Exception
   	 */
   	@RequestMapping(value="/mngwsercgateway/setPwdChng.ajax")
   	public String updatePwd(@ApiData EmfMap emfMap, ModelMap modelMap) throws Exception
   	{
   		try
   		{
   			modelMap.addAttribute("rtnData", cOBLgnService.updatePwd(emfMap));
   			
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
   	 * 비밀번호를 변경한다. 2 _ reset 에서 넘어간 경우
   	 *
   	 * @param emfMap
	 * @return String View URL
   	 * @throws Exception
   	 */
   	@RequestMapping(value="/mngwsercgateway/resetPwd.ajax")
   	public String resetPwd(@ApiData EmfMap emfMap, ModelMap modelMap) throws Exception
   	{
   		try
   		{
   			modelMap.addAttribute("rtnData",cOBLgnService.resetPwd(emfMap));
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
	 * 관리자 내정보변경 페이지
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
	@RequestMapping(value="/mngwserc/**/profile.do")
	public String getPrsnDataChngPage(@ApiData EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			EmfMap lgnMap = EgovUserDetailsHelper.getAuthenticatedUser();

			emfMap.put("detailsKey", lgnMap.getString("admSeq"));

			modelMap.addAttribute("rtnData", cOCAdmService.selectAdmDtl(emfMap));
		}
		catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw he;
		}

		return "mngwserc/co/coc/COCPrsnDataChng";
	}

	/**
	 * 관리자 내정보변경을 한다.
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
	@RequestMapping(value="/mngwserc/**/profile.ajax", method=RequestMethod.POST)
	public String updatePrsnData(@ApiData EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			emfMap.put("isAdmMng", "N");

			modelMap.addAttribute("actCnt", cOCAdmService.updatePrsnData(emfMap));
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
	 * 선택한 나라에 대한 관리자 선택
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
	@RequestMapping(value="/mngwserc/**/get-admin-list.ajax", method=RequestMethod.POST)
	public String getAdminList(@ApiData EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("rtnData", cOCAdmService.getAdmAllList(emfMap));
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
	 * 선택한 나라에 대한 관리자 선택
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
	@RequestMapping(value="/auth/{pddg}/set-admin-login.ajax", method=RequestMethod.GET)
	public String setAdmin(@ApiData EmfMap emfMap, ModelMap modelMap, HttpServletRequest request, @PathVariable String pddg) throws Exception
	{
		EmfMap rtnMap = null;
		String url = "error/blank.error";
		try
		{
			// 보안 처리(로그인 세션 변경)
    		if (request.getSession(false) != null)
    		{
    			request.getSession().invalidate();
    		}
    		emfMap.put("isPwd", "1");
    		String hcsHoIp = EgovNetworkState.getMyIPaddress(request);
    		System.out.println("hcsHoIp : " + hcsHoIp);
    		EmfMap authPddgMap = cOBLgnService.getAuthPddg(emfMap);
    		if(authPddgMap.getString("pddg").equals(pddg))
    		{
    			emfMap.put("authPddg", authPddgMap.getString("pddg"));
    			rtnMap = cOBLgnService.actionLogin(emfMap, request);
    			modelMap.addAttribute("rtnData", rtnMap);
    		}
    		else
    		{
    			emfMap.put("rtnCode", "fail.lgn.use");
    			modelMap.addAttribute("rtnData", emfMap);
    			//throw new EmfException("NOT PDDG");
    		}
    		
    		//본사&권역 간 pddg 값이 상이하거나 해당 계정에 대한 정보가 없을 경우.
    		if(!(rtnMap != null && !"".equals(rtnMap.getString("rtnUrl"))))
    		{
				modelMap.addAttribute("msg", "You Have no access to the menu.");
				modelMap.addAttribute("url", "javascript:window.close();");
				
				return url;
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

		return "redirect:" + rtnMap.getString("rtnUrl");
	}
	
	
	/**
	 * 비밀번호 초기화 페이지
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
    @RequestMapping(value="/mngwsercgateway/getResetPwdPage.do")
	public String getResetPwdPage(@ApiData EmfMap emfMap, ModelMap modelMap, HttpServletRequest request) throws Exception
    {
    	try
		{
    		// 보안 처리(로그인 세션 변경)
 			if (request.getSession(false) != null)
 			{
 				request.getSession().invalidate();
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

    	return "mngwserc/co/cob/COBResetPwd";
	}
    
    /**
	 * 비밀번호 초기화 이메일 발송
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
	@RequestMapping(value="/mngwsercgateway/sendMailResetPwd.ajax", method=RequestMethod.POST)
	public String sendMailResetPwd(@ApiData EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		EmfMap rtnMap = null;
		String url = "/mngwsercgateway/getLogin.do";
		try
		{
			modelMap.addAttribute("rtnData", cOBLgnService.sendMailResetPwd(emfMap));
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
	 * DB에 저장된 세션 날리기
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
	@RequestMapping(value="/mngwsercgateway/delSession.ajax", method=RequestMethod.POST)
	public String delSession(@ApiData EmfMap emfMap, ModelMap modelMap, HttpServletRequest request) throws Exception
	{
		try
		{
			System.out.println("call delSession");
			// 보안 처리(로그인 세션 변경)
 			if (request.getSession(false) != null)
 			{
 				EmfMap tmpMap = (EmfMap) request.getSession().getAttribute("admLgnMap");
 				
 				if(tmpMap != null){
 					tmpMap.put("admId", tmpMap.getString("id"));
 					cOBLgnService.actionLogout(tmpMap, request);
 				}
 				
 				request.getSession().invalidate();
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
	
	
}