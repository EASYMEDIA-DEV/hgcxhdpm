package com.easymedia.service.dao;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre>
 * QNA DAO
 * </pre>
 *
 * @ClassName		: COHQnaDAO.java
 * @Description		: QNA
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
@Mapper
public interface COHQnaDAO {

	/**
	 * 공지 리스트 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> selectAncmQnaList(EmfMap emfMap) throws Exception;

    /**
	 * Sample List
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> selectQnaList(EmfMap emfMap) throws Exception;
		
	/**
	 * Sample Total Count
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int selectQnaListTotCnt(EmfMap emfMap) throws Exception;

	/**
     * Sample Details
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectQnaDtl(EmfMap emfMap) throws Exception;

    /**
     * Sample Details
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int insertQnaDtl(EmfMap emfMap) throws Exception;

    /**
     * Sample Details
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int updateQnaDtl(EmfMap emfMap) throws Exception;


    /**
	 * Sample List
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getQnaCommentList(EmfMap emfMap) throws Exception;

	 /**
     * QNA 삭제
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int deleteQna(EmfMap emfMap) throws Exception;


	/**
     * 댓글 등록
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int insertQnaComment(EmfMap emfMap) throws Exception;

    /**
     * 댓글 삭제
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int deleteQnaComment(EmfMap emfMap) throws Exception;
}