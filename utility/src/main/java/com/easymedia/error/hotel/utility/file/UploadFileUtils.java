package com.easymedia.error.hotel.utility.file;

import com.easymedia.error.ErrorCode;
import com.easymedia.error.exception.BusinessException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
public class UploadFileUtils {

    public static String saveUploadedFiles(MultipartFile file, String savePath) {

        savePath = StringUtils.stripEnd(savePath, "/");
        savePath = StringUtils.stripEnd(savePath, "\\");

        File uploadDir = new File(savePath);
        if (uploadDir.exists() == false) {
            uploadDir.mkdirs();
        }

        if (file == null || file.isEmpty()) {
            return null;
        }

        String fileName = newFileName(savePath, file.getOriginalFilename());

        try {
            byte[] bytes = file.getBytes();
            Files.write(Paths.get(savePath, fileName), bytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAIL);
        }

        return fileName;
    }

    public static String saveUploadedRealNameFiles(MultipartFile file, String savePath) {

        savePath = StringUtils.stripEnd(savePath, "/");
        savePath = StringUtils.stripEnd(savePath, "\\");

        File uploadDir = new File(savePath);
        if (uploadDir.exists() == false) {
            uploadDir.mkdirs();
        }

        if (file == null || file.isEmpty()) {
            return null;
        }

        String fileName = file.getOriginalFilename();

        try {
            byte[] bytes = file.getBytes();
            Files.write(Paths.get(savePath, fileName), bytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAIL);
        }

        return fileName;
    }

    public static String copyFile(String filePath, String fileName) {
        if (Files.notExists(Paths.get(filePath, fileName))) {
            throw new BusinessException("파일이 없습니다.", ErrorCode.NOT_FOUND);
        }
        String newFileName = newFileName(filePath, fileName);
        try {
            Files.copy(Paths.get(filePath, fileName), Paths.get(filePath, newFileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.FILE_COPY_FAIL);
        }
        return newFileName;
    }

    public static void deleteFile(String filePath, String fileName) {
        try {
            Files.delete(Paths.get(filePath, fileName));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void deleteFileIfEmpty(String filePath, String fileName) {
        try {
            if (isEmptyFile(filePath, fileName)) {
                Files.delete(Paths.get(filePath, fileName));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static String newFileName(String filePath, String fileName) {
        String newFileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName);
        while (Files.exists(Paths.get(filePath, newFileName))) {
            newFileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName);
        }
        return newFileName;
    }

    public static String getFileNameFromUrl(String url) {
        String[] split = StringUtils.split(url, "/");
        String s = split[split.length - 1];
        return s;
    }

    public static boolean existsFile(String filePath, String fileName) {
        return Files.exists(Paths.get(filePath, fileName));
    }

    @SneakyThrows
    public static boolean isEmptyFile(String filePath, String fileName) {
        Path path = Paths.get(filePath, fileName);
        return Files.exists(path) && Files.size(path) == 0;
    }

    public static boolean saveFileFromUrl(String url, String filePath, String fileName) {
        filePath = StringUtils.stripEnd(filePath, "/");
        filePath = StringUtils.stripEnd(filePath, "\\");

        File uploadDir = new File(filePath);
        if (uploadDir.exists() == false) {
            uploadDir.mkdirs();
        }
        try {
            IOUtils.copy(new URL(url), new File(uploadDir, fileName));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            deleteFileIfEmpty(filePath, fileName);
            return false;
        }

    }
}
