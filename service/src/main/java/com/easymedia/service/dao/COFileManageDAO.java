package com.easymedia.service.dao;

import com.easymedia.dto.EmfMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Class Name : EgovFileMngDAO.java
 * @Description : 파일정보 관리를 위한 데이터 처리 클래스
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
@Mapper
public interface COFileManageDAO 
{
	/**
	 * 파일 목록을 조회한다.
	 * 
	 * @param emfMap
	 * @return List<EmfMap> 조회조건에 검색된 데이터
	 * @throws Exception
	 */
    public List<EmfMap> selectFileInfs(EmfMap emfMap) throws Exception;
    
    /**
	 * 파일 상세를 조회한다.
	 * 
	 * @param emfMap
	 * @return EmfMap 조회조건에 검색된 데이터
	 * @throws Exception
	 */
    public EmfMap selectFileInf(EmfMap emfMap) throws Exception;
    
    /**
     * 파일 마스터를 등록한다.
     * 
     * @param emfMap
	 * @return 
	 * @throws Exception
     */
    public int insertFileMaster(EmfMap emfMap) throws Exception ;
    
    /**
     * 파일 상세를 등록한다.
     * 
     * @param emfMap
	 * @return 
	 * @throws Exception
     */
    public int insertFileDetail(EmfMap emfMap) throws Exception;
    
    /**
     * 파일 상세를 수정한다.
     * 
     * @param emfMap
	 * @return 
	 * @throws Exception
     */
    public int updateFileDetail(EmfMap emfMap) throws Exception;

    /**
     * 파일 상세를 삭제한다.
     * 
     * @param emfMap
	 * @return 
	 * @throws Exception
     */
    public void deleteFileDetail(EmfMap emfMap) throws Exception;

    /**
     * 전체 파일을 삭제한다.
     * 
     * @param emfMap
	 * @return 
	 * @throws Exception
     */
    public void deleteAllFileInf(EmfMap emfMap) throws Exception;

    /**
     * 파일명 검색에 대한 목록을 조회한다.
     * 
     * @param emfMap
	 * @return List<EmfMap> 조회조건에 검색된 데이터
	 * @throws Exception
     */
    public List<EmfMap> selectFileListByFileNm(EmfMap emfMap) throws Exception;
    
    /**
     * 이미지 파일에 대한 목록을 조회한다.
     * 
     * @param emfMap
	 * @return List<EmfMap> 조회조건에 검색된 데이터
	 * @throws Exception
     */
    public List<EmfMap> selectImageFileList(EmfMap emfMap) throws Exception;
    
    /**
     * 파일 마스터에 데이터가 있는지 확인한다.
     * 
     * @param atchFileId
	 * @return int 조회조건에 검색된 데이터
	 * @throws Exception
     */
    public int getParntFileCnt(String atchFileId) throws Exception;
    
    /**
     * 파일ID에 대한 최대 파일순번을 가져온다.
     * 
     * @param atchFileId
	 * @return int 조회조건에 검색된 데이터
	 * @throws Exception
     */
    public int getMaxFileSeq(String atchFileId) throws Exception;
    
    /**
     * 파일의 갯수를 가져온다.
     * 
     * @param atchFileId
	 * @return int 조회조건에 검색된 데이터
	 * @throws Exception
     */
    public int getFileListCnt(String atchFileId) throws Exception;
}