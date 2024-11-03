package com.bappi.videoinventorymanagement.controller;

import com.bappi.videoinventorymanagement.config.ApiPath;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoResponseDto;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoRequestDto;
import com.bappi.videoinventorymanagement.service.VideoInfoService;
import com.bappi.videoinventorymanagement.service.impl.VideoInfoServiceImpl;
import com.bappi.videoinventorymanagement.utils.ServiceExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.bappi.videoinventorymanagement.config.ApiPath.API_VIDEO;

@Slf4j
@RestController
@RequestMapping(API_VIDEO)
public class VideoInfoController {


    private final VideoInfoService service;

    @Autowired
    public VideoInfoController(VideoInfoServiceImpl videoInfoServiceImpl) {
        this.service = videoInfoServiceImpl;
    }

    @RequestMapping(value = ApiPath.API_POST_SAVE_VIDEO, method = { RequestMethod.POST },consumes = {MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseBody
    public HttpEntity<?> saveDocument(@RequestPart("data") VideoInfoRequestDto requestDto, @RequestPart("file") MultipartFile file) {
        log.info( "Save Video for title: " + requestDto.getTitle() + " description " +requestDto.getDescription());

        ServiceExceptionHandler<VideoInfoResponseDto> dataHandler = () -> service.save(requestDto,file);

        return new ResponseEntity<>(dataHandler.executeHandler(), HttpStatus.OK);
    }
}
