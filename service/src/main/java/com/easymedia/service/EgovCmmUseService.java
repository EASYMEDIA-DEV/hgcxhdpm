package com.easymedia.service;

import com.easymedia.dto.EmfMap;

import java.util.ArrayList;
import java.util.List;

/**
 * 공통코드등 전체 업무에서 공용해서 사용해야 하는 서비스를 정의하기 위한 서비스 인터페이스
 * @author 공통서비스 개발팀 이삼섭
 * @since 2009.04.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.11  이삼섭          최초 생성
 *
 * </pre>
 */
public interface EgovCmmUseService
{
	/**
     * 공통코드를 조회한다.
     *
     * @param cdDtList
     * @return EmfMap
     * @throws Exception
     */
    public EmfMap getCmmCodeBindAll(ArrayList<String> cdDtList) throws Exception;

    /**
     * 공통코드를 조회한다.
     *
     * @param emfMap
     * @return List<EmfMap>
     * @throws Exception
     */
    public List<EmfMap> selectCmmCodeDetail(EmfMap emfMap) throws Exception;

    /**
     * 공통코드를 조회한다.
     *
     * @param emfList
     * @return EmfMap
     * @throws Exception
     */
    public EmfMap selectCmmCodeDetails(List<EmfMap> emfList) throws Exception;

    /**
     * 국가 조회
     *
     * @param emfMap
     * @return EmfMap
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
     * 현대 딜러 코드로 딜러 코드 조회
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public EmfMap getDlrCd(EmfMap emfMap) throws Exception;
    
    /**
     * 세션 정보 저장
     *
     * @param emfMap
     * @return int
     * @throws Exception
     */
    public int updateSessionAdm(EmfMap emfMap) throws Exception;
}