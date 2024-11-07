package com.bappi.videoinventorymanagement.service.impl;

import com.bappi.videoinventorymanagement.config.Constant;
import com.bappi.videoinventorymanagement.config.YamlValuesLoader;
import com.bappi.videoinventorymanagement.repository.LocalFileRepository;
import com.bappi.videoinventorymanagement.service.FileService;
import com.bappi.videoinventorymanagement.utils.APIErrorCode;
import com.bappi.videoinventorymanagement.utils.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final YamlValuesLoader yamlValuesLoader;
    private final LocalFileRepository localFileRepository;

    @Autowired
    public FileServiceImpl(YamlValuesLoader yamlValuesLoader, LocalFileRepository localFileRepository) {
        this.yamlValuesLoader = yamlValuesLoader;
        this.localFileRepository = localFileRepository;
    }

    @Override
    public String uploadFileToFileStorage(MultipartFile file) {

        String fileName = file.getOriginalFilename();
        String uploadedFilePaths = null;
        fileName = Instant.now().toEpochMilli() + Constant.EVENT_ID_SEPARATOR + fileName;
        String baseUrl = getAbsolutePath(fileName);

        try {
            localFileRepository.saveFile(file.getInputStream(), baseUrl);
            uploadedFilePaths = baseUrl.substring((yamlValuesLoader.getLocalFileUploadDir() + Constant.FILE_PATH_SEPARATOR_SLASH).length());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return uploadedFilePaths;
    }

    public String getAbsolutePath(String fileName) {
        return yamlValuesLoader.getLocalFileUploadDir() + Constant.FILE_PATH_SEPARATOR_SLASH + fileName;
    }

    public FileInputStream downloadFileFromFileStorage(String fileToDownLoad) {
        try {
            if (fileToDownLoad == null) {
                throw new CustomException(APIErrorCode.ILLEGAL_ARGUMENT, HttpStatus.BAD_REQUEST);
            }
            String absoluteFilePath = getAbsolutePath(fileToDownLoad);
            log.info("[FileService][FileInputStream]: download file from file storage: " + absoluteFilePath);

            return new FileInputStream(localFileRepository.downloadFile(absoluteFilePath));
        } catch (Exception exception) {
            log.warn("[FileService][downloadFileFromFileStorage]: " + exception.getMessage());
        }

        return null;
    }

    public HttpHeaders getHeader(String fileName) {
        String file = getSubStringAfterSpecificChar(fileName, Constant.EVENT_ID_SEPARATOR);
        HttpHeaders headers = new HttpHeaders();
        ContentDisposition disposition = ContentDisposition.attachment().filename(file).build();
        headers.setContentDisposition(disposition);
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");


        return headers;
    }

    public String getSubStringAfterSpecificChar(String string, String specificChar) {
        int index = string.indexOf(specificChar);
        return string.substring(index + 1);
    }

    public MediaType getContentType(String file) {
        String absoluteFilePath = getAbsolutePath(file);
        String contentType = "";
        try {
            contentType = Files.probeContentType(new File(absoluteFilePath).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return MediaType.parseMediaType(contentType);
    }
}
