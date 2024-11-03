package com.bappi.videoinventorymanagement.repository;

import java.io.File;
import java.io.InputStream;

public interface IFileRepository {

    Boolean saveFile(InputStream inputStream, String absolutePath);

    File downloadFile(String absolutePath) throws Exception;
}
