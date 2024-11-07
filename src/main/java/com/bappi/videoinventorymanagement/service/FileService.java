package com.bappi.videoinventorymanagement.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

public interface FileService {

    String uploadFileToFileStorage(MultipartFile file);

    FileInputStream downloadFileFromFileStorage(String fileToDownLoad);

    HttpHeaders getHeader(String fileName);

    MediaType getContentType(String file);
}
