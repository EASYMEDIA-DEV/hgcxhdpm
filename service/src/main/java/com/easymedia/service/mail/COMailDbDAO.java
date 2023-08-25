package com.easymedia.service.mail;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Class Name : COMailDAO.java
 * @Description : 이메일
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 11.     이삼섭
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 11.
 * @version
 * @see
 *
 */
@Mapper
public interface COMailDbDAO
{
    /**
     * 주소록그룹 생성
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public int setMakeTarget(EmfMap emfMap) throws Exception;


    /**
     * 주소록 삭제
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public int setDeleteTargetList(EmfMap emfMap) throws Exception;


    /**
     * 주소록 등록
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public int setCsvImport(EmfMap emfMap) throws Exception;

    /**
     * 캠페인 생성
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public int setTargetListCnt(EmfMap emfMap) throws Exception;

    /**
     * 캠페인 생성
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public int setMakeCampaign(EmfMap emfMap) throws Exception;

    /**
     * 이벤트 메일 발송
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public int insertEventEmail(EmfMap emfMap) throws Exception;

}