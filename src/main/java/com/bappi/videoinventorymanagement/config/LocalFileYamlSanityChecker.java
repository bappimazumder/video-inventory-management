package com.bappi.videoinventorymanagement.config;

import java.io.File;

public class LocalFileYamlSanityChecker {

    public void checkValidity(String fileDir) {
        if (fileDir == null || fileDir.isEmpty() || !(new File(fileDir).exists())) {
            throw new AssertionError("Local file upload directory " + fileDir +
                    " in yaml is not found, please check file.upload-dir field.");
        }
    }
}
