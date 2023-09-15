package com.easymedia.api.controller;

import com.easymedia.api.annotation.ApiData;
import com.easymedia.dto.EmfMap;
import com.easymedia.service.EgovCmmUseService;
import com.easymedia.service.HSAFeedbackService;
import com.easymedia.utility.EgovStringUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Feedback", description = "Feedback")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hs/hsa")
public class HSAFeedbackController {

    private EgovCmmUseService cmmUseService;

    private HSAFeedbackService hSAFeedbackService;

    /**
     * 딜러 List Page
     *
     * @param emfMap 데이터
     * @return String View URL
     */
    @RequestMapping(value="/dlr-list.do")
    public EmfMap selectDlrListPage(EmfMap emfMap , HttpSession session , HttpServletResponse response) throws Exception
    {
        try
        {
            EmfMap dlrSrchInfo = (EmfMap) session.getAttribute("dlrSrchInfo");

            if (dlrSrchInfo != null)
            {
                session.removeAttribute("dlrSrchInfo");
            }

            EmfMap cstmrSrchInfo = (EmfMap) session.getAttribute("cstmrSrchInfo");

            if (cstmrSrchInfo != null)
            {
                session.removeAttribute("cstmrSrchInfo");
            }

            if (Integer.parseInt(emfMap.getString("admAuthCd")) > 40)
            {
                List<String> dlrCdList = (List<String>) emfMap.get("admDlrCdList");

                if (dlrCdList.size() == 1)
                {
                    response.sendRedirect("mngwserc/hs/hsa/cstmr-list.do");
                }
            }


            // 공통코드 배열 셋팅
            ArrayList<String> codeList = new ArrayList<String>();

            // 담당업무
            codeList.add("ASGN_TASK_CD");

            // Records per page
            codeList.add("LIST_CNT");

            // 정의된 코드id값들의 상세 코드 맵 반환
            emfMap.put("rtnCode", cmmUseService.getCmmCodeBindAll(codeList));
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
        }

        return emfMap;
    }

    /**
     * 딜러 List Ajax
     *
     * @param emfMap 데이터
     * @return String View URL
     */
    @RequestMapping(value="/dlr-list.ajax")
    public EmfMap selectDlrList(EmfMap emfMap,  HttpServletRequest request) throws Exception {
        EmfMap emfMap1 = null;
        try {
            emfMap.put("strPam", request.getAttribute("strPam"));

            request.getSession().setAttribute("dlrSrchInfo", emfMap);
            emfMap1 = hSAFeedbackService.HSAFeedbackList(emfMap);
        } catch (Exception he) {
            if (log.isDebugEnabled()) {
                log.debug(he.getMessage());
            }
        }

        return emfMap1;
    }

    /**
     * 딜러 List excel Ajax
     *
     * @param emfMap 데이터
     * @return String View URL
     */
    @RequestMapping(value="/excelDown.do")
    public EmfMap selectDlrExcel(EmfMap emfMap,HttpServletRequest request) throws Exception {
        EmfMap dlrSttsCnt = null;
        try {
            emfMap.put("strPam", request.getAttribute("strPam"));
            request.getSession().setAttribute("dlrSrchInfo", emfMap);
            emfMap.put("excelYn", "Y");
            dlrSttsCnt = hSAFeedbackService.getDlrSttsCnt(emfMap);
        } catch (Exception he) {
            if (log.isDebugEnabled()) {
                log.debug(he.getMessage());
            }
        }

        return dlrSttsCnt;
    }

    /**
     * 딜러 Status Count Ajax
     *
     * @param emfMap 데이터
     * @return String View URL
     */
    @RequestMapping(value="/dlr-stts.ajax")
    public EmfMap getDlrSttsCnt(EmfMap emfMap) throws Exception {
        EmfMap dlrSttsCnt = null;
        try {
            dlrSttsCnt = hSAFeedbackService.getDlrSttsCnt(emfMap);
        } catch (Exception he) {
            if (log.isDebugEnabled()) {
                log.debug(he.getMessage());
            }
        }

        return dlrSttsCnt;
    }

    /**
     * 고객 List Page
     *
     * @param emfMap 데이터
     * @return String View URL
     */
    @RequestMapping(value="/cstmr-list.do")
    public EmfMap selectCstmrListPage(EmfMap emfMap, HttpServletRequest request , HttpServletResponse response) throws Exception
    {

        try
        {
            HttpSession session = request.getSession();

            EmfMap dlrSrchInfo = (EmfMap) session.getAttribute("dlrSrchInfo");

            if (dlrSrchInfo != null)
            {
                String dlrCnctKey = emfMap.getString("dlrCnctKey");

                if (!"".equals(dlrCnctKey))
                {
                    String [] tempArr = EgovStringUtil.split(dlrCnctKey, "_");

                    dlrSrchInfo.put("natnCd", tempArr[0]);
                    dlrSrchInfo.put("dlrspCd", tempArr[1]);
                    dlrSrchInfo.put("dlrCd", tempArr[2]);
                }

                session.setAttribute("dlrSrchInfo", dlrSrchInfo);
            }
            else
            {
                int admAuthCd = Integer.parseInt(emfMap.getString("admAuthCd"));

                List<String> dlrCdList = (List<String>) emfMap.get("admDlrCdList");

                if (admAuthCd > 40 && admAuthCd < 50 && dlrCdList.size() == 1)
                {
                    emfMap.put("dlrCd", dlrCdList.get(0));

                    session.setAttribute("dlrSrchInfo", emfMap);
                }
                else
                {
                    response.sendRedirect("mngwserc/hs/hsa/dlr-list.do");
                }
            }

            // 공통코드 배열 셋팅
            ArrayList<String> codeList = new ArrayList<String>();

            // 담당업무
            codeList.add("ASGN_TASK_CD");

            // Records per page
            codeList.add("LIST_CNT");
            emfMap.put("rtnCode", cmmUseService.getCmmCodeBindAll(codeList));
            // 정의된 코드id값들의 상세 코드 맵 반환
        }
        catch (Exception he)
        {
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }

        }

