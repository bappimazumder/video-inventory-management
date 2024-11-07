package com.bappi.videoinventorymanagement.controller;

import com.bappi.videoinventorymanagement.config.ApiPath;
import com.bappi.videoinventorymanagement.config.SecurityContextUtils;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoRequestDto;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoResponseDto;
import com.bappi.videoinventorymanagement.service.UserInfoService;
import com.bappi.videoinventorymanagement.service.VideoInfoService;
import com.bappi.videoinventorymanagement.service.impl.VideoInfoServiceImpl;
import com.bappi.videoinventorymanagement.utils.APIErrorCode;
import com.bappi.videoinventorymanagement.utils.CustomException;
import com.bappi.videoinventorymanagement.utils.ResponsePayload;
import com.bappi.videoinventorymanagement.utils.ServiceExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import static com.bappi.videoinventorymanagement.config.ApiPath.*;

@Slf4j
@RestController
@RequestMapping(API_VIDEO)
public class VideoInfoController {



    private final VideoInfoService service;
    private final UserInfoService userInfoService;

    @Autowired
    public VideoInfoController(VideoInfoServiceImpl videoInfoServiceImpl, UserInfoService userInfoService) {
        this.service = videoInfoServiceImpl;
        this.userInfoService = userInfoService;
    }

    @PostMapping(value = ApiPath.API_POST_SAVE_VIDEO,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public HttpEntity<?> saveVideo(@RequestParam("title") String title,@RequestParam("description") String description,
                                   @RequestParam("assignUserId")Long userId, @RequestParam("file") MultipartFile file){
     log.info( "Call Save Video for title: " + title + " description " + description);

        VideoInfoRequestDto requestDto = new VideoInfoRequestDto();
        requestDto.setTitle(title);
        requestDto.setDescription(description);
        requestDto.setUserId(userId);

        ServiceExceptionHandler<VideoInfoResponseDto> dataHandler = () -> service.save(requestDto,file);

        return new ResponseEntity<>(dataHandler.executeHandler(), HttpStatus.OK);
    }

    @GetMapping(value=API_GET_VIDEOS)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public HttpEntity<ResponsePayload<VideoInfoResponseDto>> getVideos() {

        log.info("Call Start getVideos() method for user {}",1);
        ResponsePayload<VideoInfoResponseDto> dto = null;

        ServiceExceptionHandler<ResponsePayload<VideoInfoResponseDto>> dataHandler = service::getVideosByUser;
        dto = dataHandler.executeHandler();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value=API_PUT_UPDATE_VIDEO+"/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public HttpEntity<?> updateVideo(@PathVariable Long id,@RequestBody VideoInfoRequestDto requestDto) {

        log.info("Call Start update video details method ");

        ServiceExceptionHandler<VideoInfoResponseDto> dataHandler = () -> service.update(id,requestDto);
        VideoInfoResponseDto dto =  dataHandler.executeHandler();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping(value=API_DELETE_VIDEO+"/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public HttpEntity<?> deleteVideo(@PathVariable Long id) {

        log.info("Call Start update video details method ");
        ResponsePayload<VideoInfoResponseDto> dto = null;

        ServiceExceptionHandler<Boolean> dataHandler = () -> service.delete(id);
        Boolean result = dataHandler.executeHandler();
        String message = "DELETE FAILED";
        if (result){
            message = "DELETE SUCCESSFULLY";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping(value=API_PUT_ASSIGN_USER+"/{videoId}"+"/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public HttpEntity<?> assignVideo(@PathVariable Long videoId,@PathVariable Long userId) {

        log.info("Call assign video");
        VideoInfoResponseDto dto = null;

        ServiceExceptionHandler<VideoInfoResponseDto> dataHandler = () -> service.assignVideo(videoId,userId);
        dto = dataHandler.executeHandler();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }



}
