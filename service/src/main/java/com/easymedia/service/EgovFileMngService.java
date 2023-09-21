package com.easymedia.service;

import com.easymedia.dto.EmfMap;

import java.util.List;

/**
 * @Class Name : EgovFileMngService.java
 * @Description : 파일정보의 관리를 위한 서비스 인터페이스
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
public interface EgovFileMngService
{
	/**
   	 * 파일 목록을 조회한다.
   	 *
   	 * @param emfMap
   	 * @return EmfMap 조회조건에 검색된 데이터
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
     * 하나의 파일에 대한 정보(속성 및 상세)를 등록한다.
     *
     * @param emfMap
	 * @return String
	 * @throws Exception
     */
    public void insertFileInf(EmfMap emfMap) throws Exception;

    /**
     * 여러 개의 파일에 대한 정보(속성 및 상세)를 등록한다.
     *
     * @param fvoList
	 * @return String
	 * @throws Exception
     */
    public String insertFileInfs(List<EmfMap> fvoList) throws Exception;

    /**
     * 하나의 파일에 대한 정보(속성 및 상세)를 수정한다.
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public void updateFileInf(EmfMap emfMap) throws Exception;

    /**
     * 여러 개의 파일에 대한 정보(속성 및 상세)를 수정한다.
     *
     * @param fvoList
	 * @return
	 * @throws Exception
     */
    public void updateFileInfs(List<EmfMap> fvoList) throws Exception;
    
    /**
     * 여러 개의 파일에 대한 정보(속성 및 상세)를 수정한다.
     *
     * @param fvoList
	 * @return atchfile_id
	 * @throws Exception
     */
    public String updateFileInf(List<EmfMap> fvoList) throws Exception;
    
    /**
     * 하나의 파일을 삭제한다.
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public void deleteFileInf(EmfMap emfMap) throws Exception;

    /**
     * 여러 개의 파일을 삭제한다.
     *
     * @param fvoList
	 * @return
	 * @throws Exception
     */
    public void deleteFileInfs(List<EmfMap> fvoList) throws Exception;

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
	 * @return List<EmfMap>
	 * @throws Exception
     */
    public List<EmfMap> selectFileListByFileNm(EmfMap emfMap) throws Exception;

    /**
     * 이미지 파일에 대한 목록을 조회한다.
     *
     * @param emfMap
	 * @return List<EmfMap>
	 * @throws Exception
     */
    public List<EmfMap> selectImageFileList(EmfMap emfMap) throws Exception;

    /**
     * 파일ID에 대한 최대 파일순번을 가져온다.
     *
     * @param atchFileId
	 * @return int
	 * @throws Exception
     */
    public int getMaxFileSeq(String atchFileId) throws Exception;

    /**
     * 파일ID에 대한 파일갯수를 가져온다.
     *
     * @param val
	 * @return int 조회조건에 검색된 데이터
	 * @throws Exception
     */
    public int getFileListCnt(String val) throws Exception;
}