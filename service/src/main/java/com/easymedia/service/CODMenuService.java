package com.easymedia.service;

import com.easymedia.dto.EmfMap;
import org.json.simple.JSONArray;

import java.util.List;

/**
 * <pre>
 * 메뉴 관리를 위한 Service
 * </pre>
 *
 * @ClassName		: CODMenuService.java
 * @Description		: 메뉴 관리를 위한 Service
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
public interface CODMenuService {

	/**
	 * 메뉴 목록을 조회한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> selectMenuList(EmfMap emfMap) throws Exception;

	/**
	 * 메뉴의 상세정보를 조회한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public EmfMap selectMenuDtl(EmfMap emfMap) throws Exception;

	/**
	 * 메뉴를 등록한다.
	*
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int insertMenu(EmfMap emfMap) throws Exception;

	/**
	 * 메뉴명을 수정한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int updateMenuNm(EmfMap emfMap) throws Exception;

	/**
	 * 메뉴정보를 수정한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int updateMenuInf(EmfMap emfMap) throws Exception;

	/**
	 * 사용자 노출 여부를 수정한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int updateUserUseYn(EmfMap emfMap) throws Exception;

	/**
	 * 메뉴를 이동한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int updateMenuPstn(EmfMap emfMap) throws Exception;

	/**
	 * 메뉴를 삭제한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int deleteMenu(EmfMap emfMap) throws Exception;

	/**
	 * 상위부모를 다 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getParntData(EmfMap emfMap) throws Exception;

	/**
	 * 하위노드를 다 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getChildData(EmfMap emfMap) throws Exception;

	/**
	 * 메뉴 목록 일괄처리를 위한 재귀함수
	 *
	 * @param jsonList
	 * @return
	 * @throws Exception
	 */
	public JSONArray getJsonData(List<EmfMap> jsonList, int startNum, int paramSeq) throws Exception;
}