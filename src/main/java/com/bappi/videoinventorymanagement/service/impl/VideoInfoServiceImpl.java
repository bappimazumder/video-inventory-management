package com.bappi.videoinventorymanagement.service.impl;

import com.bappi.videoinventorymanagement.model.dto.VideoInfoRequestDto;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoResponseDto;
import com.bappi.videoinventorymanagement.model.entity.UserInfo;
import com.bappi.videoinventorymanagement.model.entity.VideoInfo;
import com.bappi.videoinventorymanagement.repository.UserInfoRepository;
import com.bappi.videoinventorymanagement.repository.VideoInfoRepository;
import com.bappi.videoinventorymanagement.service.FileService;
import com.bappi.videoinventorymanagement.service.UserInfoService;
import com.bappi.videoinventorymanagement.service.VideoInfoService;
import com.bappi.videoinventorymanagement.utils.APIErrorCode;
import com.bappi.videoinventorymanagement.utils.CustomException;
import com.bappi.videoinventorymanagement.utils.ResponsePayload;
import com.bappi.videoinventorymanagement.utils.mapper.VideoInfoObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.bappi.videoinventorymanagement.config.Constant.IMAGE_MAX_FILE_SIZE;

@Slf4j
@Service
public class VideoInfoServiceImpl implements VideoInfoService {

    private final VideoInfoRepository repository;
    private final UserInfoService userInfoService;
    private final FileService fileService;
    private final UserInfoRepository userDetailsRepository;

    private final VideoInfoObjectMapper objectMapper;

    public VideoInfoServiceImpl(VideoInfoRepository repository, UserInfoService userInfoService, FileService fileService, UserInfoRepository userDetailsRepository) {
        this.repository = repository;
        this.userInfoService = userInfoService;
        this.fileService = fileService;
        this.userDetailsRepository = userDetailsRepository;
        this.objectMapper = Mappers.getMapper(VideoInfoObjectMapper.class);
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

        Optional<UserInfo> userDetails = userInfoService.getById(requestDto.getUserId());

        String videoUrlLocation = fileService.uploadFileToFileStorage(file);

        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setTitle(requestDto.getTitle());
        videoInfo.setDescription(requestDto.getDescription());
        videoInfo.setVideoUrl(videoUrlLocation);
        userDetails.ifPresent(videoInfo::setAssignedToUser);

        repository.save(videoInfo);

        return null;
    }

    @Override
    public ResponsePayload<VideoInfoResponseDto> getVideosByUser() {
        List<VideoInfo> videos = repository.findByAssignedToUserId(1L);
        return ResponsePayload.<VideoInfoResponseDto>builder()
                .dataList(objectMapper.map(videos)).build();
    }

    @Override
    public VideoInfoResponseDto update(Long id ,VideoInfoRequestDto requestDto) {
        Optional<VideoInfo> existingObj = repository.findById(id);
        if(existingObj.isPresent()){
            VideoInfo videoInfo =  existingObj.get();
            videoInfo.setDescription(requestDto.getDescription());
            videoInfo.setTitle(requestDto.getTitle());
            if(requestDto.getUserId() != null){
                Optional<UserInfo> userDetails = userDetailsRepository.findById(requestDto.getUserId());
                userDetails.ifPresent(videoInfo::setAssignedToUser);
            }
            videoInfo = repository.save(videoInfo);
            return objectMapper.map(videoInfo);
        }
        return new VideoInfoResponseDto();

    }

    @Override
    public Boolean delete(Long id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public VideoInfoResponseDto assignVideo(Long videoId, Long userId) {

        Optional<VideoInfo> videoInfo = repository.findById(videoId);
        Optional<UserInfo> userDetails = userDetailsRepository.findById(userId);
        if (videoInfo.isPresent() && userDetails.isPresent()){
            VideoInfo video = videoInfo.get();
            video.setAssignedToUser(userDetails.get());
            video = repository.save(video);
            return objectMapper.map(video);
        }
        return null;
    }
}
