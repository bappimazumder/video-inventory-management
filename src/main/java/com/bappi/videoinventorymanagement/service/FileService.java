package com.bappi.videoinventorymanagement.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFileToFileStorage(MultipartFile file);
}
