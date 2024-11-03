package com.bappi.videoinventorymanagement.service.impl;

import com.bappi.videoinventorymanagement.config.Constant;
import com.bappi.videoinventorymanagement.config.YamlValuesLoader;
import com.bappi.videoinventorymanagement.repository.LocalFileRepository;
import com.bappi.videoinventorymanagement.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
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
}
