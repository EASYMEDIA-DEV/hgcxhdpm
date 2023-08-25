package com.easymedia.service.dao;

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
public interface COMailDAO
{
    /**
     * 메일 등록
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public int setEmailSendMst(EmfMap emfMap) throws Exception;

    /**
     * 메일 상세 등록
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public int setEmailSendDtl(EmfMap emfMap) throws Exception;
}