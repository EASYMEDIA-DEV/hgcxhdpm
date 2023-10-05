package com.easymedia.service.impl;

import com.easymedia.dto.EmfMap;
import com.easymedia.service.CODMenuService;
import com.easymedia.service.EgovCmmUseService;
import com.easymedia.service.EgovUserDetailsHelper;
import com.easymedia.service.dao.CODMenuDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.idgnr.impl.EgovTableIdGnrServiceImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 * 메뉴 관리를 위한 ServiceImpl
 * </pre>
 *
 * @ClassName		: CODMenuServiceImpl.java
 * @Description		: 메뉴 관리를 위한 ServiceImpl
 * @author 김대환
 * @since 2019.01.15
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 *	     since		  author	            description
 *    ==========    ==========    ==============================
 * 	  2019.01.15	  김대환	             최초 생성
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CODMenuServiceImpl implements CODMenuService {
	/** DAO **/
	private final CODMenuDAO cODMenuDAO;
	/** ID **/
	private final EgovTableIdGnrServiceImpl menuIdgen;
	/** 코드 조회 **/
    private final EgovCmmUseService cmmUseService;

	/**
	 * 메뉴 목록을 조회한다.
	 *
	 * @param emfMap
	 * @return List<EmfMap>
	 * @throws Exception
	 */
	public List<EmfMap> selectMenuList(EmfMap emfMap) throws Exception
	{
		return cODMenuDAO.selectMenuList(emfMap);
	}

	/**
	 * 메뉴의 상세정보를 조회한다.
	 *
	 * @param emfMap
	 * @return List<EmfMap>
	 * @throws Exception
	 */
	public EmfMap selectMenuDtl(EmfMap emfMap) throws Exception
	{

		if ( !"".equals(emfMap.getString("menuSeq")) )
    	{
			EmfMap menuInfo = cODMenuDAO.selectMenuDtl(emfMap);

			if ( menuInfo != null )
			{
				emfMap.put("menuInfo", menuInfo);
			}

			//2019-07-10 다국어 메뉴명 조회
			List<EmfMap> lgugMenuNmList = cODMenuDAO.getLgugMenuList(emfMap);
			emfMap.put("lgugMenuNmList", lgugMenuNmList);
    	}

		return emfMap;
	}

	/**
	 * 메뉴를 등록한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap insertMenu(EmfMap emfMap) throws Exception
	{
		EmfMap lgnMap = (EmfMap) EgovUserDetailsHelper.getAuthenticatedUser();

		int menuSeq = menuIdgen.getNextIntegerId();
		int pstn = cODMenuDAO.getParentMenuMaxPstn(emfMap);

		emfMap.put("menuSeq", menuSeq);
		emfMap.put("pstn", pstn+1);
		emfMap.put("rhtVal", cODMenuDAO.getRhtVal(emfMap));
		emfMap.put("dpth", cODMenuDAO.getDpth(emfMap));

		cODMenuDAO.setLftVal(emfMap);
		cODMenuDAO.setRhtVal(emfMap);

		emfMap.put("regId", lgnMap.getString("id"));
		emfMap.put("regIp", lgnMap.getString("loginIp"));
		emfMap.put("modId", lgnMap.getString("id"));
		emfMap.put("modIp", lgnMap.getString("loginIp"));

		//공통코드 배열 셋팅
		cODMenuDAO.insertMenu(emfMap);
		return emfMap;
	}

	/**
	 * 메뉴명을 수정한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int updateMenuNm(EmfMap emfMap) throws Exception
	{
		EmfMap lgnMap = (EmfMap) EgovUserDetailsHelper.getAuthenticatedUser();

		emfMap.put("modId", lgnMap.getString("id"));
		emfMap.put("modIp", lgnMap.getString("loginIp"));

		return cODMenuDAO.updateMenuNm(emfMap);
	}

	/**
	 * 메뉴정보를 수정한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int updateMenuInf(EmfMap emfMap) throws Exception
	{
		EmfMap lgnMap = (EmfMap) EgovUserDetailsHelper.getAuthenticatedUser();

		emfMap.put("modId", lgnMap.getString("id"));
		emfMap.put("modIp", lgnMap.getString("loginIp"));

		//공통코드 배열 셋팅
		Set<String> keyList = emfMap.keySet();
		ArrayList<String> cdDtlList = new ArrayList<String>();
		cdDtlList.add("LGUG_CD");	//언어코드
		//정의된 코드id값들의 상세 코드 맵 반환
		List<EmfMap> lgugList = (List<EmfMap>)cmmUseService.getCmmCodeBindAll(cdDtlList).get("lgugCd");
		if(lgugList != null && lgugList.size() > 0)
		{
			String cd = "";
			for(int q = 0  ; q < lgugList.size() ; q++)
			{
				cd = lgugList.get(q).getString("cd");
				if(keyList.contains(cd + "MenuNm"))
				{
					emfMap.put("lgugCd", cd);
					emfMap.put("lgugMenuNm", emfMap.getString(cd + "MenuNm"));
					cODMenuDAO.insertLgugMenuNm(emfMap);
				}
			}
		}

		return cODMenuDAO.updateMenuInf(emfMap);
	}

	/**
	 * 게시판 카테고리의 노출 정보를 수정한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int updateUserUseYn(EmfMap emfMap) throws Exception
	{
		EmfMap lgnMap = (EmfMap) EgovUserDetailsHelper.getAuthenticatedUser();

		emfMap.put("modId", lgnMap.getString("id"));
		emfMap.put("modIp", lgnMap.getString("loginIp"));

		return cODMenuDAO.updateUserUseYn(emfMap);
	}

	/**
	 * 메뉴를 이동한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int updateMenuPstn(EmfMap emfMap) throws Exception
	{
		String sqlStr1 = "";
		String sqlStr2 = "";
		String sqlStr3 = "";
		String sqlStr4 = "";
		String sqlStr5 = "";
		String sqlStr6 = "";
		String sqlStr7 = "";
		String sqlStr8 = "";
		String sqlStr9 = "";
		String sqlStr10 = "";
		String sqlStr11 = "";
		String node_ids = "";

		int node_parntSeq = 0;
		int node_pstn = 0;
		int node_lftVal = 0;
		int node_rhtVal = 0;
		int node_dpth = 0;

		int refNode_parntSeq = 0;
		int refNode_pstn = 0;
		int refNode_lftVal = 0;
		int refNode_rhtVal = 0;
		int refNode_dpth = 0;

		int ndif = 2;
		int ref_ind = 0;
		int self = 0;
		int ldif = 0;
		int idif = 0;

		int menuSeq = Integer.parseInt(emfMap.getString("menuSeq"));
		int pstn = Integer.parseInt(emfMap.getString("pstn"));
		int refSeq = Integer.parseInt(emfMap.getString("refSeq"));
		int isCopy = Integer.parseInt(emfMap.getString("isCopy"));
		int resultCnt = 0;

		//현재 트리
		EmfMap currMenuMap = cODMenuDAO.selectMenuDtl(emfMap);

		//이동할 트리
		EmfMap tmpMap = new EmfMap();
		tmpMap.put("menuSeq", refSeq);

		EmfMap refMap = cODMenuDAO.selectMenuDtl(tmpMap);

		node_parntSeq = Integer.parseInt(currMenuMap.getString("parntSeq"));
		node_pstn = Integer.parseInt(currMenuMap.getString("pstn"));
		node_lftVal = Integer.parseInt(currMenuMap.getString("lftVal"));
		node_rhtVal = Integer.parseInt(currMenuMap.getString("rhtVal"));
		node_dpth = Integer.parseInt(currMenuMap.getString("dpth"));

		refNode_parntSeq = Integer.parseInt(refMap.getString("parntSeq"));
		refNode_pstn = Integer.parseInt(refMap.getString("pstn"));
		refNode_lftVal = Integer.parseInt(refMap.getString("lftVal"));
		refNode_rhtVal = Integer.parseInt(refMap.getString("rhtVal"));
		refNode_dpth = Integer.parseInt(refMap.getString("dpth"));

		if ( node_lftVal > 0 )
		{
			EmfMap exitMap = new EmfMap();

			exitMap.put("lftVal", node_lftVal);
			exitMap.put("rhtVal", node_rhtVal);
			exitMap.put("refSeq", refSeq);

			Integer exitMenuSeq = cODMenuDAO.getMoveExits(exitMap);

			if ( exitMenuSeq != null )
			{
				return 0;
			}

			EmfMap nodeIdsMap = new EmfMap();

			nodeIdsMap.put("lftVal", node_lftVal);
			nodeIdsMap.put("rhtVal", node_rhtVal);

			List<EmfMap> moveNodeIds = cODMenuDAO.getMoveNodeIds(nodeIdsMap);

			for ( int q = 0; q < moveNodeIds.size(); q++ )
			{
				EmfMap moveNodeIdMap = moveNodeIds.get(q);

				if ( "".equals(node_ids) )
				{
					node_ids = moveNodeIdMap.getString("menuSeq");
				}
				else
				{
					node_ids = node_ids + "," + moveNodeIdMap.getString("menuSeq");
				}
			}

			ndif = node_rhtVal - node_lftVal + 1;
		}

		if ( "".equals(node_ids) )
		{
			node_ids = "0";
		}

		EmfMap maxMap = new EmfMap();

		maxMap.put("refSeq", refSeq);

		int maxPosition = cODMenuDAO.getMaxPosition(maxMap);

		if ( pstn >= maxPosition )
		{
			pstn = maxPosition;
		}

		if ( node_lftVal > 0 && isCopy == 0 )
		{
			sqlStr1 = "UPDATE CO_MENU_MST SET PSTN = PSTN - 1 WHERE PARNT_SEQ = " + node_parntSeq + " AND PSTN  > " + node_pstn;
			sqlStr2 = "UPDATE CO_MENU_MST SET LFT_VAL = LFT_VAL - " + ndif + " WHERE LFT_VAL > " + node_rhtVal;
			sqlStr3 = "UPDATE CO_MENU_MST SET RHT_VAL = RHT_VAL - " + ndif + " WHERE RHT_VAL > " + node_lftVal +" AND MENU_SEQ NOT IN (" + node_ids + ")";
		}

		if ( isCopy == 0 )
		{
			sqlStr4 = "UPDATE CO_MENU_MST SET PSTN = PSTN + 1 WHERE PARNT_SEQ = " + refSeq + " AND PSTN >= " + pstn + " AND MENU_SEQ NOT IN (" + node_ids + ")";
		}
		else
		{
			sqlStr5 = "UPDATE CO_MENU_MST SET PSTN = PSTN + 1 WHERE PARNT_SEQ = " + refSeq + " AND PSTN >= " + pstn;
		}

		if ( refSeq != 0 )
		{
			ref_ind = refNode_rhtVal;
		}

		if ( ref_ind < 1 )
		{
			ref_ind = 1;
		}

		if ( node_lftVal > 0 && isCopy == 0 && node_parntSeq == refSeq && pstn > node_pstn )
		{
			self = 1;
		}

		EmfMap moveExitsMap = new EmfMap();

		moveExitsMap.put("self", self);
		moveExitsMap.put("pstn", pstn);
		moveExitsMap.put("refSeq", refSeq);

		Integer moveExits2 = cODMenuDAO.getMoveExits2(moveExitsMap);

		if ( moveExits2 != 0 )
		{
			ref_ind = cODMenuDAO.getRefInd(moveExitsMap);
		}

		if ( node_lftVal > 0 && isCopy == 0 && node_lftVal < ref_ind )
		{
			ref_ind = ref_ind - ndif;
		}

		if ( isCopy == 0 )
		{
			sqlStr6 = "UPDATE CO_MENU_MST SET LFT_VAL = LFT_VAL + " + ndif + " WHERE LFT_VAL >= " + ref_ind + " AND MENU_SEQ NOT IN (" + node_ids + ")";
			sqlStr7 = "UPDATE CO_MENU_MST SET RHT_VAL = RHT_VAL + " + ndif + " WHERE RHT_VAL >= " + ref_ind + " AND MENU_SEQ NOT IN (" + node_ids + ")";
		}
		else
		{
			sqlStr8 = "UPDATE CO_MENU_MST SET LFT_VAL = LFT_VAL + " + ndif + " WHERE LFT_VAL >= " + ref_ind;
			sqlStr9 = "UPDATE CO_MENU_MST SET RHT_VAL = RHT_VAL + " + ndif + " WHERE RHT_VAL >= " + ref_ind;
		}

		if ( refSeq != 0 )
		{
			ldif = refNode_dpth + 1;
		}

		idif = ref_ind;

		if ( node_lftVal > 0 )
		{
			ldif = node_dpth - (refNode_dpth + 1);
			idif = node_lftVal - ref_ind;

			if(isCopy == 0)
			{
				sqlStr10 = "UPDATE CO_MENU_MST SET PARNT_SEQ = " + refSeq + ", PSTN = " + pstn + " WHERE MENU_SEQ = " + menuSeq;
				sqlStr11 = "UPDATE CO_MENU_MST SET LFT_VAL = LFT_VAL - " + idif + ", RHT_VAL = RHT_VAL - " + idif + ", DPTH = DPTH - " + ldif + " WHERE MENU_SEQ IN (" + node_ids + ")";
			}
		}

		if ( !"".equals(sqlStr1) )
		{
		    EmfMap param1 = new EmfMap();

		    param1.put("node_parntSeq", node_parntSeq);
		    param1.put("node_pstn", node_pstn);

		    resultCnt = cODMenuDAO.setMenuMove(sqlStr1);
		}

		if ( !"".equals(sqlStr2) )
		{
			EmfMap param2 = new EmfMap();

			param2.put("ndif", ndif);
			param2.put("node_rhtVal", node_rhtVal);

			resultCnt = cODMenuDAO.setMenuMove(sqlStr2);
		}

		if ( !"".equals(sqlStr3) )
		{
			EmfMap param3 = new EmfMap();

			param3.put("ndif", ndif);
			param3.put("node_lftVal", node_lftVal);
			param3.put("node_ids", node_ids);

			resultCnt = cODMenuDAO.setMenuMove(sqlStr3);
		}

		if ( !"".equals(sqlStr4) )
		{
			EmfMap param4 = new EmfMap();

			param4.put("refSeq", refSeq);
			param4.put("pstn", pstn);
			param4.put("node_ids", node_ids);

			resultCnt = cODMenuDAO.setMenuMove(sqlStr4);
		}

		if ( !"".equals(sqlStr5) )
		{
			EmfMap param5 = new EmfMap();
			param5.put("refSeq", refSeq);
			param5.put("pstn", pstn);

			resultCnt = cODMenuDAO.setMenuMove(sqlStr5);
		}

		if ( !"".equals(sqlStr6) )
		{
			EmfMap param6 = new EmfMap();

			param6.put("ndif", ndif);
			param6.put("ref_ind", ref_ind);
			param6.put("node_ids", node_ids);

			resultCnt = cODMenuDAO.setMenuMove(sqlStr6);
		}

		if ( !"".equals(sqlStr7) )
		{
			EmfMap param7 = new EmfMap();

			param7.put("ndif", ndif);
			param7.put("ref_ind", ref_ind);
			param7.put("node_ids", node_ids);

			resultCnt = cODMenuDAO.setMenuMove(sqlStr7);
		}

		if ( !"".equals(sqlStr8) )
		{
		   	EmfMap param8 = new EmfMap();

		   	param8.put("ndif", ndif);
		   	param8.put("ref_ind", ref_ind);

		   	resultCnt = cODMenuDAO.setMenuMove(sqlStr8);
		}

		if ( !"".equals(sqlStr9) )
		{
		   	EmfMap param9 = new EmfMap();

		   	param9.put("ndif", ndif);
		   	param9.put("ref_ind", ref_ind);
		   	param9.put("node_ids", node_ids);

		   	resultCnt = cODMenuDAO.setMenuMove(sqlStr9);
		}

		if ( !"".equals(sqlStr10) )
		{
		   	EmfMap param10 = new EmfMap();

		   	param10.put("refSeq", refSeq);
		   	param10.put("pstn", pstn);
		   	param10.put("menuSeq", menuSeq);

		   	resultCnt = cODMenuDAO.setMenuMove(sqlStr10);
		}

		if ( !"".equals(sqlStr11) )
		{
			EmfMap param11 = new EmfMap();

			param11.put("idif", idif);
			param11.put("ldif", ldif);
			param11.put("node_ids", node_ids);

			resultCnt = cODMenuDAO.setMenuMove(sqlStr11);
		}

		return resultCnt;
	}

	/**
	 * 메뉴를 삭제한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int deleteMenu(EmfMap emfMap) throws Exception
	{
		EmfMap menuDtlMap = cODMenuDAO.selectMenuDtl(emfMap);
		int resultCnt = 0;

		if ( menuDtlMap != null )
		{
			resultCnt = cODMenuDAO.deleteMenu(menuDtlMap);
			cODMenuDAO.setDeleteUpdateLftVal(menuDtlMap);
			cODMenuDAO.setDeleteUpdateRhtVal(menuDtlMap);
			cODMenuDAO.setDeleteUpdatePstn(menuDtlMap);
		}

		return resultCnt;
	}

	/**
	 * 상위부모를 다 가져온다.
	 *
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getParntData(EmfMap emfMap) throws Exception
	{
		return cODMenuDAO.getParntData(emfMap);
	}

	/**
	 * 하위노드를 다 가져온다.
	 *
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getChildData(EmfMap emfMap) throws Exception
	{
		return cODMenuDAO.getChildData(emfMap);
	}

	/**
	 * 메뉴 목록 일괄처리를 위한 재귀함수
	 *
	 * @return
	 * @throws Exception
	 */
	public JSONArray getJsonData(List<EmfMap> jsonList, int startNum, int paramSeq) throws Exception
	{
		JSONArray tmpArray = new JSONArray();

		EmfMap jsonMap = null;

		int i = 0, parntSeq = 0, seq = 0;

		String checktype = "", checkrole = "";

		for (i = startNum; i < jsonList.size(); i++)
		{
			jsonMap = jsonList.get(i);
			parntSeq = Integer.parseInt(jsonMap.getString("parntSeq"));
			seq = Integer.parseInt(jsonMap.getString("menuSeq"));

			if (paramSeq == parntSeq)
			{
				JSONObject tmpObject = new JSONObject();

				tmpObject.put("text", jsonMap.getString("menuNm"));
				tmpObject.put("key", seq);
				tmpObject.put("i", seq);
				tmpObject.put("id", "node_" + seq);
				JSONObject stateObject = new JSONObject();
				tmpObject.put("state", stateObject);
				if (Integer.parseInt(String.valueOf(jsonMap.get("childcnt"))) > 0)
				{
					stateObject.put("opened", true);
					i = i + 1;

					JSONArray rtnArray = getJsonData(jsonList, i, seq);

					tmpObject.put("children", rtnArray);

					i = Integer.parseInt(((JSONObject) rtnArray.get(rtnArray.size() - 1)).get("i").toString());

					rtnArray = null;
				}

				JSONObject jsonAttr = new JSONObject();

		        jsonAttr.put("id", "node_" + jsonMap.get("menuSeq"));
		        jsonAttr.put("rel", jsonMap.getString("menuGb"));
		        jsonAttr.put("parent_treeid", jsonMap.get("parntSeq"));
		        jsonAttr.put("level", jsonMap.getString("dpth"));
		        jsonAttr.put("status", jsonMap.getString("userUseYn"));
		        jsonAttr.put("link", jsonMap.getString("userLink"));
		        jsonAttr.put("treeid", jsonMap.get("menuSeq"));

		        if (jsonMap.containsKey("checktype"))
		        {
		        	checktype = jsonMap.getString("checktype");

		        	jsonAttr.put("checktype", checktype);

		        	if ("".equals(checktype))
	                {
						stateObject.put("selected", false);
	                	jsonAttr.put("class", "jstree-unchecked");
	                }
	                else
	                {
						stateObject.put("selected", true);
	                	jsonAttr.put("class", "jstree-checked");
	                }
		        }

		        if (jsonMap.containsKey("checkrole"))
		        {
		        	checkrole = jsonMap.getString("checkrole");

		        	if (!"".equals(checkrole))
		        	{
		        		jsonAttr.put("checkrole", checkrole);
		        	}
		        }

		        tmpObject.put("attr", jsonAttr);
		        tmpArray.add(tmpObject);

		        tmpObject = null;
		        jsonAttr = null;
			}
		}

		return tmpArray;
	}
}