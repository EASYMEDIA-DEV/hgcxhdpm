package com.easymedia.error.hotel.utility.file;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class DownloadFileUtils {

    public Map<String, String> filCheck(HttpServletRequest request, HttpServletResponse response, String filePath, String realFilNm,
                                        String viewFileNm) throws Exception {

        Map<String, String> result = new HashMap<>();

        String path = sanitizeFilePath(filePath) + sanitizeFileName(realFilNm);
        File file = new File(path);

        if (file.exists() && file.isFile()) {
            result.put("fileExist", "Y");
        } else {
            result.put("fileExist", "N");
        }

        return result;
    }

    public void filDown(HttpServletRequest request, HttpServletResponse response, String filePath, String realFilNm,
                        String viewFileNm) throws Exception {

        String path = sanitizeFilePath(filePath) + sanitizeFileName(realFilNm);
        File file = new File(path);

        if (file.exists() && file.isFile()) {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setContentLength((int) file.length());
            String browser = getBrowser(request);
            String disposition = sanitizeDisposition(getDisposition(viewFileNm, browser));
            response.setHeader("Content-Disposition", disposition);
            response.setHeader("Content-Transfer-Encoding", "binary");
            OutputStream out = response.getOutputStream();
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
            if (fis != null)
                fis.close();
            out.flush();
            out.close();
        } else {
            throw new Exception("파일이 존재하지 않습니다.");
        }
    }

    public void viewImg(HttpServletRequest request, HttpServletResponse response, String filePath, String realFilNm,
                        String viewFileNm) throws Exception {

        String path = sanitizeFilePath(filePath) + sanitizeFileName(realFilNm);
        File file = new File(path);

        if (file.exists() && file.isFile()) {
            response.setHeader("X-Content-Type-Options", "");
            response.setHeader("X-Frame-Type-Options", "");
            response.setHeader("X-XSS-Protection-Options", "");
            String getViewType = getContentType(request, file);
            response.setContentType(getViewType);
            response.setContentLength((int) file.length());
            OutputStream out = response.getOutputStream();
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
            if (fis != null)
                fis.close();
            out.flush();
            out.close();
        } else {
            throw new Exception("파일이 존재하지 않습니다.");
        }
    }

    private String getContentType(HttpServletRequest request, File file) {

        String fileName = file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        ext = ext.toLowerCase();
        String contentType = "";
        if ("jpeg".equals(ext)) contentType = "image/jpeg";
        else if ("jpg".equals(ext)) contentType = "image/jpeg";
        else if ("png".equals(ext)) contentType = "image/png";
        else if ("bmp".equals(ext)) contentType = "image/bmp";
        else if ("ico".equals(ext)) contentType = "image/x-icon";
        else if ("gif".equals(ext)) contentType = "image/gif";
        else contentType = "application/octet-stream; charset=utf-8";

        return contentType;
    }

    private String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1)
            return "MSIE";
        else if (header.indexOf("Chrome") > -1)
            return "Chrome";
        else if (header.indexOf("Opera") > -1)
            return "Opera";
        return "Firefox";
    }

    private String getDisposition(String fileName, String browser) throws UnsupportedEncodingException {
        String dispositionPrefix = "attachment;filename=";
        String encodedFilename = null;
        String sanitizedFileName = sanitizeFileName(fileName);

        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(sanitizedFileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\"" + new String(sanitizedFileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Opera")) {
            encodedFilename = "\"" + new String(sanitizedFileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < sanitizedFileName.length(); i++) {
                char c = sanitizedFileName.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        }
        return dispositionPrefix + encodedFilename;
    }

    // 파일 이름에 대해 헤더 조작 최약점 방지
    private String sanitizeDisposition(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("File name for disposition cannot be null");
        }
        return fileName.replaceAll("[\r\n]", "");
    }

    // 파일 경로에 대해 경로 조작 취약점 방지
    public String sanitizeFilePath(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException("Path name cannot be null");
        }
        return filePath
                .replaceAll("\\.\\./", "")
                .replaceAll("\\.\\.\\\\", "")
                .replaceAll("\\.", "");
    }

    // 파일 이름에 대해 경로 조작 취약점 방지
    private String sanitizeFileName(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("File name cannot be null");
        }
        String baseName = FileNameUtils.getBaseName(fileName);
        String extension = FilenameUtils.getExtension(fileName);
        return baseName
                .replaceAll("\\.\\./", "")
                .replaceAll("\\.\\.\\\\", "")
                .replaceAll("[/\\\\.&]", "") + "." + extension;
    }
}