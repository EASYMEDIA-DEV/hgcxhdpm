package com.easymedia.service.impl;

import com.easymedia.dto.EmfMap;
import com.easymedia.dto.login.LoginUser;
import com.easymedia.service.AuthChecker;
import com.easymedia.service.EgovFileMngService;
import com.easymedia.service.dao.COFileManageDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * @Class Name : EgovFileMngServiceImpl.java
 * @Description : 파일정보의 관리를 위한 구현 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 25.     이삼섭    최초생성
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 25.
 * @version
 * @see
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EgovFileMngServiceImpl implements EgovFileMngService
{
    private final COFileManageDAO fileMngDAO;

    /**
	 * 파일 목록을 조회한다.
	 * 
	 * @param emfMap
	 * @return List<EmfMap> 조회조건에 검색된 데이터
	 * @throws Exception
	 */
    public List<EmfMap> selectFileInfs(EmfMap emfMap) throws Exception
    {
		return fileMngDAO.selectFileInfs(emfMap);	
    }
    
    /**
	 * 파일 상세를 조회한다.
	 * 
	 * @param emfMap
	 * @return EmfMap 조회조건에 검색된 데이터
	 * @throws Exception
	 */
    public EmfMap selectFileInf(EmfMap emfMap) throws Exception
    {
    	return fileMngDAO.selectFileInf(emfMap);
    }
    
    /**
     * 하나의 파일에 대한 정보(속성 및 상세)를 등록한다.
     * 
     * @param emfMap
	 * @return 
	 * @throws Exception
     */
    public void insertFileInf(EmfMap emfMap) throws Exception
    {
		LoginUser lgnMap = AuthChecker.getLoginUser();
    	
    	emfMap.put("regId", lgnMap.getId());
		emfMap.put("regIp", lgnMap.getLoginIp());
		emfMap.put("modId", lgnMap.getId());
		emfMap.put("modIp", lgnMap.getLoginIp());
    	
    	//파일 마스터를 등록한다.
    	fileMngDAO.insertFileMaster(emfMap);
    	
    	//파일 상세를 등록한다.
    	fileMngDAO.insertFileDetail(emfMap);
    }

    /**
     * 여러 개의 파일에 대한 정보(속성 및 상세)를 등록한다.
     * 
     * @param fvoList
	 * @return String 조회조건에 검색된 데이터
	 * @throws Exception
     */
    public String insertFileInfs(List<EmfMap> fvoList) throws Exception 
    {
    	String atchFileId = null;
    	if (fvoList.size() > 0)
    	{
    		EmfMap emfMap = (EmfMap) fvoList.get(0);

    		if (!"".equals(emfMap.getString("atchFileId")))
    		{
				LoginUser lgnMap = AuthChecker.getLoginUser();

				emfMap.put("regId", lgnMap.getId());
				emfMap.put("regIp", lgnMap.getLoginIp());
				emfMap.put("modId", lgnMap.getId());
				emfMap.put("modIp", lgnMap.getLoginIp());
    			
    			int parntCnt = fileMngDAO.getParntFileCnt(emfMap.getString("atchFileId"));

    			if (parntCnt < 1)
    			{
    				fileMngDAO.insertFileMaster(emfMap);
    			}
        		
        		Iterator iter = fvoList.iterator();
        		
        		while (iter.hasNext()) 
        		{
        			emfMap = (EmfMap) iter.next();
					emfMap.put("regId", lgnMap.getId());
					emfMap.put("regIp", lgnMap.getLoginIp());
					emfMap.put("modId", lgnMap.getId());
					emfMap.put("modIp", lgnMap.getLoginIp());
        			
        			fileMngDAO.insertFileDetail(emfMap);
        		}
        		
        		atchFileId = emfMap.getString("atchFileId");
    		}
    	}
    	
    	return atchFileId;
    }
    
    /**
     * 하나의 파일에 대한 정보(속성 및 상세)를 수정한다.
     * 
     * @param emfMap
	 * @return 
	 * @throws Exception
     */
    public void updateFileInf(EmfMap emfMap) throws Exception 
    {
		LoginUser lgnMap = AuthChecker.getLoginUser();

		emfMap.put("regId", lgnMap.getId());
		emfMap.put("regIp", lgnMap.getLoginIp());
		emfMap.put("modId", lgnMap.getId());
		emfMap.put("modIp", lgnMap.getLoginIp());
    	
    	fileMngDAO.updateFileDetail(emfMap);
    }
    
    /**
     * 여러 개의 파일에 대한 정보(속성 및 상세)를 수정한다.
     * 
     * @param fvoList
	 * @return 
	 * @throws Exception
     */
    public void updateFileInfs(List<EmfMap> fvoList) throws Exception 
    {
    	//Delete & Insert
		LoginUser lgnMap = AuthChecker.getLoginUser();
    	
    	EmfMap emfMap = new EmfMap();
    	
    	Iterator iter = fvoList.iterator();
		
		while (iter.hasNext()) 
		{
			emfMap = (EmfMap) iter.next();
			emfMap.put("regId", lgnMap.getId());
			emfMap.put("regIp", lgnMap.getLoginIp());
			emfMap.put("modId", lgnMap.getId());
			emfMap.put("modIp", lgnMap.getLoginIp());
			
			//파일 상세를 삭제한다.
			fileMngDAO.deleteFileDetail(emfMap);
			
			//파일 상세를 등록한다.
			fileMngDAO.insertFileDetail(emfMap);
		}
    }
    
    /**
     * 여러 개의 파일에 대한 정보(속성 및 상세)를 수정한다.
     * 
     * @param fvoList
	 * @return 
	 * @throws Exception
     */
    public String updateFileInf(List<EmfMap> fvoList) throws Exception 
    {
    	
    	String atchFileId = "";
    	//Delete & Insert
		LoginUser lgnMap = AuthChecker.getLoginUser();
    	
    	EmfMap emfMap = new EmfMap();
    	
    	Iterator iter = fvoList.iterator();
		
		while (iter.hasNext()) 
		{
			emfMap = (EmfMap) iter.next();
			emfMap.put("regId", lgnMap.getId());
			emfMap.put("regIp", lgnMap.getLoginIp());
			emfMap.put("modId", lgnMap.getId());
			emfMap.put("modIp", lgnMap.getLoginIp());
			
			//파일 상세를 삭제한다.
			fileMngDAO.deleteFileDetail(emfMap);
			
			//파일 상세를 등록한다.
			fileMngDAO.insertFileDetail(emfMap);
			
			atchFileId = emfMap.getString("atchFileId");
		}
		
		return atchFileId;
    }
    
    /**
     * 하나의 파일을 삭제한다.
     * 
     * @param emfMap
	 * @return 
	 * @throws Exception
     */
    public void deleteFileInf(EmfMap emfMap) throws Exception 
    {
    	fileMngDAO.deleteFileDetail(emfMap);
    }
    
    /**
	 * 여러 개의 파일을 삭제한다.
	 * 
	 * @param fvoList
	 * @return 
	 * @throws Exception
	 */
    public void deleteFileInfs(List<EmfMap> fvoList) throws Exception 
    {
    	EmfMap emfMap = new EmfMap();
    	
    	Iterator iter = fvoList.iterator();
		
		while (iter.hasNext()) 
		{
			emfMap = (EmfMap)iter.next();
			
			fileMngDAO.deleteFileDetail(emfMap);
		}
    }

    /**
     * 전체 파일을 삭제한다.
     * 
     * @param emfMap
	 * @return 
	 * @throws Exception
     */
    public void deleteAllFileInf(EmfMap emfMap) throws Exception 
    {
    	fileMngDAO.deleteAllFileInf(emfMap);
    }
    
    /**
     * 파일명 검색에 대한 목록을 조회한다.
     * 
     * @param emfMap
	 * @return List<EmfMap> 조회조건에 검색된 데이터
	 * @throws Exception
     */
    public List<EmfMap> selectFileListByFileNm(EmfMap emfMap) throws Exception 
    {
		return fileMngDAO.selectFileListByFileNm(emfMap);		
    }

    /**
     * 이미지 파일에 대한 목록을 조회한다.
     * 
     * @param emfMap
	 * @return List<EmfMap> 조회조건에 검색된 데이터
	 * @throws Exception
     */
    public List<EmfMap> selectImageFileList(EmfMap emfMap) throws Exception
    {
    	return fileMngDAO.selectImageFileList(emfMap);
    }
    
    /**
     * 파일ID에 대한 최대 파일순번을 가져온다.
     * 
     * @param atchFileId
	 * @return int 조회조건에 검색된 데이터
	 * @throws Exception
     */
    public int getMaxFileSeq(String atchFileId) throws Exception 
    {
    	return fileMngDAO.getMaxFileSeq(atchFileId);
    }
    
    /**
     * 파일ID에 대한 파일갯수를 가져온다.
     * 
     * @param val
	 * @return int 조회조건에 검색된 데이터
	 * @throws Exception
     */
    public int getFileListCnt(String val) throws Exception 
    {
    	return fileMngDAO.getFileListCnt(val);
    }
}