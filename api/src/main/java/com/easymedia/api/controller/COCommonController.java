package com.easymedia.api.controller;

import com.easymedia.api.annotation.ApiData;
import com.easymedia.dto.EmfMap;
import com.easymedia.dto.login.LoginUser;
import com.easymedia.error.ErrorResponse;
import com.easymedia.service.EgovCmmUseService;
import com.easymedia.utility.EgovFileMngUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 공통 관리를 위한 Controller
 * </pre>
 *
 * @ClassName		: COCommonController.java
 * @Description		: 공통 관리를 위한 Controller
 * @author 박주석
 * @since 2019.01.18
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		 since		  author	            description
 *    ==========    ==========    ==============================
 *    2019.01.18	  박주석	             최초 생성
 * </pre>
 */
@Tag(name = "공통", description = "공통")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class COCommonController {

    /** 공통 서비스  **/
    private final EgovCmmUseService cmmUseService;

    private final EgovFileMngUtil fileMngUtil;


    /**
     * 모든 코드 조회
     *
     * @return String View URL
     * @throws Exception
     */
    @Operation(summary = "모든 코드 조회", description = "")
    @RequestMapping(value="/co/code/list")
    public EmfMap getCodeList() throws Exception
    {
        EmfMap rntCode = null;
        try
        {
            ArrayList<String> codeList = new ArrayList<String>();
            rntCode = cmmUseService.getCmmCodeBindAll(codeList);
        }
        catch (Exception he){
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }
        return rntCode;
    }


    /**
     * 국가 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @Operation(summary = "국가 리스트 조회", description = "")
    @RequestMapping(value="/co/country/list")
    public List<EmfMap> getCountryList(@ApiData EmfMap emfMap) throws Exception
    {
        try {
            //2023-04-14 아태 권역에서만 노출
            // emfMap.put("curtRegion", region);
            return cmmUseService.getNationList(emfMap);
        }
        catch (Exception he){
            if (log.isDebugEnabled())
            {
                log.debug(he.getMessage());
            }
            throw he;
        }
    }

    /**
     * 국가 > 대리점 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @Operation(summary = "대리점 리스트 조회", description = "")
    @RequestMapping(value="/co/country/agency/list")
    public List<EmfMap> getAgencyList(@ApiData EmfMap emfMap, @RequestParam("ntnCd") String ntnCd) throws Exception
    {
        try
        {
            emfMap.put("nm", "Distributors");
            return cmmUseService.getAgencyList(emfMap);
        }
        catch (Exception he)
        {
            if (log.isErrorEnabled())
            {
                log.error(he.getMessage());
            }
            throw he;
        }

    }

    /**
     * 국가 > 대리점 > 딜러 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @Operation(summary = "딜러 리스트 조회", description = "")
    @RequestMapping(value="/co/country/agency/delear/list")
    public List<EmfMap> getDelearList(@ApiData EmfMap emfMap, @RequestParam("ntnCd") String ntnCd, @RequestParam("dlspCd") String dlspCd) throws Exception
    {
        try
        {
            emfMap.put("nm", "Dealer");
            return cmmUseService.getDelearList(emfMap);
        }
        catch (Exception he)
        {
            if (log.isErrorEnabled())
            {
                log.error(he.getMessage());
            }
            throw he;
        }

    }


    /**
     * 국가 > 대리점 > 딜러 그룹 조회
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @Operation(summary = "딜러 그룹 리스트 조회", description = "")
    @RequestMapping(value="/co/country/agency/delearGrp/list")
    public List<EmfMap> getDelearGrpList(@ApiData EmfMap emfMap, @RequestParam("ntnCd") String ntnCd, @RequestParam("dlspCd") String dlspCd) throws Exception
    {
        try
        {
            emfMap.put("nm", "Dealer Group");
            return cmmUseService.getDelearGrpList(emfMap);
        }
        catch (Exception he)
        {
            if (log.isErrorEnabled())
            {
                log.error(he.getMessage());
            }
            throw he;
        }
    }



    @Operation(summary = "뱃지 조회", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value="/get-badge-list")
    public List<EmfMap> getBadgeList(@ApiData EmfMap emfMap, @AuthenticationPrincipal LoginUser loginUser) throws Exception
    {
        List<EmfMap> rtnList = null;
        try
        {
            emfMap.put("dlspCd", loginUser.getDlspCd());
            emfMap.put("dlrCdList", loginUser.getDlrCdList());
            emfMap.put("authCd", loginUser.getAuthCd());
            rtnList = cmmUseService.getBadgeList(emfMap);
        }
        catch (Exception he)
        {
            if (log.isErrorEnabled())
            {
                log.error(he.getMessage());
            }
            throw he;
        }
        return rtnList;
    }

    /**
     * 파일 ajax 업로드
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
   /* @RequestMapping(value="/mngwserc/co/file-upload.ajax")
    public String setFileUpload(EmfMap emfMap, ModelMap model, HttpServletRequest request, HttpServletResponse response, MultipartHttpServletRequest multiRequest) throws Exception
    {
        try
        {
            //첨부파일 처리
            final Map<String, MultipartFile> files = multiRequest.getFileMap();

            if (!files.isEmpty())
            {
                Iterator<Map.Entry<String, MultipartFile>> itr = files.entrySet().iterator();
                MultipartFile file;

                int atchFileCnt = 0;

                while (itr.hasNext())
                {
                    Map.Entry<String, MultipartFile> entry = itr.next();

                    file = entry.getValue();

                    if (file.getName().indexOf("atchFile") > -1)
                    {
                        atchFileCnt++;
                    }
                }

                if (atchFileCnt > 0)
                {
                    int fileSize = Integer.parseInt(atchFileSize);

                    if (!"".equals(emfMap.getString("atchFileSize")))
                    {
                        fileSize = Integer.parseInt(emfMap.getString("atchFileSize")) * 1024 * 1024;
                    }

                    int fileSeq = 0;

                    String atchFileId = emfMap.getString("atchFileId");

                    if (!"".equals(atchFileId))
                    {
                        if (!"".equals(emfMap.getString("fileSeq")))
                        {
                            //파일 삭제
                            fileMngService.deleteFileInf(emfMap);

                            fileSeq = Integer.parseInt(emfMap.getString("fileSeq"));
                        }
                        else
                        {
                            fileSeq = fileMngService.getMaxFileSeq(atchFileId);
                        }
                    }
                    else
                    {
                        fileSeq = 0;
                    }

                    List<EmfMap> atchFileList = fileMngUtil.parseFileInf(files, "RSC_", fileSeq, atchFileId, "Globals.fileStorePath", "atchFile", fileSize, atchFileExtns.split(","), false);

                    if (atchFileList != null && atchFileList.size() > 0)
                    {
                        model.addAttribute("atchFileList", COJsonUtil.getJsonArrayFromList(atchFileList));

                        if (atchFileList.size() > 0)
                        {
                            fileMngService.insertFileInfs(atchFileList);
                        }
                    }
                }
            }
        }
        catch (Exception he)
        {
            if (log.isErrorEnabled())
            {
                log.error(he.getMessage());
            }
            he.printStackTrace();
            throw he;
        }

        return "jsonView";
    }*/

    /**
     * 파일 ajax 삭제
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    /*@RequestMapping(value="/mngwserc/co/file-delete.ajax")
    public String setFileDelete(EmfMap emfMap, ModelMap model, HttpServletRequest request, HttpServletResponse response
            , @RequestParam(value="atchFileId", required=true) String atchFileId
            , @RequestParam(value="fileSeq", required=true) int fileSeq ) throws Exception
    {
        int actCnt = 0;

        try
        {
            //파일 조회
            EmfMap fileMap = fileMngService.selectFileInf(emfMap);

            if (fileMap != null)
            {
                //첨부파일 처리
                fileMngService.deleteFileInf(emfMap);

                String phyPathFile = fileMap.getString("phyPath") + File.separator + fileMap.getString("saveFileNm");

                File file = new File(phyPathFile);

                if (file.exists())
                {
                    if (file.delete())
                    {
                        actCnt = 1;
                    }
                    else
                    {
                        actCnt = 0;
                    }
                }
                else
                {
                    actCnt = 0;
                }
            }
        }
        catch (Exception he)
        {
            if (log.isErrorEnabled())
            {
                log.error(he.getMessage());
            }
            he.printStackTrace();
            throw he;
        }

        model.addAttribute("actCnt", actCnt);

        return "jsonView";
    }*/

    /**
     * 파일 복사 시작
     *
     * @param emfMap
     * @return String View URL
     * @throws Exception
     */
    @RequestMapping(value="/mngwserc/co/file-move.ajax")
    public String setFileMove(EmfMap emfMap, ModelMap model, HttpServletRequest request, HttpServletResponse response
            , @RequestParam(value="atchFileId", required=true) String[] atchFileIdList) throws Exception
    {
        try
        {
            //파일 복사 시작
            System.out.println(atchFileIdList.length);
        }
        catch (Exception he)
        {
            if (log.isErrorEnabled())
            {
                log.error(he.getMessage());
            }
            he.printStackTrace();
            throw he;
        }
        return "jsonView";
    }

}
