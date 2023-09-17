package com.easymedia.api.controller;

import com.easymedia.api.annotation.ApiData;
import com.easymedia.dto.EmfMap;
import com.easymedia.service.COAMainService;
import com.easymedia.service.EgovCmmUseService;
import com.easymedia.utility.EgovDateUtil;
import com.easymedia.utility.EgovStringUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * Main Controller
 * </pre>
 *
 * @ClassName		: COAMainController.java
 * @Description		: Main Controller
 * @author 허진영
 * @since 2019.01.09
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.01.09		  허진영					     최초생성
 * </pre>
 */
@Tag(name = "로그인", description = "로그인")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mngwserc/co/coa")
public class COAMainController {

    private final EgovCmmUseService cmmUseService;

    private final COAMainService cOAMainService;

    // TODO 수정필요
    /* 구글 실시간 조회 */
    //@Resource(name="oRASnsGoogleService")
    //private ORASnsGoogleService oRASnsGoogleService;

    // TODO 수정필요
    /* 페이스북 실시간 조회 */
   // @Resource(name="oRASnsFacebookService")
    //private ORASnsFacebookService oRASnsFacebookService;

    /**
     * 검색하려는 URL의 권한 확인
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    private EmfMap isAuthPage(EmfMap emfMap, String searchCd) throws Exception
    {
        emfMap.put("searchCd", searchCd);
        int searchLen = searchCd.length();
        int authCd = Integer.parseInt( emfMap.getString("admAuthCd") );
        String admNatnCd = emfMap.getString("admNatnCd");
        String admDlspCd = emfMap.getString("admDlspCd");
        List<String> admDlrCdList = emfMap.getList("admDlrCdList");
        List<String> tmpAdmDlrCdList = new ArrayList<String>();

        if(authCd <= 29)
        {
            //지역본부 관리자 (국가 선택)
            if(searchLen <= 2)
            {
                emfMap.put("searchScope", "natn");
            }
            else if(searchLen <= 3)
            {
                emfMap.put("searchScope", "all");
            }
            else if(searchLen <= 5)
            {
                emfMap.put("searchScope", "dlrsp");
                emfMap.put("searchDlspCd", searchCd.subSequence(0, 5));
            }
            else if(searchLen <= 10)
            {
                emfMap.put("searchScope", "dlr");
                emfMap.put("searchDlspCd", searchCd.subSequence(0, 5));
                tmpAdmDlrCdList.add(searchCd);
                emfMap.put("searchDlrCdList", tmpAdmDlrCdList);
            }
        }
        else if(searchLen < 10 && authCd <= 42)
        {
            //5글자는 대리점
            int isAuthCnt = searchCd.indexOf(admDlspCd);
            if(isAuthCnt > -1)
            {
                emfMap.put("searchDlspCd", admDlspCd);
                if(authCd == 41 || authCd == 42)
                {
                    emfMap.put("searchDlrCdList", (List<String>)emfMap.get("admDlrCdList"));
                }
                emfMap.put("searchScope", "dlrsp");
            }
            else
            {
                emfMap.put("searchScope", "");
            }
        }
        else if(searchLen < 20 && authCd <= 49)
        {
            //10글자는 딜러
            int isAuthCnt = searchCd.indexOf(admDlspCd);
            boolean isAuth = false;
            if(isAuthCnt > -1)
            {
                //딜러 사장, 딜러 그룹	2020-03-25
                if(authCd == 41 || authCd == 42)
                {
                    emfMap.put("searchDlspCd", searchCd.subSequence(0, 5));
                    tmpAdmDlrCdList.add(searchCd);
                    emfMap.put("searchDlrCdList", tmpAdmDlrCdList);
                    emfMap.put("searchScope", "dlr");
                }
				/*if(authCd == 41 || authCd == 42)
				{
					//딜러 사장, 딜러 그룹 사장은 본인껀지 확인
					if(((List<String>)emfMap.get("admDlrCdList")).contains(searchCd))
					{
						emfMap.put("searchDlspCd", admDlspCd);
						emfMap.put("searchDlrCdList", (List<String>)emfMap.get("admDlrCdList"));
						emfMap.put("searchScope", "dlr");
					}
					else
					{
						emfMap.put("searchScope", "");
					}
				}*/
                else
                {
                    tmpAdmDlrCdList.add(searchCd);
                    emfMap.put("searchDlrCdList", tmpAdmDlrCdList);
                    emfMap.put("searchScope", "dlr");
                    emfMap.put("searchDlspCd", admDlspCd);
                }
            }
            else
            {
                emfMap.put("searchScope", "");
            }
        }
        else
        {
            emfMap.put("searchScope", "");
        }

        return emfMap;
    }

    private EmfMap isAuthPage(EmfMap emfMap, String searchCd, boolean isNatnConf) throws Exception
    {
        if (isNatnConf && !"all".equals(searchCd))
        {
            emfMap.put("natnConf", cOAMainService.getNatnConfig(searchCd));
        }

        return isAuthPage(emfMap, searchCd);
    }

    /**
     * Main dashBoard Page
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/dashBoard.do")
    public String setDashBoard(EmfMap emfMap, ModelMap modelMap) throws Exception
    {
        String url = "redirect:/mngwserc/co/coa/";

        try
        {
            //권하에 따라서 dash보드를 변경한다
            int authCd = Integer.parseInt(emfMap.getString("admAuthCd"));

            if (authCd <= 29)
            {
                //지역본부 관리자 (국가 선택)
                url = url + "all/mainDashBoard.do";
            }
            else if (authCd <= 42)
            {
                //대리점
                url = url + emfMap.getString("admDlspCd") + "/main.do";
            }
            else if (authCd <= 48)
            {
                //딜러
                url = url + ((List) emfMap.get("admDlrCdList")).get(0) + "/main.do";
            }
            else if (authCd <= 49)
            {
                //딜러
                url = url + "/mngwserc/ms/msa/dlr-list.do";
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
     * Main dashBoard Page
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/main.do")
    public String selectSampleDashBoardPage(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            //현재 시스템 시간으로 분기 계산
            emfMap = isAuthPage(emfMap, searchCd, true);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            String curtDt = EgovDateUtil.getCurrentDate("");

            List<EmfMap> curtQrtList = new ArrayList<EmfMap>();

            for (int q = 0; q < 4; q++)
            {
                String tmpDate = EgovDateUtil.addMonth(curtDt, -3 * q);
                EmfMap tmpMap = new EmfMap();

                tmpMap.put("dt", tmpDate);
                tmpMap.put("qtr", EgovStringUtil.quarterYear(tmpDate));

                curtQrtList.add(tmpMap);
            }

            emfMap.put("curtQrtList", curtQrtList);
            emfMap.put("curtDt", curtDt);

            //딜러 본인
            emfMap = cOAMainService.selectMainDataDtl(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);

        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoard";
    }

    /**
     * 딜러 레벨 - Google My Business 정보 bar 불러오기.
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/setGoogleInfo.ajax")
    public String setGoogleInfo(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }



            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));

            modelMap.addAttribute("rtnMap", emfMap);

            //딜러 접근 시, 구글/페이스북 실시간 리뷰 총 갯수, 평점 가져오기.
            if ("dlr".equals(emfMap.getString("searchScope")))
            {
                emfMap.put("detailsKey", emfMap.getString("searchCd"));
                // TODO 수정필요
                // modelMap.addAttribute("rtnGoogleData", oRASnsGoogleService.getGoogleMyBusinessInfo(emfMap));
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

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardGoogleInfo";
    }

    /**
     * 딜러 레벨 - Facebook review 정보 bar 불러오기.
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/setFacebookInfo.ajax")
    public String setOraInfo(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));

            modelMap.addAttribute("rtnMap", emfMap);

            //딜러 접근 시, 구글/페이스북 실시간 리뷰 총 갯수, 평점 가져오기.
            if ("dlr".equals(emfMap.getString("searchScope")))
            {
                emfMap.put("detailsKey", emfMap.getString("searchCd"));
                // TODO 수정필요
                //   modelMap.addAttribute("rtnFacebookData", oRASnsFacebookService.getFacebookInfo(emfMap));
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

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardFacebookInfo";
    }

    /**
     * HGSI Status
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-hgsi-status.ajax")
    public String getHgsiStatus(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("kpiCd", emfMap.getString("kpiCd"));

            cOAMainService.getHgsiStatus(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardHgsiStatus";
    }

    /**
     * HGSI 총점 순위 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-hgsi-rating.ajax")
    public String getHgsiRagingAjax(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));

            cOAMainService.getHgsiRankingList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            throw new Exception(he.getMessage());
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardHgsiRatingList";
    }

    /**
     * 국가 및 개인별 월별 HGSI 평균 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-hgsi-month-rating.ajax")
    public String getHgsiMonthList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("kpiCd", emfMap.getString("kpiCd"));

            cOAMainService.getHgsiMonthList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
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
     * NPS Status 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-nps-status.ajax")
    public String getNpsStatus(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("kpiCd", emfMap.getString("kpiCd"));

            modelMap.addAttribute("rtnMap", cOAMainService.getNpsStatus(emfMap));
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardKeyQuestionNpsResultList";
    }

    /**
     * NPS Ranking 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-nps-rating.ajax")
    public String getNpsRankingList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("kpiCd", emfMap.getString("kpiCd"));

            cOAMainService.getNpsRankingList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardNpsRatingList";
    }

    /**
     * NPS Trend 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-nps-month-rating.ajax")
    public String getNpsMonthList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt",  EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("kpiCd", emfMap.getString("kpiCd"));

            cOAMainService.getNpsMonthList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
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
     * Key Question Performance 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-key-question-performance-list.ajax")
    public String getKeyQuestionPerformanceList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("kpiCd", emfMap.getString("kpiCd"));

            cOAMainService.getKeyQuestionPerformanceList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
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
     * Key Question Performance 추가 질문 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-key-question-performance-add-list.ajax")
    public String getKeyQuestionPerformanceAddResultList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("kpiCd", emfMap.getString("kpiCd"));

            cOAMainService.getKeyQuestionPerformanceAddResultList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardKeyQuestionAddResultList";
    }

    /**
     * Key Question Performance Trend (Latest 3 Months) 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-key-latest-question-performance-list.ajax")
    public String getKeyLatestQuestionPerformanceList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

//			emfMap.put("strtDt", EgovDateUtil.convertDate(EgovDateUtil.addYear(EgovDateUtil.getCurrentDate(""), -1), "yyyyMMdd", "yyyy-MM-dd", ""));
//			emfMap.put("endDt", EgovDateUtil.convertDate(EgovDateUtil.getCurrentDate(""), "yyyyMMdd", "yyyy-MM-dd", ""));

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("kpiCd", emfMap.getString("kpiCd"));

            cOAMainService.getKeyLatestQuestionPerformanceList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
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
     * Survey Response Status 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-survey-response-status-list.ajax")
    public String getSurveyResponseStatusList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("kpiCd", emfMap.getString("kpiCd"));

            cOAMainService.getSurveyResponseStatusList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
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
     * HGSI Feedback 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-hgsi-feedback-list.ajax")
    public String getFeedbackList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("kpiCd", emfMap.getString("kpiCd"));

            cOAMainService.getFeedbackList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardHgsiFeedbackList";
    }

    /**
     * Hot Alert - Latest 3 Months 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-hgsi-alert-list.ajax")
    public String getHgsiAlertList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));

            cOAMainService.getHotAlertList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardHgsiAlertList";
    }

    /**
     * HGSI Action Planning - Latest 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-hgsi-plan-list.ajax")
    public String getHgsiActPlngList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            cOAMainService.getHgsiActPlngList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardHgsiPlanList";
    }

    /**
     * Retail Standard Check Action Planning - Latest 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-rsc-plan-list.ajax")
    public String getRscActPlngList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            cOAMainService.getRscActPlngList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardRscPlanList";
    }

    /**
     * Profitability Data Status - Latest 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-profit-list.ajax")
    public String getProfitabilityDataList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            EmfMap tmpMap = null;

            String curtDt = EgovDateUtil.getCurrentDate("");

            List<EmfMap> curtQrtList = new ArrayList<EmfMap>();

            int curtQtr = 0;

            for (int q = 0; q < 4; q++)
            {
                tmpMap 			= new EmfMap();
                String tmpDate 	= EgovDateUtil.addMonth(curtDt, -3 * q);
                curtQtr         = EgovStringUtil.quarterYear(tmpDate);

                if (curtQtr == 1)
                {
                    tmpMap.put("strtYm", tmpDate.substring(0, 4) + "01");
                    tmpMap.put("endYm", tmpDate.substring(0, 4) + "03");
                }
                else if (curtQtr == 2)
                {
                    tmpMap.put("strtYm", tmpDate.substring(0, 4) + "04");
                    tmpMap.put("endYm", tmpDate.substring(0, 4) + "06");
                }
                else if (curtQtr == 3)
                {
                    tmpMap.put("strtYm", tmpDate.substring(0, 4) + "07");
                    tmpMap.put("endYm", tmpDate.substring(0, 4) + "09");
                }
                else if (curtQtr == 4)
                {
                    tmpMap.put("strtYm", tmpDate.substring(0, 4) + "10");
                    tmpMap.put("endYm", tmpDate.substring(0, 4) + "12");
                }
                tmpMap.put("qtr", curtQtr);
                tmpMap.put("dt", tmpDate);
                curtQrtList.add(tmpMap);
            }

            emfMap.put("curtQrtList", curtQrtList);

            cOAMainService.getProfitabilityDataList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardProfitDataList";
    }

    /**
     * 대리점 구글 리뷰
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-google-rvw-list.ajax")
    public String getGoogleRvwList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            EmfMap tmpMap = null;

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));

            String curtDt = EgovDateUtil.getCurrentDate("");

            List<EmfMap> curtQrtList = new ArrayList<EmfMap>();

            int curtQtr = 0;

            for (int q = 0; q < 4; q++)
            {
                tmpMap 			= new EmfMap();
                String tmpDate 	= EgovDateUtil.addMonth(curtDt, -3 * q);
                curtQtr         = EgovStringUtil.quarterYear(tmpDate);

                if (curtQtr == 2)
                {
                    tmpMap.put("strtYm", tmpDate.substring(0, 4) + "04");
                    tmpMap.put("endYm", tmpDate.substring(0, 4) + "06");
                }
                else if (curtQtr == 3)
                {
                    tmpMap.put("strtYm", tmpDate.substring(0, 4) + "07");
                    tmpMap.put("endYm", tmpDate.substring(0, 4) + "09");
                }
                else if (curtQtr == 4)
                {
                    tmpMap.put("strtYm", tmpDate.substring(0, 4) + "10");
                    tmpMap.put("endYm", tmpDate.substring(0, 4) + "12");
                }
                else
                {
                    tmpMap.put("strtYm", tmpDate.substring(0, 4) + "01");
                    tmpMap.put("endYm", tmpDate.substring(0, 4) + "03");
                }

                tmpMap.put("dt", tmpDate);
                tmpMap.put("qtr", curtQtr);

                curtQrtList.add(tmpMap);
            }

            emfMap.put("curtQrtList", curtQrtList);

            //딜러 접근 시, 구글 실시간 리뷰 가져오기.
            if ("dlr".equals(emfMap.getString("searchScope")))
            {
                // TODO 수정필요
                //   modelMap.addAttribute("rtnData", oRASnsGoogleService.getGoogleMyBusinessReviewsList(emfMap));

                // 딜러 유저 보다 권한이 높을 경우만 댓글 입력 권한 부여
                if (Integer.parseInt(emfMap.getString("admAuthCd")) < 44)
                {
                    modelMap.addAttribute("gmbReplyAuth", "isOk");
                }
            }
            else
            {
                cOAMainService.getGoogleRvwList(emfMap);
            }

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardGoogleRvwList";
    }

    /**
     * 대리점 페이스북 리뷰
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-facebook-rvw-list.ajax")
    public String getFacebookRvwList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            EmfMap tmpMap = null;

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));

            String curtDt = EgovDateUtil.getCurrentDate("");

            List<EmfMap> curtQrtList = new ArrayList<EmfMap>();

            int curtQtr	= 0;

            for (int q = 0; q < 4; q++)
            {
                tmpMap 			= new EmfMap();
                String tmpDate 	= EgovDateUtil.addMonth(curtDt, -3 * q);
                curtQtr         = EgovStringUtil.quarterYear(tmpDate);

                if (curtQtr == 2)
                {
                    tmpMap.put("strtYm", tmpDate.substring(0, 4) + "04");
                    tmpMap.put("endYm", tmpDate.substring(0, 4) + "06");
                }
                else if (curtQtr == 3)
                {
                    tmpMap.put("strtYm", tmpDate.substring(0, 4) + "07");
                    tmpMap.put("endYm", tmpDate.substring(0, 4) + "09");
                }
                else if (curtQtr == 4)
                {
                    tmpMap.put("strtYm", tmpDate.substring(0, 4) + "10");
                    tmpMap.put("endYm", tmpDate.substring(0, 4) + "12");
                }
                else
                {
                    tmpMap.put("strtYm", tmpDate.substring(0, 4) + "01");
                    tmpMap.put("endYm", tmpDate.substring(0, 4) + "03");
                }

                tmpMap.put("dt", tmpDate);
                tmpMap.put("qtr", curtQtr);

                curtQrtList.add(tmpMap);
            }

            emfMap.put("curtQrtList", curtQrtList);

            //딜러 접근 시, 페이스북 실시간 리뷰 가져오기.
            if ("dlr".equals(emfMap.getString("searchScope")))
            {
                // TODO 수정필요
                // modelMap.addAttribute("rtnData", oRASnsFacebookService.getFacebookReviewPage(emfMap));
            }
            else
            {
                cOAMainService.getFacebookRvwList(emfMap);
            }

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardFacebookRvwList";
    }

    /**
     * Tracker 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/get-traceker-list.ajax")
    public String getTrackerList(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));

            cOAMainService.getTrackerList(emfMap);

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoardDealerTrackerList";
    }

    /**
     * Main dashBoard Page
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/mainDashBoard.do")
    public String mainDashBoard(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        try
        {
            emfMap = isAuthPage(emfMap, searchCd, true);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            String curtDt = EgovDateUtil.getCurrentDate("");

            List<EmfMap> curtQrtList = new ArrayList<EmfMap>();

            for (int q = 0; q < 4; q++)
            {
                String tmpDate = EgovDateUtil.addMonth(curtDt, -3 * q);
                EmfMap tmpMap = new EmfMap();

                tmpMap.put("dt", tmpDate);
                tmpMap.put("qtr", EgovStringUtil.quarterYear(tmpDate));

                curtQrtList.add(tmpMap);
            }

            emfMap.put("curtQrtList", curtQrtList);
            emfMap.put("curtDt", curtDt);

            modelMap.addAttribute("rtnMap", emfMap);
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/COAMainDashBoard";
    }

    /**
     * 딜러명 or 딜러코드(현대딜러코드)로 검색하기 위한 모든 딜러List
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/getSearchDlrList")
    public List<EmfMap> getSearchDlrList(@ApiData EmfMap emfMap) throws Exception
    {
        try
        {
//            modelMap.addAttribute("paramMap", emfMap);
//            EmfMap rtnMap = new EmfMap();
            return cOAMainService.getSearchDlrList(emfMap);

        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }
    }


    /**
     * 메뉴별 엑셀 다운로드
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/{searchCd}/excelDown.do")
    public String excelDown(EmfMap emfMap, ModelMap modelMap, @PathVariable String searchCd) throws Exception
    {
        String url = "";
        try
        {
            emfMap = isAuthPage(emfMap, searchCd);

            if ("".equals(emfMap.getString("searchScope")))
            {
                throw new Exception("NOT PREMIT");
            }

            emfMap.put("strtDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("strtDt"),true), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("endDt", EgovDateUtil.convertDate(cOAMainService.convertDate(emfMap.getString("endDt"),false), "MM/dd/yyyy", "yyyy-MM-dd", ""));
            emfMap.put("kpiCd", emfMap.getString("kpiCd"));
            emfMap.put("excel", "Y");

            switch(emfMap.getString("excelCd")){
                case "1":		// hgsi
                    break;
                case "2":		// survey response
                    url ="COAMainDashBoardSurveyExcel.excel";
                    modelMap.addAttribute("rtnData", cOAMainService.selectFrptSurveyExcel(emfMap));
                    break;
                case "3":		// hgsi analytics
                    cOAMainService.getFeedbackList(emfMap);
                    modelMap.addAttribute("rtnMap", emfMap);
                    url = "COAMainDashBoardHgsiFeedbackExcel.excel";
                    break;
                case "4":		// retail Standard Check(RSC)
                    cOAMainService.getFrptRscListExcel(emfMap);
                    modelMap.addAttribute("rtnMap", emfMap);
                    url = "COAMainDashBoardRscPlanExcel.excel";
                    break;
                case "5":		// Dealer Performance Data Status(KPI)
                    cOAMainService.selectFrptKpiExcel(emfMap);
                    //공통코드 배열 셋팅
				/*ArrayList<String> cdDtlList = new ArrayList<String>();

				if("sales".equals(emfMap.getString("asgnTaskCd")))
				{
					cdDtlList.add("KPI_SALES_TYPE_CD");
				}
				else
				{
					cdDtlList.add("KPI_SERVICE_TYPE_CD");
				}

				modelMap.put("cdDtlList", cmmUseService.getCmmCodeBindAll(cdDtlList));*/
                    modelMap.addAttribute("rtnMap", emfMap);
                    url = "COAMainDashBoardProfitDataExcel.excel";
                    break;
                case "6":		// survey response
                    url ="COAMainDashBoardSurveyMonthlyExcel.excel";
                    modelMap.addAttribute("rtnData", cOAMainService.selectSurveyMonthlyExcel(emfMap));
                    break;
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

        return "mngwserc/co/coa/" + emfMap.getString("searchScope") + "/" + url;
    }
}