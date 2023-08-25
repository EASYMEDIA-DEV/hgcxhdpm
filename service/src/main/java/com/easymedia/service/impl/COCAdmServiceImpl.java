package com.easymedia.service.impl;

import com.easymedia.dto.EmfMap;
import com.easymedia.error.hotel.utility.sim.SeedCipher;
import com.easymedia.service.COCAdmService;
import com.easymedia.service.EgovUserDetailsHelper;
import com.easymedia.service.dao.COBLgnDAO;
import com.easymedia.service.dao.COCAdmDAO;
import com.easymedia.utility.COJsonUtil;
import com.easymedia.utility.COPaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 * 관리자 계정 관리 ServiceImpl
 * </pre>
 *
 * @ClassName		: COCAdmServiceImpl.java
 * @Description		: 관리자 계정 관리 ServiceImpl
 * @author 허진영
 * @since 2019.01.14
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.01.14		허진영					최초 생성
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class COCAdmServiceImpl implements COCAdmService {

	/** 관리자 **/
    private final COCAdmDAO cOCAdmDAO;

	/** 로그인 **/
	private final COBLgnDAO cOBLgnDAO;

	/** 관리자 ID **/
	private final EgovIdGnrService admIdgen;

	@Value("${globals.charcode}")
	private String ENCODE;

	/**
	 * 관리자 계정 관리 List
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
    public EmfMap selectAdmList(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();

    	PaginationInfo paginationInfo = new COPaginationUtil().getPage(emfMap);

		// 리스트 가져오기
		List<EmfMap> list = cOCAdmDAO.selectAdmList(emfMap);

		// 총 건수 가져오기
		int totCnt = cOCAdmDAO.selectAdmListTotCnt(emfMap);

		paginationInfo.setTotalRecordCount(totCnt);

		rtnMap.put("totCnt", totCnt);
		rtnMap.put("pageIndex", paginationInfo.getCurrentPageNo());
		rtnMap.put("recordPerPage", paginationInfo.getRecordCountPerPage());
		rtnMap.put("totPage", paginationInfo.getTotalPageCount());
		rtnMap.put("list", COJsonUtil.getJsonArrayFromList(list));

    	return rtnMap;
    }

    /**
     * 관리자 강제 변경을 위한 조회
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap getAdmAllList(EmfMap emfMap) throws Exception
    {
		// 리스트 가져오기
		List<EmfMap> list = cOCAdmDAO.getAdmAllList(emfMap);
		emfMap.put("list", COJsonUtil.getJsonArrayFromList(list));

    	return emfMap;
    }

    /**
	 * 관리자 계정 관리 Details
	 *
	 * @param emfMap
	 * @return
	 * @throws Exception
	 */
    public EmfMap selectAdmDtl(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();

    	if (!"".equals(emfMap.getString("detailsKey")))
    	{
    		EmfMap info = cOCAdmDAO.selectAdmDtl(emfMap);

	    	if (info != null)
	    	{
	    		/*
	    		// 연락처
	    		String tel = info.getString("tel");
	    		if (!"".equals(tel) && tel.indexOf("0") != 0)
	    		{
	    			info.put("tel", SeedCipher.decrypt(tel, ENCODE));
	    		}
				*/
	    		rtnMap.put("info", COJsonUtil.getJsonStringFromMap(info));
	    	}
    	}

    	return rtnMap;
    }

	/**
     * 관리자 계정 관리 Insert
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public int insertAdm(EmfMap emfMap) throws Exception
	{
		EmfMap lgnMap = EgovUserDetailsHelper.getAuthenticatedUser();

		// 패스워드
		String password = emfMap.getString("pwd");

		if (!"".equals(password) && password.length() >= 6)
		{
			emfMap.put("pwd", SeedCipher.oneencrypt(password));
		}

		/*
		// 전화번호
		String tel = emfMap.getString("tel");

		if (!"".equals(tel))
		{
    		emfMap.put("tel", SeedCipher.encrypt(tel, ENCODE));
		}
		*/
		emfMap.put("detailsKey", admIdgen.getNextIntegerId());
		emfMap.put("regId", lgnMap.get("id"));
		emfMap.put("regIp", lgnMap.get("loginIp"));
		emfMap.put("modId", lgnMap.get("id"));
		emfMap.put("modIp", lgnMap.get("loginIp"));

		setAdmMenu(emfMap);

		return cOCAdmDAO.insertAdm(emfMap);
    }

	/**
     * 관리자를 수정한다.
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public int updateAdm(EmfMap emfMap) throws Exception
	{
		EmfMap lgnMap = (EmfMap) EgovUserDetailsHelper.getAuthenticatedUser();

		// 비밀번호
		String password = emfMap.getString("pwd");

		if (!"".equals(password))
		{
			emfMap.put("pwd", SeedCipher.oneencrypt(password));
		}

		/*
		// 전화번호
		String tel = emfMap.getString("tel");

		if (!"".equals(tel))
		{
    		emfMap.put("tel", SeedCipher.encrypt(tel, ENCODE));
		}
		*/
		emfMap.put("delKeyList", emfMap.getList("detailsKey"));
		emfMap.put("regId", lgnMap.getString("id"));
		emfMap.put("regIp", lgnMap.getString("loginIp"));
		emfMap.put("modId", lgnMap.getString("id"));
		emfMap.put("modIp", lgnMap.getString("loginIp"));

    	setAdmMenu(emfMap);

    	return cOCAdmDAO.updateAdm(emfMap);
	}

	/**
     * 관리자를 수정한다. (내정보 변경)
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public int updatePrsnData(EmfMap emfMap) throws Exception
	{
		EmfMap lgnMap = (EmfMap) EgovUserDetailsHelper.getAuthenticatedUser();

		// 순번
		emfMap.put("detailsKey", lgnMap.getString("admSeq"));

		// 비밀번호
		String password = emfMap.getString("pwd");
		if (!"".equals(password) && password.length() >= 6)
		{
			emfMap.put("pwd", SeedCipher.oneencrypt(password));
		}

		emfMap.put("modId", lgnMap.getString("id"));
		emfMap.put("modIp", lgnMap.getString("loginIp"));

		return cOCAdmDAO.updateAdm(emfMap);
	}

	/**
     * 관리자 계정 관리 Delete
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public int deleteAdm(EmfMap emfMap) throws Exception
	{
		int actCnt = 0;

		List<String> delKeyList = emfMap.getList("detailsKey");

		if (delKeyList.size() > 0)
		{
			emfMap.put("delKeyList", emfMap.getList("detailsKey"));

			cOCAdmDAO.deleteAdmMenu(emfMap);

			actCnt = cOCAdmDAO.deleteAdm(emfMap);
		}

    	return actCnt;
	}


	/**
     * 관리자 계정 중복 Check
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public int getIdCheck(EmfMap emfMap) throws Exception
	{
		return cOCAdmDAO.getIdCheck(emfMap);
	}

    /**
     * 관리자 메뉴정보를 Setting한다.
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public void setAdmMenu(EmfMap emfMap) throws Exception
    {
    	// 관리자 계정 메뉴 Delete
    	if (emfMap.containsKey("delKeyList"))
    	{
    		cOCAdmDAO.deleteAdmMenu(emfMap);
    	}

    	// 관리자 계정 메뉴 Insert
    	String mChecked = emfMap.getString("mChecked");

    	if (!"".equals(mChecked))
		{
			String[] menuSeqs;

			if (mChecked.indexOf(",") > 0)
			{
				menuSeqs = mChecked.split(",");
			}
			else
			{
				menuSeqs = new String[]{mChecked};
			}

			if (!"0".equals(emfMap.getString("authCd")))
			{
				if (menuSeqs != null)
				{
					for (int q = 0; q < menuSeqs.length; q++)
					{
						emfMap.put("menuSeq", Integer.parseInt(menuSeqs[q]));

						cOCAdmDAO.insertAdmMenu(emfMap);
					}
				}
			}
		}
    }
}