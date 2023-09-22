package com.easymedia.api.controller;

import com.easymedia.api.annotation.ApiData;
import com.easymedia.dto.EmfMap;
import com.easymedia.error.ErrorCode;
import com.easymedia.error.ErrorResponse;
import com.easymedia.error.exception.BusinessException;
import com.easymedia.service.*;
import com.easymedia.utility.EgovFileMngUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * QNA Controller
 * </pre>
 *
 * @ClassName		: COHQnaController.java
 * @Description		: 딜러-계정관리
 * @author 박주석
 * @since 2019.01.11
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.01.11		  박주석					     최초생성
 * </pre>
 */
@Tag(name = "QNA", description = "QNA")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qna")
public class COHQnaController {

	// 코드 조회
    private final EgovCmmUseService cmmUseService;
	// 비지니스 조회
	private final COHQnaService cOHQnaService;

   	private final EgovFileMngUtil fileUtil;

    private final EgovFileMngService fileMngService;

	@Value("${globals.qna-to-user}")
	private String globalsToUser;
	@Value("${globals.http-admin-url}")
	private String httpAdminUrl;

	/* 메일 서비스 */
    private final MailService mailService;

	@Operation(summary = "리스트", description = "")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@GetMapping(value="/list")
	public EmfMap selectSDealershipListAjax(@ApiData EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		EmfMap rtnData = null;
		try
		{
			rtnData = cOHQnaService.selectQnaList(emfMap);
		}
		catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
			}
			throw he;
		}

		return rtnData;
	}

	@Operation(summary = "리스트 상세", description = "")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@GetMapping(value="/view")
	public EmfMap selectDealershipView(@ApiData  EmfMap emfMap, ModelMap modelMap, HttpSession session) throws Exception
	{
		EmfMap rtnData = null;
		String detailsKey = emfMap.getString("detailsKey");
		
		if("".equals(detailsKey))
		{
			emfMap.put("detailsKey", (String)session.getAttribute("detailsKey"));
		}else
		{
			session.setAttribute("detailsKey", detailsKey);
		}
		try
		{
			rtnData = cOHQnaService.selectQnaDtl(emfMap);
			if(rtnData.get("info") == null)
			{
				throw new BusinessException(ErrorCode.NOT_FOUND);
			}
			if(rtnData.get("info") != null && !"".equals(((EmfMap)rtnData.get("info")).getString("atchFileId")))
			{
				EmfMap fileMap = new EmfMap();
				fileMap.put("atchFileId", ((EmfMap)rtnData.get("info")).getString("atchFileId"));
				emfMap.put("atchFile", fileMngService.selectFileInfs(fileMap));
			}
			modelMap.addAttribute("rtnData", rtnData);
		}
		catch (Exception he)
		{
			if (log.isErrorEnabled())
			{
				log.error(he.getMessage());
            }
			throw he;
		}

		return rtnData;
	}

	@Operation(summary = "QNA 삭제", description = "")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@GetMapping(value="/delete")
	public int delQna(@ApiData EmfMap emfMap) throws Exception
	{
		int actCnt = 0;
		try
		{
			actCnt = cOHQnaService.deleteQna(emfMap);
		}
		catch (Exception he)
		{
			if (log.isErrorEnabled())
			{
				log.error(he.getMessage());
			}
			throw he;
		}

		return actCnt;
	}

	@Operation(summary = "QNA 댓글 조회", description = "")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@GetMapping(value="/comment/list")
	public List<EmfMap> getQnaCommentList(@ApiData EmfMap emfMap) throws Exception
	{
		List<EmfMap> rtnData = null;
		try
		{
			rtnData = cOHQnaService.getQnaCommentList(emfMap);
		}
		catch (Exception he)
		{
			if (log.isErrorEnabled())
			{
				log.error(he.getMessage());
			}
			throw he;
		}

		return rtnData;
	}

	@Operation(summary = "QNA 댓글 삭제", description = "")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@GetMapping(value="/comment/delete")
	public int delComment(@ApiData EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		int actCnt = 0;
		try
		{
			actCnt = cOHQnaService.deleteQnaComment(emfMap);
		}
		catch (Exception he)
		{
			if (log.isErrorEnabled())
			{
				log.error(he.getMessage());
			}
			throw he;
		}
		return actCnt;
	}

	@Operation(summary = "QNA 댓글 등록", description = "")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@PostMapping(value="/comment/insert")
	public String setComment(@RequestBody  @ApiData EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		int actCnt = 0;
		try
		{
			//이메일 발송
			actCnt = cOHQnaService.insertQnaComment(emfMap);
			if(actCnt > 0)
			{
				//누가 썼는지 확인해서 보낼야 할 사람 정함
				List<EmfMap> list = new ArrayList<EmfMap>();
				EmfMap mailMap = new EmfMap();
				mailMap.setCamelYn("N");
				emfMap.put("admId", AuthChecker.getLoginUser().getId());
				emfMap.put("admName", AuthChecker.getLoginUser().getName());
				if(!emfMap.getString("admId").equals(emfMap.getString("dtlRegId")))
				{
					mailMap.put("email", emfMap.getString("dtlRegId"));
					mailMap.put("etc2" , globalsToUser);
				}
				else
				{
					mailMap.put("email", globalsToUser);
					mailMap.put("etc2" , emfMap.getString("admId") + "(" + emfMap.getString("admName") + ")");
				}
				mailMap.put("etc1" , httpAdminUrl);
				mailMap.put("etc3" , emfMap.getString("titl"));
				list.add(mailMap);
				emfMap.put("toMailList", list);
				emfMap.put("title", "[Hyundai Global Customer Experience] New Q&A comment has been added");
				mailService.sendEventTempleteMail(emfMap, "COHQnaWrite.html");
			}
			modelMap.addAttribute("actCnt", actCnt);
		}
		catch (Exception he)
		{
			if (log.isErrorEnabled())
			{
				log.error(he.getMessage());
			}
			he.printStackTrace();
			modelMap.addAttribute("actCnt", actCnt);
		}

		return "jsonView";
	}


	/**
	 * QNA 등록
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
	@Operation(summary = "QNA 등록", description = "")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@PostMapping(value="/insert")
	public int insertQna(@ApiData EmfMap emfMap, ModelMap modelMap, MultipartHttpServletRequest multiRequest) throws Exception
	{
		int actCnt = 0;
		try
		{
			log.error(multiRequest.getFileMap().toString());
			log.error(emfMap.toString());
			List<EmfMap> atchFileList = fileUtil.parseFileInf(multiRequest.getFileMap(), "", 0, "", "Globals.fileStorePath", "atchFile", 20971520, "jpg,jpeg,gif,png,bmp,pdf,ppt,pptx,xls,xlsx,doc,docx,hwp,txt,zip".split(","));

			String atchFileId = "";
			if (atchFileList.size() > 0)
			{
				atchFileId = fileMngService.insertFileInfs(atchFileList);
				emfMap.put("atchFileId", atchFileId);
			}
			actCnt = cOHQnaService.insertQnaDtl(emfMap);
			//이메일 발송
			if(actCnt > 0)
			{
				List<EmfMap> list = new ArrayList<EmfMap>();
				EmfMap mailMap = new EmfMap();
				mailMap.setCamelYn("N");
				mailMap.put("email", globalsToUser);
				mailMap.put("etc1" , httpAdminUrl);
				mailMap.put("etc2" , emfMap.getString("admId") + "(" + emfMap.getString("admName") + ")");
				mailMap.put("etc3" , emfMap.getString("titl"));
				mailMap.put("etc4" , emfMap.getString("cntn"));
				list.add(mailMap);
				emfMap.put("toMailList", list);
				emfMap.put("title", "[Hyundai Global Customer Experience] New Q&A post has been added");
				mailService.sendEventTempleteMail(emfMap, "COHQnaWrite.html");
			}
			modelMap.addAttribute("actCnt", actCnt);
		}
		catch (Exception he)
		{
			if (log.isErrorEnabled())
			{
				log.error(he.getMessage());
            }
			throw he;
		}

		return actCnt;
	}

	@Operation(summary = "QNA 수정", description = "")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@PostMapping(value="/update")
	public int updateQna(@ApiData EmfMap emfMap, MultipartHttpServletRequest multiRequest) throws Exception
	{
		int actCnt = 0;
		try
		{
			if(!"".equals(emfMap.getString("delAtchFileSeq")))
			{
				//삭제
				EmfMap fileMap = new EmfMap();
				fileMap.put("atchFileId", emfMap.getString("atchFileId"));
				fileMap.put("fileSeq", emfMap.getString("delAtchFileSeq"));
				fileMngService.deleteFileInf(fileMap);
			}
			List<EmfMap> atchFileList = fileUtil.parseFileInf(multiRequest.getFileMap(), "", 0, emfMap.getString("atchFileId"), "Globals.fileStorePath", "atchFile", 20971520, "jpg,jpeg,gif,png,bmp,pdf,ppt,pptx,xls,xlsx,doc,docx,hwp,txt,zip".split(","));
			String atchFileId = emfMap.getString("atchFileId");
			if (atchFileList.size() > 0)
			{
				if(!"".equals( atchFileId ))
				{
					fileMngService.updateFileInfs(atchFileList);
				}
				else
				{
					atchFileId = fileMngService.insertFileInfs(atchFileList);
				}
			}
			emfMap.put("atchFileId", atchFileId);
			actCnt = cOHQnaService.updateQnaDtl(emfMap);
		}
		catch (Exception he)
		{
			if (log.isErrorEnabled())
			{
				log.error(he.getMessage());
            }
			throw he;
		}

		return actCnt;
	}









}