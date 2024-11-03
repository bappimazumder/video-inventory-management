package com.bappi.videoinventorymanagement.service.impl;

import com.bappi.videoinventorymanagement.model.dto.VideoInfoRequestDto;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoResponseDto;
import com.bappi.videoinventorymanagement.model.entity.UserDetails;
import com.bappi.videoinventorymanagement.model.entity.VideoInfo;
import com.bappi.videoinventorymanagement.repository.VideoInfoRepository;
import com.bappi.videoinventorymanagement.service.FileService;
import com.bappi.videoinventorymanagement.service.UserDetailsService;
import com.bappi.videoinventorymanagement.service.VideoInfoService;
import com.bappi.videoinventorymanagement.utils.APIErrorCode;
import com.bappi.videoinventorymanagement.utils.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.bappi.videoinventorymanagement.config.Constant.IMAGE_MAX_FILE_SIZE;

@Slf4j
@Service
public class VideoInfoServiceImpl implements VideoInfoService {

    private final VideoInfoRepository repository;
    private final UserDetailsService userDetailsService;
    private final FileService fileService;

    public VideoInfoServiceImpl(VideoInfoRepository repository, UserDetailsService userDetailsService, FileService fileService) {
        this.repository = repository;
        this.userDetailsService = userDetailsService;
        this.fileService = fileService;
    }

    @Override
    public VideoInfoResponseDto save(VideoInfoRequestDto requestDto, MultipartFile file) {

        // Uploaded file validation
        if(file.isEmpty()){
            log.error(" File is empty ");
            throw new CustomException(APIErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }
        if (file.getSize() > IMAGE_MAX_FILE_SIZE) {
            log.error("Uploaded file is too large");
            throw new CustomException(APIErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = userDetailsService.getByCode(requestDto.getUserCode());

        String videoUrlLocation = fileService.uploadFileToFileStorage(file);

        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setTitle(requestDto.getTitle());
        videoInfo.setDescription(requestDto.getDescription());
        videoInfo.setVideoUrl(videoUrlLocation);
        videoInfo.setAssignedToUser(userDetails);

        repository.save(videoInfo);



        return null;
    }
}