        return emfMap;
    }

    /**
     * 고객 List Ajax
     *
     * @param emfMap 데이터
     * @return String View URL
     */
    @RequestMapping(value="/cstmr-list")
    public EmfMap selectCstmrList(@ApiData EmfMap emfMap, HttpSession session) throws Exception {
        EmfMap emfMap1 = null;
        try {
            session.setAttribute("cstmrSrchInfo", emfMap);

            EmfMap dlrSrchInfo = (EmfMap) session.getAttribute("dlrSrchInfo");

            if (dlrSrchInfo != null) {
                emfMap.put("dlrSrchInfo", dlrSrchInfo);
            }
            emfMap1 = hSAFeedbackService.HSAFeedbackList(emfMap);
        } catch (Exception he) {
            if (log.isDebugEnabled()) {
                log.debug(he.getMessage());
            }
        }

        return emfMap1;
    }



    /**
     * 고객 Status Count Ajax
     *
     * @param emfMap 데이터
     * @return String View URL
     */
    @RequestMapping(value = "/cstmr-stts.ajax")
    public EmfMap getCstmrSttsCnt(EmfMap emfMap, HttpSession session) throws Exception {
        EmfMap cstmrSttsCnt=null;
        try {
            EmfMap dlrSrchInfo = (EmfMap) session.getAttribute("dlrSrchInfo");

            if (dlrSrchInfo != null) {
                emfMap.put("dlrSrchInfo", dlrSrchInfo);
            }
            cstmrSttsCnt = hSAFeedbackService.getCstmrSttsCnt(emfMap);
        } catch (Exception he) {
            if (log.isDebugEnabled()) {
                log.debug(he.getMessage());
            }
        }

        return cstmrSttsCnt;
    }

    /**
     * 고객 Details
     *
     * @param emfMap 데이터
     * @return String View URL
     */
    @RequestMapping(value="/cstmr-write.do")
    public EmfMap selectCstmrDtl(EmfMap emfMap, HttpSession session) throws Exception {
        EmfMap emfMap1 = null;
        try {
            EmfMap dlrSrchInfo = (EmfMap) session.getAttribute("dlrSrchInfo");

            if (dlrSrchInfo != null) {
                String cstmrCnctKey = emfMap.getString("cstmrCnctKey");

                if ("".equals(cstmrCnctKey)) {
                    emfMap.put("cstmrCnctKey", dlrSrchInfo.getString("cstmrCnctKey"));
                } else {
                    dlrSrchInfo.put("cstmrCnctKey", cstmrCnctKey);
                }

                emfMap.put("dlrSrchInfo", dlrSrchInfo);
                session.setAttribute("dlrSrchInfo", dlrSrchInfo);
            }
            emfMap1 = hSAFeedbackService.selectCstmrDtl(emfMap);
        } catch (Exception he) {
            if (log.isDebugEnabled()) {
                log.debug(he.getMessage());
            }
        }

        return emfMap1;
    }

    /**
     * 엑셀다운로드
     *
     * @param emfMap 검색할 데이터
     * @return String View URL
     */
    @RequestMapping(value = "/cstmrExcelDown.do")
    public EmfMap excelCstmrList(EmfMap emfMap, HttpSession session) throws Exception {
        EmfMap emfMap1 = null;
        try {
            EmfMap dlrSrchInfo = (EmfMap) session.getAttribute("dlrSrchInfo");

            if (dlrSrchInfo != null) {
                emfMap.put("dlrSrchInfo", dlrSrchInfo);
            }

            emfMap.put("excelYn", "Y");
            emfMap.put("asgnTaskCd", emfMap.getString("asgnTaskCd"));
            emfMap1 = hSAFeedbackService.HSAFeedbackList(emfMap);
        } catch (Exception he) {
            if (log.isDebugEnabled()) {
                log.debug(he.getMessage());
            }

        }

        return emfMap1;
    }

    @RequestMapping(value="/allDlrExcelDown.do")
    public EmfMap excelAllDlrList(EmfMap emfMap, HttpSession session) throws Exception {
        EmfMap emfMap1 = null;
        try {
            EmfMap dlrSrchInfo = (EmfMap) session.getAttribute("dlrSrchInfo");

            if (dlrSrchInfo != null) {
                emfMap.put("dlrSrchInfo", dlrSrchInfo);
            }

            emfMap.put("excelYn", "Y");
            emfMap.put("asgnTaskCd", emfMap.getString("asgnTaskCd"));
            emfMap1 = hSAFeedbackService.HSAFeedbackList(emfMap);
        } catch (Exception he) {
            if (log.isDebugEnabled()) {
                log.debug(he.getMessage());
            }
        }

        return emfMap1;
    }
}
