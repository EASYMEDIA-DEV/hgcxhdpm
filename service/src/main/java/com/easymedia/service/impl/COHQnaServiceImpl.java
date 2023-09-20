package com.easymedia.service.impl;

import com.easymedia.dto.EmfMap;
import com.easymedia.service.COHQnaService;
import com.easymedia.service.dao.COHQnaDAO;
import com.easymedia.utility.COJsonUtil;
import com.easymedia.utility.COPaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.idgnr.impl.EgovTableIdGnrServiceImpl;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * <pre>
 * 딜러-계정관리
 * </pre>
 *
 * @ClassName		: SCBDealerAuthServiceImpl.java
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
@Slf4j
@Service
@RequiredArgsConstructor
public class COHQnaServiceImpl implements COHQnaService {

    private final COHQnaDAO cOHQnaDAO;

	private final EgovTableIdGnrServiceImpl qstnIdgen;

	private EgovTableIdGnrServiceImpl rplyIdgen;


	/**
     * Sample List
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectQnaList(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();
    	PaginationInfo paginationInfo = new COPaginationUtil().getPage(emfMap);

		// 리스트 가져오기
		List<EmfMap> list = cOHQnaDAO.selectQnaList(emfMap);

		//공지 리스트 조회
		List<EmfMap> ancmList = cOHQnaDAO.selectAncmQnaList(emfMap);

		// 총 건수 가져오기
		int totCnt = cOHQnaDAO.selectQnaListTotCnt(emfMap);


		paginationInfo.setTotalRecordCount(totCnt);

		rtnMap.put("totCnt", totCnt);
		rtnMap.put("pageIndex", paginationInfo.getCurrentPageNo());
		rtnMap.put("recordPerPage", paginationInfo.getRecordCountPerPage());
		rtnMap.put("totPage", paginationInfo.getTotalPageCount());
		rtnMap.put("list", COJsonUtil.getJsonArrayFromList(list));
		rtnMap.put("ancmList", COJsonUtil.getJsonArrayFromList(ancmList));
    	return rtnMap;
    }

    /**
     * 관리자 상세
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectQnaDtl(EmfMap emfMap) throws Exception
    {
    	if (!"".equals(emfMap.getString("detailsKey")))
    	{
    		//상세 조회
    		EmfMap info = cOHQnaDAO.selectQnaDtl(emfMap);
    		if(info != null)
    		{
    			emfMap.put("info", info);
    		}
    	}
    	return emfMap;
    }

    /**
     * Sample Details
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int insertQnaDtl(EmfMap emfMap) throws Exception
    {
    	int qstnSeq = qstnIdgen.getNextIntegerId();
    	emfMap.put("qstnSeq", qstnSeq);
    	return cOHQnaDAO.insertQnaDtl(emfMap);
    }


    /**
     * Sample Details
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int updateQnaDtl(EmfMap emfMap) throws Exception
    {
    	return cOHQnaDAO.updateQnaDtl(emfMap);
    }


    /**
     * 댓글 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public List<EmfMap> getQnaCommentList(EmfMap emfMap) throws Exception
    {
    	List<EmfMap> commentList = null;
    	if (!"".equals(emfMap.getString("detailsKey")))
    	{
    		//상세 조회
    		commentList = cOHQnaDAO.getQnaCommentList(emfMap);
    	}
    	return commentList;
    }

    /**
     * 댓글 등록
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int insertQnaComment(EmfMap emfMap) throws Exception
    {
    	int rplySeq = rplyIdgen.getNextIntegerId();
    	emfMap.put("rplySeq", rplySeq);
    	return cOHQnaDAO.insertQnaComment(emfMap);
    }

    /**
     * QNA 삭제
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int deleteQna(EmfMap emfMap) throws Exception
    {
    	return cOHQnaDAO.deleteQna(emfMap);
    }

    /**
     * 댓글 삭제
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int deleteQnaComment(EmfMap emfMap) throws Exception
    {
    	return cOHQnaDAO.deleteQnaComment(emfMap);
    }
}