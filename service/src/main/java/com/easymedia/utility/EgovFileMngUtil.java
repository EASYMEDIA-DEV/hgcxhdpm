package com.easymedia.utility;

import com.easymedia.dto.EmfMap;
import com.easymedia.error.hotel.utility.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @Class Name  : EgovFileMngUtil.java
 * @Description : 메시지 처리 관련 유틸리티
 * @Modification Information
 *
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.02.13       이삼섭                  최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 02. 13
 * @version 1.0
 * @see
 *
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EgovFileMngUtil
{

    public static final int BUFF_SIZE = 2048;

    private final EgovIdGnrService egovFileIdGnrService;

    private String[] thnlFileWidthHeight = "280_164,280_220".split(",");

	@Value("${globals.excel-sample-file-path}")
    private String excelSamplelFilePath;

	@Value("${globals.file-store-path}")
	private String fileStorePath;

    /**
     * 첨부파일에 대한 목록 정보를 취득한다.
     *
     * @param files
     * @return
     * @throws Exception
     */
    public List<EmfMap> parseFileInf(Map<String, MultipartFile> files, String KeyStr, int fileKeyParam, String atchFileId, String storePath, String formname, long fileSize, String[] checkFileExt) throws Exception
    {
		return this.parseFileInf(files, KeyStr, fileKeyParam, atchFileId, storePath, formname, fileSize, checkFileExt, false);
    }

    /**
     * 첨부파일에 대한 목록 정보를 취득한다.
     *
     * @param files
     * @return
     * @throws Exception
     */
    public List<EmfMap> parseFileInf(Map<String, MultipartFile> files, String KeyStr, int fileKeyParam, String atchFileId, String storePath, String formname, long fileSize, String[] checkFileExt, boolean isThumNail) throws Exception
    {
		int fileKey = fileKeyParam;

		String pyhPath = "";

		pyhPath = fileStorePath;

		String atchFileIdString = "";

		if ("".equals(atchFileId) || atchFileId == null)
		{
		    atchFileIdString = egovFileIdGnrService.getNextStringId();
		}
		else
		{
		    atchFileIdString = atchFileId;
		}

		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;

		EmfMap fvo;
		List<EmfMap> result = new ArrayList<EmfMap>();

		String filePyhPath = null;

		boolean isImage = false;
		boolean isVideo = false;

		while (itr.hasNext())
		{
		    Entry<String, MultipartFile> entry = itr.next();

		    file = entry.getValue();

		    if (file.getName().indexOf(formname) > -1)
		    {
		    	filePyhPath = pyhPath;
			    String mimeType = file.getContentType();
			    Calendar c = Calendar.getInstance();
			    if (mimeType.indexOf("image") > -1)
			    {
			    	isImage = true;
			    	isVideo = false;
			    	filePyhPath = filePyhPath  + "image" + File.separator + String.valueOf(c.get(Calendar.YEAR)) + File.separator + String.valueOf(c.get(Calendar.MONTH) + 1);
			    }
			    else if (mimeType.indexOf("video") > -1)
			    {
			    	isImage = false;
			    	isVideo = true;
			    	filePyhPath = filePyhPath + "video" + File.separator + String.valueOf(c.get(Calendar.YEAR)) + File.separator + String.valueOf(c.get(Calendar.MONTH) + 1);
			    }
			    else
			    {
			    	isImage = false;
			    	isImage = false;
			    	filePyhPath = filePyhPath + String.valueOf(c.get(Calendar.YEAR)) + File.separator + String.valueOf(c.get(Calendar.MONTH) + 1);
			    }

				File saveFolder = new File(WebUtil.filePathBlackList(filePyhPath));

				if (!saveFolder.exists() || saveFolder.isFile())
				{
				    saveFolder.mkdirs();
				}

			    //--------------------------------------
			    // 원 파일명이 없는 경우 처리
			    // (첨부가 되지 않은 input file type)
			    //--------------------------------------
				String realFileNm = file.getOriginalFilename();
			    if ("".equals(realFileNm))
			    {
			    	continue;
			    }
				else
				{
			    	// 파일명이 동일한 경우 
			    	Random random = new Random();
			    	realFileNm = (random.nextInt(100) + 1) + "_" + realFileNm;
			    }
			    ////------------------------------------

			    long _size = file.getSize();
			    if (_size > (long) fileSize)
			    {
			    	continue;
			    }

			    String fileExtn = realFileNm.substring(realFileNm.lastIndexOf(".") + 1);

			    boolean isFileExt = true;

			    if (checkFileExt != null)
			    {
			    	isFileExt = false;

			    	for (int q = 0; q < checkFileExt.length; q++)
			    	{
			    		if (checkFileExt[q].trim().toLowerCase().equals(fileExtn.toLowerCase()))
			    		{
			    			isFileExt = true;
			    			break;
			    		}
			    	}
			    }

			    if (!isFileExt)
			    {
			    	continue;
			    }

			    String saveFileNm = KeyStr + getTimeStamp() + fileKey;
			    String filePath = null, webPath = null;

		    	if (isImage || isVideo)
		    	{

		    		saveFileNm = saveFileNm + "." + fileExtn;

		    		filePath = filePyhPath + File.separator + saveFileNm;
		    		webPath = File.separator + filePath.replace(pyhPath, "");
		    		webPath = webPath.replace(File.separator, "/");
		    	}
		    	else
		    	{
		    		filePath = filePyhPath + File.separator + saveFileNm;
		    	}

				//FileCopyUtils.copy(file.getBytes(), new File(WebUtil.filePathBlackList(filePath)));

		    	file.transferTo(new File(WebUtil.filePathBlackList(filePath)));

		    	//썸네일 생성
                if (isThumNail && isImage)
                {
                	String saveThumnailNm = null;

            		String[] thumnailWidthHeightSize;

            		for (int q = 0; q < thnlFileWidthHeight.length; q++)
                	{
            			thumnailWidthHeightSize = thnlFileWidthHeight[q].split("_");
            			saveThumnailNm = saveFileNm.substring(0, saveFileNm.lastIndexOf(".")) + "_" + thumnailWidthHeightSize[1] + "." + fileExtn;
            			Thumbnails.of(filePath).crop(Positions.TOP_CENTER).size(Integer.parseInt(thumnailWidthHeightSize[0]), Integer.parseInt(thumnailWidthHeightSize[1])).outputQuality(1).toFile(new File(filePyhPath + File.separator + saveThumnailNm));
                	}
                }

			    fvo = new EmfMap();
			    fvo.put("atchFileId", atchFileIdString);
			    fvo.put("fileSeq", fileKey);
			    fvo.put("phyPath", filePyhPath);
			    fvo.put("realFileNm", realFileNm);
			    fvo.put("saveFileNm", saveFileNm);
			    fvo.put("fileExtn", fileExtn);
			    fvo.put("fileSize", _size);
			    fvo.put("webPath", webPath);
			    result.add(fvo);
			    fileKey++;
		    }
		}

		return result;
    }

    /**
     * 첨부파일에 대한 파일 사이즈를 반환한다.
     *
     * @param files
     * @return
     * @throws Exception
     */
    public long parseFileSize(Map<String, MultipartFile> files) throws Exception
    {
		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;

		long _size = 0;

		while (itr.hasNext())
		{
		    Entry<String, MultipartFile> entry = itr.next();
		    file = entry.getValue();
		    String orginFileName = file.getOriginalFilename();

		    //--------------------------------------
		    // 원 파일명이 없는 경우 처리
		    // (첨부가 되지 않은 input file type)
		    //--------------------------------------
		    if ("".equals(orginFileName))
		    {
		    	continue;
		    }
		    ////------------------------------------

		    _size = file.getSize();
		}

		return _size;
    }

    /**
     * 첨부파일을 서버에 저장한다.
     *
     * @param file
     * @param newName
     * @param stordFilePath
     * @throws Exception
     */
    protected void writeUploadedFile(MultipartFile file, String newName, String stordFilePath) throws Exception
    {
		InputStream stream = null;
		OutputStream bos = null;

		try
		{
		    stream = file.getInputStream();
		    File cFile = new File(stordFilePath);

		    if (!cFile.isDirectory())
		    {
		    	boolean _flag = cFile.mkdir();

				if (!_flag)
				{
				    throw new IOException("Directory creation Failed ");
				}
		    }

		    bos = new FileOutputStream(stordFilePath + File.separator + newName);

		    int bytesRead = 0;
		    byte[] buffer = new byte[BUFF_SIZE];

		    while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1)
		    {
		    	bos.write(buffer, 0, bytesRead);
		    }
		}
		catch (Exception e)
		{
		    log.error("IGNORE:", e);	// 2011.10.10 보안점검 후속조치
		}
		finally
		{
		    if (bos != null)
		    {
				try
				{
				    bos.close();
				}
				catch (Exception ignore)
				{
				    log.debug("IGNORED: " + ignore.getMessage());
				}
		    }
		    if (stream != null)
		    {
				try
				{
				    stream.close();
				} catch (Exception ignore)
				{
				    log.debug("IGNORED: " + ignore.getMessage());
				}
		    }
		}
    }

    /**
     * 서버의 파일을 다운로드한다.
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public static void downFile(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		String downFileName = "";
		String orgFileName = "";

		if ((String)request.getAttribute("downFile") == null)
		{
		    downFileName = "";
		}
		else
		{
		    downFileName = (String)request.getAttribute("downFile");
		}

		if ((String)request.getAttribute("orgFileName") == null)
		{
		    orgFileName = "";
		}
		else
		{
		    orgFileName = (String)request.getAttribute("orginFile");
		}

		orgFileName = orgFileName.replaceAll("\r", "").replaceAll("\n", "");

		File file = new File(WebUtil.filePathBlackList(downFileName));

		if (!file.exists())
		{
		    throw new FileNotFoundException(downFileName);
		}

		if (!file.isFile())
		{
		    throw new FileNotFoundException(downFileName);
		}

		//buffer size 2K.
		byte[] b = new byte[BUFF_SIZE];

		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment; filename=" + new String(orgFileName.getBytes(), "UTF-8"));
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");

		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try
		{
			fin = new BufferedInputStream(new FileInputStream(file));
		    outs = new BufferedOutputStream(response.getOutputStream());
		    int read = 0;

			while ((read = fin.read(b)) != -1)
			{
			    outs.write(b, 0, read);
			}
		}
		finally
		{
		    if (outs != null)
		    {
				try
				{
				    outs.close();
				}
				catch (Exception ignore)
				{
				    log.debug("IGNORED: " + ignore.getMessage());
				}
		    }

		    if (fin != null)
		    {
				try
				{
				    fin.close();
				}
				catch (Exception ignore)
				{
				    log.debug("IGNORED: " + ignore.getMessage());
				}
		    }
		}
    }

    /**
     * 파일을 실제 물리적인 경로에 생성한다.
     *
     * @param file
     * @param newName
     * @param stordFilePath
     * @throws Exception
     */
    protected static void writeFile(MultipartFile file, String newName, String stordFilePath) throws Exception
    {
		InputStream stream = null;
		OutputStream bos = null;

		try
		{
		    stream = file.getInputStream();

		    File cFile = new File(WebUtil.filePathBlackList(stordFilePath));

		    if (!cFile.isDirectory())
		    {
		    	cFile.mkdir();
		    }

		    bos = new FileOutputStream(WebUtil.filePathBlackList(stordFilePath + File.separator + newName));

		    int bytesRead = 0;

		    byte[] buffer = new byte[BUFF_SIZE];

		    while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1)
		    {
		    	bos.write(buffer, 0, bytesRead);
		    }
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}
		finally
		{
		    if (bos != null)
		    {
				try
				{
				    bos.close();
				}
				catch (Exception ignore)
				{
					log.error(ignore.getMessage());
				}
		    }
		    if (stream != null)
		    {
				try
				{
				    stream.close();
				}
				catch (Exception ignore)
				{
				    log.error(ignore.getMessage());
				}
		    }
		}
    }

    /**
     * 서버 파일에 대하여 다운로드를 처리한다.
     *
     * @param response
     * @param streFileNm
     *            : 파일저장 경로가 포함된 형태
     * @param orignFileNm
     * @throws Exception
     */
    public void downFile(HttpServletResponse response, String streFileNm, String orignFileNm) throws Exception
    {
		String downFileName = streFileNm;
		String orgFileName = orignFileNm;

		File file = new File(downFileName);

		if (!file.exists())
		{
		    throw new FileNotFoundException(downFileName);
		}

		if (!file.isFile())
		{
		    throw new FileNotFoundException(downFileName);
		}

		//byte[] b = new byte[BUFF_SIZE];
		//buffer size 2K.
		int fSize = (int)file.length();

		if (fSize > 0)
		{
		    BufferedInputStream in = null;

		    try
		    {
		    	in = new BufferedInputStream(new FileInputStream(file));

    	    	String mimetype = "text/html"; //"application/x-msdownload"

    	    	response.setBufferSize(fSize);
				response.setContentType(mimetype);
				response.setHeader("Content-Disposition", "attachment; filename=" + orgFileName);
				response.setContentLength(fSize);
				FileCopyUtils.copy(in, response.getOutputStream());
		    }
		    finally
		    {
				if (in != null)
				{
				    try
				    {
				    	in.close();
				    }
				    catch (Exception ignore)
				    {
				    	log.debug(ignore.getMessage());
				    }
				}
		    }

		    response.getOutputStream().flush();
		    response.getOutputStream().close();
		}
    }

    /**
     * 2011.08.09
     * 공통 컴포넌트 utl.fcc 패키지와 Dependency제거를 위해 내부 메서드로 추가 정의함
     * 응용어플리케이션에서 고유값을 사용하기 위해 시스템에서17자리의TIMESTAMP값을 구하는 기능
     *
     * @param
     * @return Timestamp 값
     * @see
     */
    public static String getTimeStamp()
    {
		String rtnStr = null;

		// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
		String pattern = "yyyyMMddhhmmssSSS";

		try
		{
		    SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		    Timestamp ts = new Timestamp(System.currentTimeMillis());
		    rtnStr = sdfCurrent.format(ts.getTime());
		}
		catch (Exception e)
		{
			log.debug("IGNORED: " + e.getMessage());
		}

		return rtnStr;
    }



    /**
	 * 브라우저 구분 얻기.
	 *
	 * @param request
	 * @return String
	 * @throws Exception
	 */
    public String getBrowser(HttpServletRequest request)
    {
        String header = request.getHeader("User-Agent");

        if (header.indexOf("MSIE") > -1)
        {
            return "MSIE";
        }
        else if (header.indexOf("Trident") > -1)
        {
        	return "Trident";
        }
        else if (header.indexOf("Chrome") > -1)
        {
            return "Chrome";
        }
        else if (header.indexOf("Opera") > -1)
        {
            return "Opera";
        }
        else if (header.indexOf("Safari") > -1)
        {
            return "Safari";
        }

        return "Firefox";
    }

    /**
     * Disposition 지정하기.
     *
     * @param filename
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		String browser = getBrowser(request);

		String dispositionPrefix = "attachment; filename=";
		String encodedFilename = null;

		if (browser.equals("MSIE"))
		{
		    encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		}
		else if(browser.equals("Trident"))
		{
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		}
		else if (browser.equals("Firefox"))
		{
		    encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		}
		else if (browser.equals("Opera"))
		{
		    encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		}
		else if (browser.equals("Chrome"))
		{
		    StringBuffer sb = new StringBuffer();

		    for (int i = 0; i < filename.length(); i++)
		    {
				char c = filename.charAt(i);

				if (c > '~')
				{
				    sb.append(URLEncoder.encode("" + c, "UTF-8"));
				}
				else
				{
				    sb.append(c);
				}
		    }

		    encodedFilename = sb.toString();
		}
		else if (browser.equals("Safari"))
		{
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		}
		else
		{
		    throw new IOException("Not supported browser");
		}

		response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);
		response.setHeader("Content-Transfer-Encoding", "binary");
    }




    /**
     * 엑셀 파일 업로드
     *
     * @param mRequest
     * @param uploadFileName
     * @return
     * @throws Exception
     */
    public String excelfileUpload(MultipartHttpServletRequest mRequest, String uploadFileName) {

		String saveFileName = "";

		Calendar c = Calendar.getInstance();
		String uploadPath = excelSamplelFilePath + String.valueOf(c.get(Calendar.YEAR)) + File.separator + String.valueOf(c.get(Calendar.MONTH) + 1);
		File dir = new File(WebUtil.filePathBlackList(uploadPath));
		if (!dir.exists() || dir.isFile())
		{
			dir.mkdirs();
		}
		MultipartFile mFile = mRequest.getFile(uploadFileName);
		if(mFile != null && !"".equals(mFile.getOriginalFilename()))
		{

			if(new File(uploadPath + File.separator + saveFileName).exists())
			{
				saveFileName = System.currentTimeMillis() + "." + mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf(".") + 1);
			}
			try
			{
				mFile.transferTo(new File(uploadPath + File.separator + saveFileName));
			}
			catch (IllegalStateException e)
			{
				e.printStackTrace();

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return uploadPath + File.separator + saveFileName;
	}
}