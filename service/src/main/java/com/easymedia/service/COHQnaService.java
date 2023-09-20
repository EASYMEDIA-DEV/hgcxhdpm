package com.easymedia.service;

import com.easymedia.dto.EmfMap;

import java.util.List;

/**
 * <pre>
 * 딜러-계정관리
 * </pre>
 *
 * @ClassName		: SCBDealerAuthService.java
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
public interface COHQnaService {

    /**
     * Sample List
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectQnaList(EmfMap emfMap) throws Exception;

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
     * 댓글 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public List<EmfMap> getQnaCommentList(EmfMap emfMap) throws Exception;

    /**
     * 댓글 등록
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int insertQnaComment(EmfMap emfMap) throws Exception;

    /**
     * QNA 삭제
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int deleteQna(EmfMap emfMap) throws Exception;

    /**
     * 댓글 삭제
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public int deleteQnaComment(EmfMap emfMap) throws Exception;

}