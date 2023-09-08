package com.easymedia.service.dao;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre>
 * 메뉴 관리를 위한 DAO
 * </pre>
 *
 * @ClassName		: CODMenuDAO.java
 * @Description		: 메뉴 관리를 위한 DAO
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
@Mapper
public interface CODMenuDAO {

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
	 * 해당 메뉴의 오른쪽 값을 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int getRhtVal(EmfMap emfMap) throws Exception;

	/**
	 * 해당 메뉴의 깊이를 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int getDpth(EmfMap emfMap) throws Exception;

	/**
	 * 해당 메뉴의 왼쪽 값을 지정한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public void setLftVal(EmfMap emfMap) throws Exception;

	/**
	 * 해당 메뉴의 오른쪽 값을 지정한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public void setRhtVal(EmfMap emfMap) throws Exception;

	/**
	 * 최상위 position을 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public Integer getMaxPosition(EmfMap emfMap) throws Exception;

	/**
	 * 하위 트리들을 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getMoveNodeIds(EmfMap emfMap) throws Exception;

	/**
	 * 하위트리갯수를 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public Integer getRefInd(EmfMap emfMap) throws Exception;

	/**
	 * 메뉴 자식의 갯수를 가져온다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public Integer getMoveExits(EmfMap emfMap) throws Exception;

	/**
	 * 움직일 노드 갯수 확인한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public Integer getMoveExits2(EmfMap emfMap) throws Exception;

	/**
	 * 메뉴를 이동한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int setMenuMove(String sql) throws Exception;

	/**
	 * 메뉴를 삭제한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int deleteMenu(EmfMap emfMap) throws Exception;

	/**
	 * 메뉴의 하위노드 왼쪽키값 변경
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public void setDeleteUpdateLftVal(EmfMap emfMap) throws Exception;

	/**
	 * 메뉴의 하위노드 오른쪽키값 변경
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public void setDeleteUpdateRhtVal(EmfMap emfMap) throws Exception;

	/**
	 * 메뉴의 하위노드 위치 변경
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public void setDeleteUpdatePstn(EmfMap emfMap) throws Exception;

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
	 * 부모키 기준으로 첫번째 하위노드를 조회한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getChildNodeData(EmfMap emfMap) throws Exception;


	/**
	 * 다국어 메뉴명 조회
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public List<EmfMap> getLgugMenuList(EmfMap emfMap) throws Exception;
	/**
	 * 다국어 메뉴를 등록한다.
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
	public int insertLgugMenuNm(EmfMap emfMap) throws Exception;
}