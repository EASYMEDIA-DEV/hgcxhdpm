package com.easymedia.service.dao;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Class Name : CmmUseDAO.java
 * @Description : 공통코드등 전체 업무에서 공용해서 사용해야 하는 서비스를 정의하기위한 데이터 접근 클래스
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
public interface CmmUseDAO
{
	/**
     * 주어진 조건에 따른 공통코드를 불러온다.
     *
     * @param cdDtList
     * @return
     * @throws Exception
     */
    public List<EmfMap> getCmmCodeBindAll(EmfMap cdDtList) throws Exception;

    /**
     * 주어진 조건에 따른 공통코드를 불러온다.
     *
     * @param emfMap
     * @return
     * @throws Exception
     */
    public List<EmfMap> selectCmmCodeDetail(EmfMap emfMap) throws Exception;

    /**
     * 국가 조회
     *
     * @param emfMap
     * @return
     * @throws Exception
     */
    public List<EmfMap> getNationList(EmfMap emfMap) throws Exception;

    /**
     * 국가 > 대리점 조회
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public List<EmfMap> getAgencyList(EmfMap emfMap) throws Exception;

    /**
     * 국가 > 대리점 > 딜러 조회
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public List<EmfMap> getDelearList(EmfMap emfMap) throws Exception;

    /**
     * 국가 > 대리점 > 딜러 그룹 조회
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public List<EmfMap> getDelearGrpList(EmfMap emfMap) throws Exception;

    /**
     * 뱃지 조회
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public List<EmfMap> getBadgeList(EmfMap emfMap) throws Exception;

    /**
     * 뱃지 등록
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public int setBadge(EmfMap emfMap) throws Exception;
    
    /**
     * 현대 딜러코드로 딜러코드 조회
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public EmfMap getDlrCd(EmfMap emfMap) throws Exception;
    
    /**
     * 다중접속 방지를 위한 세션정보 저장
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    
    public int updateSessionAdm(EmfMap emfMap) throws Exception;

    /**
     * 주어진 조건에 따른 공통코드 단건을 불러온다.
     *
     * @param emfMap
     * @return
     * @throws Exception
     */
    public EmfMap getCmmCodeDtl(EmfMap emfMap) throws Exception;
}