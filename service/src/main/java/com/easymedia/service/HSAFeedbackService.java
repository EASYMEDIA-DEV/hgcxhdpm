package com.easymedia.service;


import com.easymedia.dto.EmfMap;

public interface HSAFeedbackService {

    /**
     * Feedback List
     *
     * @param emfMap 데이터
     * @return
     */
    public EmfMap HSAFeedbackList(EmfMap emfMap) throws Exception;

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
    public EmfMap selectCstmrList(EmfMap emfMap) throws Exception;

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
     * 엑셀다운로드
     *
     * @param emfMap 데이터
     * @return
     */
    public EmfMap excelCstmrList(EmfMap emfMap) throws Exception;
}
