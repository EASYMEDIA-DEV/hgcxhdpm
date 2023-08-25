package com.easymedia.service.impl;

import com.easymedia.dto.EmfMap;
import com.easymedia.error.hotel.utility.sim.SeedCipher;
import com.easymedia.service.SCADealershipAuthService;
import com.easymedia.service.dao.SCADealershipAuthDAO;
import com.easymedia.utility.COJsonUtil;
import com.easymedia.utility.COPaginationUtil;
import com.easymedia.utility.EgovFileMngUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * <pre>
 * 대리점-계정관리
 * </pre>
 *
 * @ClassName		: SCADealershipAuthServiceImpl.java
 * @Description		: 대리점-계정관리
 * @author 박주석
 * @since 2019.01.11
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.01.11		  박주석					     최초생성
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SCADealershipAuthServiceImpl implements SCADealershipAuthService {

	/* DAO */
    private final SCADealershipAuthDAO sCADealershipAuthDAO;

	/* 관리자 싴퉌스 */
	private final EgovIdGnrService admIdgen;

    private final EgovFileMngUtil fileUtil;

	/**
     * Sample List
     *
     * @param emfMap 데이터
	 * @return
	 * @throws Exception
     */
    public EmfMap selectDealershipList(EmfMap emfMap) throws Exception
    {
    	EmfMap rtnMap = new EmfMap();

    	PaginationInfo paginationInfo = new COPaginationUtil().getPage(emfMap);

		// 리스트 가져오기
		List<EmfMap> list = sCADealershipAuthDAO.selectDealershipList(emfMap);

		// 총 건수 가져오기
		int totCnt = sCADealershipAuthDAO.selectDealershipListTotCnt(emfMap);

		paginationInfo.setTotalRecordCount(totCnt);

		rtnMap.put("totCnt", totCnt);
		rtnMap.put("pageIndex", paginationInfo.getCurrentPageNo());
		rtnMap.put("recordPerPage", paginationInfo.getRecordCountPerPage());
		rtnMap.put("totPage", paginationInfo.getTotalPageCount());
		rtnMap.put("list", COJsonUtil.getJsonArrayFromList(list));

    	return rtnMap;
    }

    /**
     * 관리자 상세
     *
     * @param emfMap 데이터
	 * @return
	 * @throws Exception
     */
    public EmfMap selectDealershipDtl(EmfMap emfMap) throws Exception
    {
    	if (!"".equals(emfMap.getString("detailsKey")))
    	{
    		//상세 조회
    		emfMap.put("info", COJsonUtil.getJsonStringFromMap(sCADealershipAuthDAO.selectDealershipDtl(emfMap)));
    	}
    	//대리점 조회
    	emfMap.put("dealershipList", COJsonUtil.getJsonArrayFromList(sCADealershipAuthDAO.getDealershipList(emfMap)));

    	return emfMap;
    }

    /**
     * 관리자 ID 존재 확인
     *
     * @param emfMap 데이터
	 * @return
	 * @throws Exception
     */
    public Integer getEmailExistenceCnt(EmfMap emfMap) throws Exception
    {
    	return sCADealershipAuthDAO.getEmailExistenceCnt(emfMap);
    }

    /**
     * 관리자 휴대전화번호 확인
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public Integer getHpExistenceCnt(EmfMap emfMap) throws Exception
    {
    	int cnt = 0;
    	String hp = emfMap.getString("hp");
    	//EmfMap lgnMap = (EmfMap) EgovUserDetailsHelper.getAuthenticatedUser();
		EmfMap lgnMap = new EmfMap();
    	String authHp = lgnMap.getString("hp");
    	if(hp.equals(authHp))
    	{
    		cnt = 0;
    	}
    	else
    	{
    		cnt = sCADealershipAuthDAO.getHpExistenceCnt(emfMap);
    	}
    	return cnt;
    }

	/**
     * Insert
     *
     * @param emfMap
	 * @return
	 * @throws Exception
     */
	public int insertDealershipAuther(EmfMap emfMap) throws Exception
	{
		int cnt = 0;
		emfMap.put("detailsKey", admIdgen.getNextIntegerId());
		emfMap.put("asgnTaskCd", String.join(",", emfMap.getList("asgnTaskCd")));
		emfMap.put("pwd", SeedCipher.oneencrypt(emfMap.getString("pwd")));
		
		int existenceCnt = 0;
		existenceCnt = sCADealershipAuthDAO.getEmailExistenceCnt(emfMap);

		if(existenceCnt == 0){
			sCADealershipAuthDAO.insertDealershipAutherMst(emfMap);
			cnt = sCADealershipAuthDAO.insertDealershipAutherDtl(emfMap);
		}

		return cnt;
	}

	/**
     * Update
     *
     * @param emfMap 데이터
	 * @return
	 * @throws Exception
     */
	public int updateDealershipAuther(EmfMap emfMap) throws Exception
	{
		emfMap.put("asgnTaskCd", String.join(",", emfMap.getList("asgnTaskCd")));
		if(!"".equals(emfMap.getString("pwd")))
		{
			emfMap.put("pwd", SeedCipher.oneencrypt(emfMap.getString("pwd")));
		}
		sCADealershipAuthDAO.updateDealershipAutherMst(emfMap);
		return sCADealershipAuthDAO.updateDealershipAutherDtl(emfMap);
	}

	/**
     * 계정 승인
     *
     * @param emfMap 데이터
	 * @return
	 * @throws Exception
     */
	public int updateDealershipAppyn(EmfMap emfMap) throws Exception
	{
		return sCADealershipAuthDAO.updateDealershipAppyn(emfMap);
	}

	/**
     * 비밀번호 초기화
     *
     * @param emfMap 데이터
	 * @return
	 * @throws Exception
     */
	public int updateInitPwd(EmfMap emfMap) throws Exception
	{
		//난수 비밀번호 조회
		Random rnd =new Random();

    	StringBuffer buf =new StringBuffer();

    	// 영문 + 숫자 랜덤하게 6자리
    	for(int i=0 ; i<6 ; i++){
    	    if(rnd.nextBoolean()){
    	        buf.append((char)((int)(rnd.nextInt(26))+97));
    	    }else{
    	        buf.append((rnd.nextInt(10)));
    	    }
    	}

    	String tmpPwd = buf.toString();

		if (!"".equals(tmpPwd) && tmpPwd.length() >= 6)
		{
			emfMap.put("pwd", SeedCipher.oneencrypt(tmpPwd));
		}

		return sCADealershipAuthDAO.updateInitPwd(emfMap);
	}

	/**
     * 엑셀다운로드
     *
     * @param emfMap 데이터
	 * @return
	 * @throws Exception
     */
	public EmfMap excelDealershipList(EmfMap emfMap) throws Exception
	{
		EmfMap rtnMap = new EmfMap();

		rtnMap.put("list", COJsonUtil.getJsonArrayFromList(sCADealershipAuthDAO.excelDealershipList(emfMap)));

		return rtnMap;
	}


	/**
     * 고객관리 파일 업로드
     *
     * @param emfMap 데이터
     * @param multiRequest 파일
	 * @return
	 * @throws Exception
     */
    public EmfMap setExcelFile(EmfMap emfMap, MultipartHttpServletRequest multiRequest) throws Exception
    {
    	Map<String, MultipartFile> files = multiRequest.getFileMap();
    	FileInputStream fis = null;
        XSSFWorkbook workbook = null;
        List<String> tabList = new ArrayList<String>();
        List<String> thList = new ArrayList<String>();
        List<List> tdList 	= new ArrayList<List>();
		if ( !files.isEmpty() )
		{
			String uploadFileName = fileUtil.excelfileUpload(multiRequest, "atchAuraFile");
		    fis = new FileInputStream(uploadFileName);
		    workbook = new XSSFWorkbook(fis);//엑셀읽기
		    int rowindex=0;
		    int columnindex=0;
		    int sheetSize = 0;
		    int rowSize = 0;
		    int cellSize = 0;
		    String value="";
		    XSSFSheet sheet = null;
		    XSSFRow   curRow = null;
            XSSFCell  curCell = null;
		    //시트 조회
		    sheetSize = workbook.getNumberOfSheets();
		    if(sheetSize > 0)
		    {
		    	for(int sidx = 0 ; sidx < sheetSize ; sidx++)
		    	{
		    		sheet = workbook.getSheetAt(sidx);
		    		if(sheet != null && !"".equals(sheet.getSheetName()))
		    		{
		    			tabList.add(sheet.getSheetName());
		    			List<List> tdDtlList = new ArrayList<List>();
		    			rowSize = sheet.getPhysicalNumberOfRows();
		    			for(int rowIndex=3; rowIndex < rowSize; rowIndex++)
		    			{
		    				curRow = sheet.getRow(rowIndex);
		    				if(curRow != null)
			    			{
			    				cellSize = curRow.getPhysicalNumberOfCells();
			    				List<String> tmpList = new ArrayList<String>();
			    				for(int cellIndex=0;cellIndex<cellSize; cellIndex++)
			    				{
	                                curCell = curRow.getCell(cellIndex);
	                                if(curCell != null)
		                            {
		                                value = "";
		                                switch (curCell.getCellType())
		                                {
											case FORMULA:
			                                    value = curCell.getCellFormula();
			                                    break;
											case NUMERIC:
			                                	// 숫자일 경우, String형으로 변경하여 값을 읽는다.
												curCell.setCellType(CellType.STRING);
			                                    value = curCell.getStringCellValue()+"";
			                                    break;
											case STRING:
			                                    value = curCell.getStringCellValue()+"";
			                                    break;
											case BLANK:
			                                    value = "";
			                                    break;
											case ERROR:
			                                    value = curCell.getErrorCellValue()+"";
			                                    break;
			                                default:
			                                    value = new String();
			                                    break;
		                                }
		                                if(rowIndex == 0 && sidx == 0)
		                            	{
		                            		thList.add(value);
		                            	}
		                            	if(rowIndex != 0)
		                            	{
		                            		tmpList.add(value);
		                            	}
		                            }
			    				}
			    				if(tmpList.size() > 0 && rowIndex != 0)
			    				{
			    					tdDtlList.add(tmpList);
			    				}
			    			}
		    			}
		    			tdList.add(tdDtlList);
		    		}
		    	}
		    }
		    //컬럼 조회
		}
        emfMap.put("tabList", tabList);
        emfMap.put("thList", thList);
        emfMap.put("tdList", tdList);
    	return emfMap;
    }

    /**
     * 고객관리 파일 데이터 업로드
     *
     * @param emfMap 데이터
	 * @return
	 * @throws Exception
     */
    public int insertExcelDataUpload(EmfMap emfMap) throws Exception
    {
    	int actCnt = 0;
    	List<String> ntnCdList 		= emfMap.getList("ntnNm");
    	List<String> dlspCdList 	= emfMap.getList("dlspCd");
    	List<String> dlrCdList 		= emfMap.getList("dlrCd");
    	List<String> emailList 		= emfMap.getList("email");
    	List<String> firstNmList 	= emfMap.getList("firstNm");
    	List<String> lastNmList 	= emfMap.getList("lastNm");
    	//List<String> hpList 		= emfMap.getList("hp");						//2021.02.08 삭제처리
    	//List<String> authCdList 	= emfMap.getList("authCd");
    	List<String> asgnTaskCdList = emfMap.getList("asgnTaskCd");
    	List<String> hgcxUseList	= emfMap.getList("hgcxUseYn");				//2021.02.08 추가
    	List<String> hdisUseList	= emfMap.getList("hdisUseYn");				//2021.02.08 추가
    	List<String> sheetNmList	= emfMap.getList("sheetNm");				//2021.09.29 추가

    	if(emailList != null && emailList.size() > 0)
    	{
    		EmfMap authMap = null;
    		for(int q = 0 ; q < emailList.size() ; q++)
    		{
    			if(emailList.get(q) != null && !"".equals(emailList.get(q)))
    			{
	    			EmfMap tmpMap = new EmfMap();
	    			emfMap.put("asgnTaskCd", asgnTaskCdList.get(q).replace(" ", "").trim()); //공백제거 추가 2021.09.29
	    			emfMap.put("pwd", "NqCDwMRh4xC9LoHAN42uwReGNWRuk+zwSO+WzcyLEoM=");		// 2020.05.11 암호화
	    			emfMap.put("id", emailList.get(q));
	    			emfMap.put("name", firstNmList.get(q) + " " + lastNmList.get(q));
	    			emfMap.put("hgcxUseYn" , "Y");//2021.09.28 파일 업로드로 account 생성시 기본값  Y 세팅
	    			emfMap.put("hdisUseYn", "Y");//2021.09.28 파일 업로드로 account 생성시 기본값 Y 세팅
	    			//emfMap.put("hp", hpList.get(q));
	    			emfMap.put("regId", "dev@easymedia.net");
	    			emfMap.put("modId", "dev@easymedia.net");

	    			emfMap.put("firstName", firstNmList.get(q));
	    			emfMap.put("lastName", lastNmList.get(q));

	    			emfMap.put("dlrspCd", dlspCdList.get(q));

	    			authMap = sCADealershipAuthDAO.getNtnDealershipAuth(emfMap);
	    			if(authMap != null)
	    			{
	    				emfMap.put("detailsKey", authMap.getString("admSeq"));
	    				actCnt = actCnt + sCADealershipAuthDAO.updateDealershipExcelAutherMst(emfMap);
		    			sCADealershipAuthDAO.updateDealershipExcelAutherDtl(emfMap);
	    			}
	    			else
	    			{
	    				emfMap.put("detailsKey", admIdgen.getNextIntegerId());
	    				actCnt = actCnt + sCADealershipAuthDAO.insertDealershipAutherMst(emfMap);
		    			sCADealershipAuthDAO.insertDealershipAutherDtl(emfMap);
		    			sCADealershipAuthDAO.updateDealershipAppyn(emfMap);
	    			}
    			}
    		}
    	}
    	return actCnt;
    }
}