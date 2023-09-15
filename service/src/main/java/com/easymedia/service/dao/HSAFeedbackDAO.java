package com.easymedia.service.dao;

import java.util.List;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;

/**
 * <pre> 
 * HGSI - Feedback DAO
 * </pre>
 * 
 * @ClassName		: HSAFeedbackDAO.java
 * @Description		: HGSI - Feedback DAO
 * @author 허진영
 * @since 2019.01.21
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.01.21		허진영					최초 생성
 * </pre>
 */
@Mapper
public interface HSAFeedbackDAO {
	
	/**
	 * 딜러 List
	 * 
	 * @param emfMap 데이터
	 * @return
	 */
	public List<EmfMap> HSAFeedbackList(EmfMap emfMap) throws Exception;
	
	/**
	 * 딜러 Total Count
	 * 
	 * @param emfMap 데이터
	 * @return
	 */
	public int selectDlrListTotCnt(EmfMap emfMap) throws Exception;
	
	/**
	 * 딜러 Status Count
	 * 
	 * @param emfMap 데이터
	 * @return
	 */
	public EmfMap getDlrSttsCnt(EmfMap emfMap) throws Exception;
	
	/**
	 * 고객 List
	 * 
	 * @param emfMap 데이터
	 * @return
	 */
	public List<EmfMap> selectCstmrList(EmfMap emfMap) throws Exception;
	
	/**
	 * 고객 Total Count
	 * 
	 * @param emfMap 데이터
	 * @return
	 */
	public int selectCstmrListTotCnt(EmfMap emfMap) throws Exception;
	
	/**
	 * 고객 Status Count
	 * 
	 * @param emfMap 데이터
	 * @return
	 */
	public EmfMap getCstmrSttsCnt(EmfMap emfMap) throws Exception;
	
	/**
	 * 고객 Details
	 * 
	 * @param emfMap 데이터
	 * @return
	 */
	public EmfMap selectCstmrDtl(EmfMap emfMap) throws Exception;

	/**
	 * 고객 기본 설문 답변
	 * 
	 * @param emfMap 데이터
	 * @return
	 */
	public List<EmfMap> selectCstmrBscSrvyAnsw(EmfMap emfMap) throws Exception;
	
	/**
	 * 고객 추가 설문 답변
	 * 
	 * @param emfMap 데이터
	 * @return
	 */
	public List<EmfMap> selectCstmrAddSrvyAnsw(EmfMap emfMap) throws Exception;

	/**
	 * 고객 설문 오픈 여부 Update
	 * 
	 * @param emfMap 데이터
	 * @return
	 */
	public int updateCstmrSrvyOpnYn(EmfMap emfMap) throws Exception;
	
	/**
	 * 엑셀다운로드
	 *
	 * @param emfMap 데이터
	 * @return
	 */
	public List<EmfMap> excelCstmrList(EmfMap emfMap) throws Exception;
	
	/**
	 * 질문(취약점) List
	 * 
	 * @param emfMap 데이터
	 * @return
	 */
	public List<EmfMap> selectSrvyWeakList(EmfMap emfMap) throws Exception;
	
	/**
	 * 추가질문 List
	 * 
	 * @param emfMap 데이터
	 * @return
	 */
	public List<EmfMap> getAddAnswList(EmfMap emfMap) throws Exception;
}