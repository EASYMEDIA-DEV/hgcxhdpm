package com.easymedia.utility;

import com.easymedia.dto.EmfMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * 페이징 처리 함수
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    	 --------    ---------------------------
 *   2015.07.20  박주석          최초 생성
 *
 * </pre>
 */
public class COPaginationUtil
{
	//페이지 사이즈
	private static int PageSize = 10;
	
	//리스트 사이즈
	private static int CntPage = 10;
	
	/**
	 * 페이지 정보를 가져온다.
	 * 
	 * @param emfMap 현재 페이지
	 */
	public static PaginationInfo getPage(EmfMap emfMap) throws Exception
	{
		if ("".equals(emfMap.getString("pageIndex")))
		{
			emfMap.put("pageIndex", 1);
		}
		
		PaginationInfo page = new PaginationInfo();
		
    	page.setCurrentPageNo(Integer.parseInt(emfMap.getString("pageIndex")));
    	
    	if (!"".equals(emfMap.getString("listCnt")))
		{
			page.setRecordCountPerPage(Integer.parseInt(emfMap.getString("listCnt")));
		}
		else
		{
			page.setRecordCountPerPage(CntPage);
		}
    	
    	page.setPageSize(PageSize);
    	
    	emfMap.put("firstIndex", page.getFirstRecordIndex());
    	emfMap.put("recordCountPerPage", page.getRecordCountPerPage());
    	
		return page;
	}
}