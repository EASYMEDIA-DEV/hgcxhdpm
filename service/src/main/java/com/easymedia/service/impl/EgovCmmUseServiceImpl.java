package com.easymedia.service.impl;

import com.easymedia.dto.EmfMap;
import com.easymedia.service.EgovCmmUseService;
import com.easymedia.service.dao.CmmUseDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Class Name : EgovCmmUseServiceImpl.java
 * @Description : 공통코드등 전체 업무에서 공용해서 사용해야 하는 서비스를 정의하기위한 서비스 구현 클래스
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
@Slf4j
@Service
@RequiredArgsConstructor
public class EgovCmmUseServiceImpl implements EgovCmmUseService
{
	/** 코드 **/
    private final CmmUseDAO cmmUseDAO;

    /** 관리자 알림 시퀀스 조회 **/
	private final EgovIdGnrService admNtcIdgen;

    /**
     * 공통코드를 조회한다.
     *
     * @param cdDtList
     * @return EmfMap
     * @throws Exception
     */
    public EmfMap getCmmCodeBindAll(ArrayList<String> cdDtList) throws Exception
    {
    	EmfMap cdMap = new EmfMap();
    	cdMap.put("cdIdList", cdDtList);
    	List<EmfMap> codeList = cmmUseDAO.getCmmCodeBindAll(cdMap);

    	HashMap<String, Object> codeMap = new HashMap<String, Object>();

    	ArrayList<EmfMap> list;


    	if (codeList != null && codeList.size() > 0)
    	{
    		for (int i = 0; i < codeList.size(); i++)
        	{
        		cdMap = (EmfMap) codeList.get(i);

        		if (codeMap.get(cdMap.getString("cdId")) == null)
    			{
    				codeMap.put(cdMap.getString("cdId"), new ArrayList<EmfMap>());
    			}

        		list = (ArrayList<EmfMap>) codeMap.get(cdMap.getString("cdId"));
    			list.add(cdMap);
    		}
    	}

    	String cdId = "";

    	EmfMap rtnMap = new EmfMap();

		for (int i = 0; i < cdDtList.size(); i++)
    	{
			cdId = cdDtList.get(i).toString();

			if (codeMap.get(cdId) != null)
			{
				rtnMap.put(cdId, (ArrayList<EmfMap>) codeMap.get(cdId));
			}
		}

    	return rtnMap;
    }

    /**
     * 공통코드를 조회한다.
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public List<EmfMap> selectCmmCodeDetail(EmfMap emfMap) throws Exception
    {
    	return cmmUseDAO.selectCmmCodeDetail(emfMap);
    }

    /**
     * 공통코드를 조회한다.
     *
     * @param emfList
     * @return EmfMap
     * @throws Exception
     */
    public EmfMap selectCmmCodeDetails(List<EmfMap> emfList) throws Exception
    {
		EmfMap tmpMap = new EmfMap();
		EmfMap rtnMap = new EmfMap();

		for (int i = 0; i < emfList.size(); i++)
		{
			tmpMap = emfList.get(i);
			rtnMap.put(String.valueOf(tmpMap.get("cdId")), cmmUseDAO.selectCmmCodeDetail(tmpMap));
		}

		return rtnMap;
    }

    /**
     * 국가 조회
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public List<EmfMap> getNationList(EmfMap emfMap) throws Exception
    {
    	return cmmUseDAO.getNationList(emfMap);
    }

    /**
     * 국가 > 대리점 조회
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public List<EmfMap> getAgencyList(EmfMap emfMap) throws Exception
    {
    	return cmmUseDAO.getAgencyList(emfMap);
    }

    /**
     * 국가 > 대리점 > 딜러 조회
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public List<EmfMap> getDelearList(EmfMap emfMap) throws Exception
    {
    	List<EmfMap> list = null;
    	list = cmmUseDAO.getDelearList(emfMap);
    	return list;
    }
    
    
    /**
     * 국가 > 대리점 > 딜러 그룹 조회
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public List<EmfMap> getDelearGrpList(EmfMap emfMap) throws Exception
    {
    	List<EmfMap> list = null;
    	list = cmmUseDAO.getDelearGrpList(emfMap);
    	return list;
    }    
    

    /**
     * 뱃지 조회
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public List<EmfMap> getBadgeList(EmfMap emfMap) throws Exception
    {
    	return cmmUseDAO.getBadgeList(emfMap);
    }

    /**
     * 뱃지 등록
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public int setBadge(EmfMap emfMap) throws Exception
    {
    	return cmmUseDAO.setBadge(emfMap);
    }
    
    /**
     * 현대 딜러 코드로 딜러 코드 조회
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
    public EmfMap getDlrCd(EmfMap emfMap) throws Exception
    {
    	return cmmUseDAO.getDlrCd(emfMap);
    }

    /**
     * 다중접속 방지를 위한 session 정보 저장
     *
     * @param emfMap
     * @return EmfMap
     * @throws Exception
     */
	public int updateSessionAdm(EmfMap emfMap) throws Exception {
		return cmmUseDAO.updateSessionAdm(emfMap);
	}
}