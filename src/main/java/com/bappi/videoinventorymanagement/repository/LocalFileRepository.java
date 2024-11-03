package com.bappi.videoinventorymanagement.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.InputStream;

@Slf4j
@Repository(value = "localFileRepository")
public class LocalFileRepository implements IFileRepository {

    @Override
    public Boolean saveFile(InputStream inputStream, String absolutePath) {
        try {
            File file = new File(absolutePath);
            file.getParentFile().mkdirs();
            FileUtils.copyInputStreamToFile(inputStream, file);
            log.info("[LocalFileRepository][saveFile]: file saving in " + absolutePath);
            return true;
        } catch (Exception exception) {
            log.warn("[LocalFileRepository][saveFile]: error!! "
                    + exception.getMessage());
        }
        return false;
    }

    @Override
    public File downloadFile(String absolutePath) throws Exception {
        return new File(absolutePath);
    }
}
