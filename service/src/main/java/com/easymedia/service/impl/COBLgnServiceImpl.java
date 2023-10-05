package com.easymedia.service.impl;

import com.easymedia.dto.EmfMap;
import com.easymedia.dto.login.LoginUser;
import com.easymedia.enums.menu.Site;
import com.easymedia.error.ErrorCode;
import com.easymedia.error.hotel.utility.sim.SeedCipher;
import com.easymedia.jwt.JwtTokenProvider;
import com.easymedia.property.JwtProperties;
import com.easymedia.service.COBLgnService;
import com.easymedia.service.COFSysLogService;
import com.easymedia.service.EgovCmmUseService;
import com.easymedia.service.MailService;
import com.easymedia.service.dao.COBLgnDAO;
import com.easymedia.utility.EgovDateUtil;
import com.easymedia.utility.EgovNetworkState;
import com.easymedia.utility.EgovStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

	/** 공통 서비스  **/
	private final EgovCmmUseService cmmUseService;
    
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

	/** 토큰 생성 속성 **/
	private final JwtProperties _jwtProperties;
	/** 토큰 제공 **/
	private final JwtTokenProvider _jwtTokenProvider;

	/**
	 * 일반 로그인을 처리한다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
    public EmfMap actionLogin(EmfMap emfMap, HttpServletRequest request, HttpServletResponse response) throws Exception
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
    			rtnMap.put("rtnUrl", "/getPwdChng");
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
						emfMap.put("admSeq", info.getString("admSeq"));
						setTempJwtToken(response, emfMap, Site.MNGWSERCTEMP);
	   					rtnMap.put("rtnUrl", "/getPwdChng");
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
   	           					pageAdminLink = "/mngwserc/main";
   	           					rtnMap.put("rtnUrl", pageAdminLink);
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

								rtnMap.put("code", cmmUseService.getCmmCodeBindAll(new ArrayList<String>()));

								//API TOKEN
								setJwtToken(response, info, Site.MNGWSERC);
   	       		    			RequestContextHolder.getRequestAttributes().setAttribute("admLgnMap", info, RequestAttributes.SCOPE_SESSION);

   	        		    		// 로그인 로그를 삽입해준다.
   	       		    			emfMap.put("srvcNm", "COBLgnController");
   	       		    			emfMap.put("fncNm", "actionLogin");
   	       		    			emfMap.put("trgtMenuNm", "로그인 페이지");
   	       		    			emfMap.put("prcsCd", "L");
   	       		    			emfMap.put("prcsTime", "100");
   	       		    			emfMap.put("reqnId", info.getString("id"));
   	       		    			emfMap.put("reqnIp", info.getString("loginIp"));
								rtnMap.put("info", info);

								//메뉴리스트
								rtnMap.put("menuList", menuList);
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

	/**
	 * 로그인 토큰 생성
	 */
	private void setJwtToken(HttpServletResponse response, EmfMap loginMap, Site site) throws Exception {
		String token = _jwtTokenProvider.createToken(loginMap);
		//_jwtTokenProvider.createCookie(response, _jwtProperties.getTokenPrefix() + token, site.name());
		response.addHeader(_jwtProperties.getTokenHeader() + "_" + site.name(), token);
	}

	/**
	 * 임시 로그인(비밀번호 변경) 토큰 생성
	 */
	private void setTempJwtToken(HttpServletResponse response, EmfMap loginMap, Site site) throws Exception {
		//쿠키 굽기
		LoginUser loginUser = LoginUser.builder()
				.admSeq(Integer.parseInt(loginMap.getString("admSeq")))
				.id(loginMap.getString("id"))
				.password(loginMap.getString("password"))
				.authorities(null)
				.build();
		String token = _jwtTokenProvider.createTempToken(loginUser);
		//_jwtTokenProvider.createCookie(response, _jwtProperties.getTokenPrefix() + token, site.name());
		response.addHeader(_jwtProperties.getTokenHeader() + "_" + site.name(), token);
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

		String token = emfMap.getString("token");
		if(StringUtil.isBlank(token)){
			throw new AccessDeniedException(ErrorCode.ACCESS_DENIED.getMessage());
		}
		Authentication authentication = _jwtTokenProvider.getTempAuthentication(token);
		if (authentication == null) {
			throw new AccessDeniedException(ErrorCode.ACCESS_DENIED.getMessage());
		}
		Object principalObj = authentication.getPrincipal();
		if (!(principalObj instanceof LoginUser)) {
			throw new AccessDeniedException(ErrorCode.ACCESS_DENIED.getMessage());
		}
		LoginUser loginUser = (LoginUser)principalObj;
    	if (loginUser != null)
    	{
    		// 비밀번호 확인
        	if (!loginUser.getPassword().equals(SeedCipher.oneencrypt(emfMap.getString("password"))))
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
        		emfMap.put("admSeq", loginUser.getAdmSeq());
        		emfMap.put("id", loginUser.getId());
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
		try
		{
			EmfMap mailMap = new EmfMap();
			String pddg = UUID.randomUUID().toString();
			emfMap.put("pddg", pddg);
			List<EmfMap> toMailList = new ArrayList<EmfMap>();
			String emailFileNm = "reset_password.html";
			EmfMap tmpMap = new EmfMap();
			mailMap.put("title", "["+globalsSiteName+"] Password Reset");
			tmpMap.put("email", emfMap.getString("id"));
			tmpMap.put("etc1", httpAdminUrl);
			tmpMap.put("etc2", httpAdminUrl + "/"+pddg+"/getPwdReset");
			toMailList.add(tmpMap);
			mailMap.put("toMailList", toMailList);

			//UUID 저장
			cOBLgnDAO.insertEmailReset(emfMap);

			mailService.sendEventTempleteMail(mailMap, emailFileNm);
			resultMap.put("result",true);
		}
		catch(Exception e)
		{
			e.printStackTrace();;
			resultMap.put("result",false);
		}
		return resultMap;
	}

	/**
	 *  비밀번호 초기화 UUID 조회
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public EmfMap getEmailResetUuid(EmfMap emfMap) throws Exception {
		return cOBLgnDAO.getEmailResetUuid(emfMap);
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
		//UUID 사용 처리
		int cnt = cOBLgnDAO.updateEmailResetUuid(emfMap);
		if(cnt > 0){
			cnt = cOBLgnDAO.resetPwd(emfMap);
		}
		return cnt;
	}

	
	 
}