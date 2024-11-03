package com.bappi.videoinventorymanagement.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class YamlValuesLoader implements InitializingBean {

    @Value("${file.upload-dir}")
    private String localFileUploadDir;

    private LocalFileYamlSanityChecker localFileYamlSanityChecker = new LocalFileYamlSanityChecker();

    @Override
    public void afterPropertiesSet() {
        localFileYamlSanityChecker.checkValidity(localFileUploadDir);
    }
}
