package com.easymedia.service.impl;

import com.easymedia.dto.EmfMap;
import com.easymedia.error.hotel.utility.sim.SeedCipher;
import com.easymedia.service.COBLgnService;
import com.easymedia.service.COFSysLogService;
import com.easymedia.service.MailService;
import com.easymedia.service.dao.COBLgnDAO;
import com.easymedia.utility.EgovDateUtil;
import com.easymedia.utility.EgovNetworkState;
import com.easymedia.utility.EgovStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 로그인/로그아웃 ServiceImpl
 * </pre>
 *
 * @ClassName		: COBLgnServiceImpl.java
 * @Description		: 로그인/로그아웃 ServiceImpl
 * @author 허진영
 * @since 2019.01.15
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		 since			 author				   description
 *   ============    ==============    =============================
 *    2019.01.15	 	 허진영					최초 생성
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class COBLgnServiceImpl implements COBLgnService {

    private final COBLgnDAO cOBLgnDAO;

   	private final COFSysLogService cOFSysLogService;
    
    /** 메일 보내는 사람 주소 **/
    @Value("${globals.http-admin-url}")
	private String httpAdminUrl;

	/** 메일 보내는 사람 주소 **/
	@Value("${globals.http-url}")
	private String httpUrl;

	/** SITENAME **/
	@Value("${globals.site-name}")
	private String globalsSiteName;

    /** 메일 서비스 **/
    private final MailService mailService;

	/**
	 * 일반 로그인을 처리한다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public EmfMap actionLogin(EmfMap emfMap, HttpServletRequest request) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();
		rtnMap.put("rtnCode", "success.lgn");
    	// 단방향 암호화 처리
    	if("1".equals(emfMap.getString("isPwd")))
    	{
    		emfMap.put("password", emfMap.getString("password"));
    	}
    	else
    	{
    		emfMap.put("password", SeedCipher.oneencrypt(emfMap.getString("password")));
    	}

    	// 관리자 계정 조회
    	EmfMap info = cOBLgnDAO.actionLogin(emfMap);

    	if (info != null)
    	{
    		//권한여부에 따라서 승인이 필요하지 않다.
    		String authCd = info.getString("authCd");

    		// 차단여부 확인
    		if (!"Y".equals(info.getString("useYn")))
    		{
    			rtnMap.put("rtnCode", "fail.lgn.use");
    		}
    		// 승인여부 확인
    		else if (!"Y".equals(info.getString("appvYn")) && (!"0".equals(authCd) && !"10".equals(authCd) && !"20".equals(authCd)))
    		{
    			rtnMap.put("rtnCode", "fail.lgn.appv");
    			rtnMap.put("rtnUrl", "/mngwsercgateway/getPwdChng.do");
    		}
    		else
    		{
    			// 비밀번호변경 필요여부
    			boolean pwdChngNeeds = false;
    			
    			//본사대쉬보드 -> 권역레벨로 접근 시, 해당 계정에 대한 확인 불필요(2019-08-08)
        		if (!"1".equals(emfMap.getString("isPwd")))
        		{
        			// 첫 로그인 확인
            		if ("".equals(info.getString("lastLgnDtm")))
                	{
            			pwdChngNeeds = true;
                	}
            		// 임시 비밀번호 로그인 확인
                	else if ("Y".equals(info.getString("tmpPwdYn")))
                	{
                		pwdChngNeeds = true;
                		rtnMap.put("rtnCode", "fail.lgn.pwd.temp");
                	}
                	else
                	{
                		// 비밀번호변경 3개월 주기 확인
                    	String lastPwdModDtm = info.getString("lastPwdModDtm");
                		if (!"".equals(lastPwdModDtm))
                		{
                			lastPwdModDtm = EgovDateUtil.convertDate(lastPwdModDtm, "yyyy-MM-dd HH:mm:ss", "yyyyMMdd", "");
                		}
                		else
                		{
                			lastPwdModDtm = EgovDateUtil.convertDate(info.getString("regDtm"), "yyyy-MM-dd HH:mm:ss", "yyyyMMdd", "");
                		}

                		if (!"Y".equals(info.getString("longTermYn")) &&
                				EgovDateUtil.getDaysDiff(lastPwdModDtm, EgovDateUtil.getToday()) > 90)
                		{
                			pwdChngNeeds = true;
                			rtnMap.put("rtnCode", "fail.lgn.pwd.chng");
                		}
                	}
        		}

    			if (pwdChngNeeds)
    			{
    				if(!"Y".equals(info.getString("longTermYn"))){
	    				// 임시 로그인 세션을 생성한다.
	    	    		RequestContextHolder.getRequestAttributes().setAttribute("tmpLgnMap", emfMap, RequestAttributes.SCOPE_SESSION);
	   					rtnMap.put("rtnUrl", "/mngwsercgateway/getPwdChng.do");
    				}
   				}
   				else
   				{
   					// HGCX 접근 가능 여부 확인
   		    		if("N".equals(info.getString("acssAuthHgcx")))
   		    		{
   		    			rtnMap.put("rtnCode", "fail.lgn.acssAuth");
   		    		}
   		    		else{
   		    		// 최초 Redirect URL 설정
   	           			List<EmfMap> menuList = getMenuList(info);
   	           			if (menuList.size() == 0)
   	           			{
   	           				rtnMap.put("rtnCode", "fail.lgn.menu");
   	           			}
   	           			else
   	           			{
   	           				String pageAdminLink = "";
   	           				List<EmfMap> dlrCdList = null;
   	           				List<String> dlrCdStringList = new ArrayList<String>();

   	           				int intAuthCd = Integer.parseInt(authCd);

   	           				if (intAuthCd > 40 && intAuthCd < 50)
   	   		    			{
   	   		    				if (intAuthCd == 41)
   	   		    				{
   	   		    					//딜러 그룹 사장
   	   		    					dlrCdList = cOBLgnDAO.getAdminDealerGroupList(info);
   	   		    				}
   	   		    				else if (intAuthCd == 42)
   	   		    				{
   	   		    					//딜러 사장
   	   		    					dlrCdList = cOBLgnDAO.getAdminDealerAppvRelList(info);
   	   		    				}
   	   		    				else
   	   		    				{
   	   		    					//딜러 사장
   	   		    					dlrCdList = cOBLgnDAO.getAdminDealerAppvRelList(info);
   	   		    				}
   	   		    				String asgnTaskCd = "";
   	   		    				EmfMap asgnTaskCdMap = new EmfMap();
   	   		    				if(dlrCdList != null && dlrCdList.size() > 0)
   	   		    				{
   	   		    					for(int q = 0 ; q < dlrCdList.size() ; q++)
   	   		    					{
   	   		    						asgnTaskCdMap.put(dlrCdList.get(q).getString("asgnTaskCd").toLowerCase(), "1");
   	   		    						dlrCdStringList.add(dlrCdList.get(q).getString("dlrCd"));
   	   		    					}
   	   		    				}
   	   		    				//관리하는 딜러사의 담당업무코드를 조회한다.
   	   		    				//관리자의 상세에 있는 담당업무코드는 엑셀 조회용.
   	   		    				if("1".equals(asgnTaskCdMap.getString("sales,service")))
   	   		    				{
   	   		    					asgnTaskCd = "Sales,Service";
   	   		    				}
   	   		    				if("1".equals(asgnTaskCdMap.getString("sales")))
   	   		    				{
   	   		    					asgnTaskCd = "Sales";
   	   		    				}
   	   		    				if("1".equals(asgnTaskCdMap.getString("service")))
   	   		    				{
   	   		    					if("".equals(asgnTaskCd))
   	   		    					{
   	   		    						//서비스만 있을경우
   	   		    						asgnTaskCd = "Service";
   	   		    					}
   	   		    					else
   	   		    					{
   	   		    						//Sales가 있을경우
   	   		    						asgnTaskCd = "Sales,Service";
   	   		    					}
   	   		    				}
   	   		    				info.put("asgnTaskCd", asgnTaskCd);
   	   		    				info.put("dlrCdList", dlrCdStringList);
   	   		    			}
   	   		    			else
   	   		    			{
   	   		    				info.put("dlrCdList", new ArrayList<String>());
   	   		    			}

   	           				if (intAuthCd <= 29)
   	           				{
   	           					pageAdminLink = "/mngwserc/co/coa/all/mainDashBoard.do";
   	           					rtnMap.put("rtnUrl", "/mngwserc/co/coa/all/mainDashBoard.do");
   	           					//2022-10-18 본사에서 타권역 이동 시 본사 호출인지 check
   	           					rtnMap.put("hcsHo", 10);
   	           				}
   	           				else if (intAuthCd <= 42)
   	           				{
   	           					pageAdminLink = "/mngwserc/co/coa/"+info.getString("dlspCd")+"/main.do";
   	           					rtnMap.put("rtnUrl", "/mngwserc/co/coa/"+info.getString("dlspCd")+"/main.do");
   	           				}
   	           				else if (intAuthCd <= 44)
   	           				{
   	           					pageAdminLink = "/mngwserc/co/coa/"+ dlrCdStringList.get(0)+"/main.do";
   	           					rtnMap.put("rtnUrl", "/mngwserc/co/coa/"+dlrCdStringList.get(0)+"/main.do");
   	           				}
   	           				else if (intAuthCd <= 50)
   	           				{
   	           					pageAdminLink = "/mngwserc/ms/msa/dlr-list.do";
   	           					rtnMap.put("rtnUrl", "/mngwserc/ms/msa/dlr-list.do");
   	           				}
   	           				else
   	           				{
   	   	    					for (int i = 0, size = menuList.size(); i < size; i++)
   	   	            			{
   	   	            				pageAdminLink = EgovStringUtil.getUrlFolder(menuList.get(i).getString("admLink"));
   	   	            				if (!"".equals(pageAdminLink))
   	   	            				{
   	   	            					rtnMap.put("rtnUrl", menuList.get(i).getString("admLink"));
   	   	            					break;
   	   	            				}
   	   	            			}
   	           				}
   	       					if ("".equals(pageAdminLink))
   	               			{
   	               				rtnMap.put("rtnCode", "fail.lgn.menu");
   	               			}
   	       					else
   	       					{
   	       						
   	       						// 로그인 IP를 삽입해준다.
   	       		    			info.put("loginIp", EgovNetworkState.getMyIPaddress(request));
   	       		    			emfMap.put("loginIp", info.getString("loginIp"));
   	       		    			
   	       						// 로그인 시간을 업데이트 해준다. + ip도 삽입 2020.05.20
   	       		    			cOBLgnDAO.setLgnLstDtm(emfMap);
   	        		    			
   	       		    			// 로그인 세션을 생성한다.
   	       		    			info.put("firstRtnUrl", rtnMap.getString("rtnUrl"));
   	       		    			info.put("nation", httpUrl);
   	       		    			//2022-10-18 본사에서 타권역 이동 시 본사 호출인지 check
   	       		    			info.put("hcsHo", rtnMap.getString("hcsHo"));
   	       		    			// 20200910 세션시간 연장
   	       		    			HttpSession session = request.getSession();
   	       		    			session.setMaxInactiveInterval(3600);
   	       		    			info.put("sessionId", session.getId());
   	       		    			RequestContextHolder.getRequestAttributes().setAttribute("admLgnMap", info, RequestAttributes.SCOPE_SESSION);

   	       		    			if("1".equals(emfMap.getString("isPwd")))
   	       		    	    	{
   	       		    				RequestContextHolder.getRequestAttributes().setAttribute("admLgnMapTrue", info, RequestAttributes.SCOPE_SESSION);
   	       		    	    	}
   	        		    			// 로그인 로그를 삽입해준다.
   	       		    			emfMap.put("srvcNm", "COBLgnController");
   	       		    			emfMap.put("fncNm", "actionLogin");
   	       		    			emfMap.put("trgtMenuNm", "로그인 페이지");
   	       		    			emfMap.put("prcsCd", "L");
   	       		    			emfMap.put("prcsTime", "100");
   	       		    			emfMap.put("reqnId", info.getString("id"));
   	       		    			emfMap.put("reqnIp", info.getString("loginIp"));

								rtnMap.put("info", info);
   	       		    			
   	       		    			try
   	       		    			{
   	       		    				cOFSysLogService.logInsertSysLog(emfMap);
   	       		    			}
   	       		    			catch (Exception e)
   	       		    			{
   	       		    				log.debug(e.getMessage());
   	       		    			}
   	       					}
   	           			}
   		    		}
   					
   	    		}
    		}
    	}
    	else
    	{
    		rtnMap.put("rtnCode", "fail.lgn.info");
    		
    		// 2020.05.22 틀린 횟수 업데이트
    		cOBLgnDAO.updateLgnFail(emfMap);
    	}

    	return rtnMap;
    }
    
    /* 로그인 실패 처리 */
    @Override
	public void actionLgnFail(EmfMap emfMap, HttpServletRequest request)throws Exception {
    	cOBLgnDAO.updateLgnFail(emfMap);
	}
    
    /* 로그인 실패 초기화 */
    @Override
	public void actionLgnFailReset(EmfMap emfMap, HttpServletRequest request)throws Exception {
    	cOBLgnDAO.actionLgnFailReset(emfMap);
	}
    
    
    /**
	 * 로그아웃을 처리한다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public void actionLogout(EmfMap emfMap, HttpServletRequest request) throws Exception
    {
    	
    	// 로그인 세션을 삭제한다.
    	RequestContextHolder.getRequestAttributes().setAttribute("admLgnMap", null, RequestAttributes.SCOPE_SESSION);

    	// 로그아웃 로그를 삽입해준다.
    	emfMap.put("srvcNm", "COBLgnController");
    	emfMap.put("fncNm", "actionLogout");
		emfMap.put("trgtMenuNm", "로그인 페이지");
		emfMap.put("prcsCd", "O");
		emfMap.put("prcsTime", "100");
		emfMap.put("reqnId", emfMap.getString("regId"));
		emfMap.put("reqnIp", EgovNetworkState.getMyIPaddress(request));

		try
		{
			cOFSysLogService.logInsertSysLog(emfMap);
			
			// DB에 저장된 세션 정보 초기화
	    	cOBLgnDAO.initSession(emfMap);
		}
		catch(Exception e)
		{
			log.debug(e.getMessage());
		}
    }

    /**
     * 비밀번호를 변경한다.
     *
	 * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap updatePwd(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();

    	// 임시 로그인 세션
    	EmfMap lgnMap = (EmfMap) RequestContextHolder.getRequestAttributes().getAttribute("tmpLgnMap", RequestAttributes.SCOPE_SESSION);

    	if (lgnMap != null)
    	{
    		// 비밀번호 확인
        	if (!lgnMap.getString("password").equals(SeedCipher.oneencrypt(emfMap.getString("password"))))
        	{
        		rtnMap.put("rtnCode", "fail.lgn.pwd.info");
        	}
        	// 신규 비밀번호 확인
        	else if ("".equals(emfMap.getString("newPassword")) || !emfMap.getString("newPassword").equals(emfMap.getString("newPasswordConfirm")))
        	{
        		rtnMap.put("rtnCode", "fail.lgn.pwd._new");
        	}
        	else
        	{
        		emfMap.put("id", lgnMap.getString("id"));
        		emfMap.put("pwd", SeedCipher.oneencrypt(emfMap.getString("newPassword")));

        		// 기존 비밀번호 사용여부 확인
        		if (emfMap.getString("password").equals(emfMap.getString("pwd")))
            	{
        			rtnMap.put("rtnCode", "fail.lgn.pwd.diff");
            	}
        		else
        		{
        			int actCnt = cOBLgnDAO.setPwdChng(emfMap);

        			if (actCnt > 0)
        			{
    	    			cOBLgnDAO.setLgnLstDtm(emfMap);
        			}
        		}
        	}
    	}

    	return rtnMap;
    }

  	/**
	 * 메뉴 리스트를 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public List<EmfMap> getMenuList(EmfMap emfMap) throws Exception
    {
    	return cOBLgnDAO.getMenuList(emfMap);
    }

    /**
	 * 상위 부모의 메뉴를 가져온다.
	 *
	 * @param pageNo
	 * @return
	 * @exception Exception
	 */
    public List<EmfMap> getParntMenuList(int pageNo) throws Exception
    {
    	return cOBLgnDAO.getParntMenuList(pageNo);
    }

    /**
	 * 외부에서 접근하는 관리자 확인
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public EmfMap getAuthPddg(EmfMap emfMap) throws Exception
    {
    	return cOBLgnDAO.getAuthPddg(emfMap);
    }

	@Override
	public EmfMap getLastLgnInfo(EmfMap emfMap, HttpServletRequest request ) throws Exception {
		if (emfMap.containsKey("password")) {
			emfMap.put("password", SeedCipher.oneencrypt(emfMap.getString("password")));
		}

		EmfMap infoMap = cOBLgnDAO.getLastLgnInfo(emfMap);

		if (infoMap != null) {
			infoMap.put("admId", emfMap.get("id"));
			//접속후 1시간 이내에
			if (!"Y".equals(infoMap.getString("isNoSession"))) {
				String curSession = RequestContextHolder.getRequestAttributes().getSessionId();
				String curIp = EgovNetworkState.getMyIPaddress(request);
				//접속한 세션과 DB세션이 다른 상태
				if (!curSession.equals(infoMap.getString("sessionId"))) {
					cOBLgnDAO.initSession(infoMap);
				}
			} else {
				cOBLgnDAO.initSession(infoMap);
			}
		}

		return infoMap;
	}

	/**
	 *  비밀번호 초기화 메일 발송
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	@Override
	public EmfMap sendMailResetPwd(EmfMap emfMap) throws Exception {
		EmfMap resultMap = new EmfMap();
		
		try{
			EmfMap mailMap = new EmfMap();
			
			List<EmfMap> toMailList = new ArrayList<EmfMap>();
			
			String emailFileNm = "reset_password.html";
			EmfMap tmpMap = new EmfMap();
			mailMap.put("title", "["+globalsSiteName+"] Password Reset");
			tmpMap.put("email", emfMap.getString("id"));
			tmpMap.put("etc1", httpAdminUrl);
			tmpMap.put("etc2", httpAdminUrl + "/mngwsercgateway/getPwdChng.do?reset=1&id="+emfMap.getString("id"));
			toMailList.add(tmpMap);
			
			mailMap.put("toMailList", toMailList);
			mailService.sendEventTempleteMail(mailMap, emailFileNm);
			resultMap.put("result",true);
		}catch(Exception e){
			resultMap.put("result",false);
		}
		
		return resultMap;
	}

	
	/**
	 *  비밀번호 초기화 화면에서 비밀번호 변경
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	@Override
	public int resetPwd(EmfMap emfMap) throws Exception {
		
		if(emfMap.containsKey("newPassword")){
			emfMap.put("newPassword", SeedCipher.oneencrypt(emfMap.getString("newPassword")));
		}
		
		return cOBLgnDAO.resetPwd(emfMap);
	}

	
	 
}