package com.easymedia.service.impl;

import com.easymedia.dto.EmfMap;
import com.easymedia.service.COFSysLogService;
import com.easymedia.service.dao.COFSysLogDAO;
import com.easymedia.utility.COJsonUtil;
import com.easymedia.utility.COPaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * <pre>
 * 로그관리(시스템)를 위한 ServiceImpl
 * </pre>
 *
 * @ClassName		: COFSysLogServiceImpl.java
 * @Description		: 로그관리(시스템)를 위한 ServiceImpl
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
@Slf4j
@Service
@RequiredArgsConstructor
public class COFSysLogServiceImpl  implements COFSysLogService
{
	/** 시스템 로그 DAO **/
	private final COFSysLogDAO cOFSysLogDAO;
	/** 시스템 로그 ID **/
	private final EgovIdGnrService egovSysLogIdGnrService;

	/**
	 * 로그 목록을 조회한다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public EmfMap logSelectSysLogList(EmfMap emfMap) throws Exception
	{
		EmfMap rtnMap = new EmfMap();
		
		PaginationInfo paginationInfo = new COPaginationUtil().getPage(emfMap);

		String[] strtDt = emfMap.getString("strtDt").split("/");
		String[] endDt = emfMap.getString("endDt").split("/");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(endDt[1]), Integer.parseInt(endDt[0])-1, 1);
    	
    	emfMap.put("strtDt", strtDt[0] + "/01/" + strtDt[1]);
    	emfMap.put("endDt", endDt[0] + "/"+cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/" + endDt[1]);
		
		// 리스트 가져오기
		List<EmfMap> list = cOFSysLogDAO.logSelectSysLogList(emfMap);

		// 총 건수 가져오기
		int totCnt = cOFSysLogDAO.logSelectSysLogListTotCnt(emfMap);

		paginationInfo.setTotalRecordCount(totCnt);

		rtnMap.put("totCnt", totCnt);
		rtnMap.put("pageIndex", paginationInfo.getCurrentPageNo());
		rtnMap.put("recordPerPage", paginationInfo.getRecordCountPerPage());
		rtnMap.put("totPage", paginationInfo.getTotalPageCount());
		rtnMap.put("list", COJsonUtil.getJsonArrayFromList(list));

		return rtnMap;
	}

	/**
	 * 로그 목록을 조회한다. (엑셀 다운로드)
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public EmfMap logExcelSysLogList(EmfMap emfMap) throws Exception
	{
		EmfMap rtnMap = new EmfMap();
		
		String[] strtDt = emfMap.getString("strtDt").split("/");
		String[] endDt = emfMap.getString("endDt").split("/");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(endDt[1]), Integer.parseInt(endDt[0])-1, 1);
    	
    	emfMap.put("strtDt", strtDt[0] + "/01/" + strtDt[1]);
    	emfMap.put("endDt", endDt[0] + "/"+cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/" + endDt[1]);
		
		rtnMap.put("list", COJsonUtil.getJsonArrayFromList(cOFSysLogDAO.logExcelSysLogList(emfMap)));
		
		return rtnMap;
	}

	/**
	 * 시스템 로그정보를 생성한다.
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public void logInsertSysLog(EmfMap emfMap) throws Exception
	{
		try
		{
			emfMap.put("logId", egovSysLogIdGnrService.getNextStringId());

			cOFSysLogDAO.logInsertSysLog(emfMap);
		}
		catch(Exception e)
		{
			log.debug(e.getMessage());
		}
	}

	/**
	 * 시스템 에러 등록
	 *
	 * @param emfMap
	 * @return
	 * @exception Exception
	 */
	public void logInsertErrLog(EmfMap emfMap) throws Exception
	{
		try
		{
			emfMap.put("logId", egovSysLogIdGnrService.getNextStringId());

			cOFSysLogDAO.logInsertErrLog(emfMap);
		}
		catch(Exception e)
		{
			log.debug(e.getMessage());
		}
	}
}