package com.bappi.videoinventorymanagement.service.impl;

import com.bappi.videoinventorymanagement.config.SecurityContextUtils;
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
import org.apache.tika.Tika;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.bappi.videoinventorymanagement.config.Constant.*;

@Slf4j
@Service
public class VideoInfoServiceImpl implements VideoInfoService {

    private final String VIDEO_FORMAT = "Video";
    private static final long VEDIO_MAX_FILE_SIZE = 500 * 1024 * 1024;
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
    public VideoInfoResponseDto save(VideoInfoRequestDto requestDto, MultipartFile file){
        VideoInfoResponseDto responseDto = new VideoInfoResponseDto();
        if (requestDto.getTitle() == null || requestDto.getTitle().isEmpty()) {
            responseDto.setMessage("Title can't be empty");
            responseDto.setErrorCode(APIErrorCode.INVALID_REQUEST);
            return responseDto;
            //throw new CustomException(APIErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }
        if (requestDto.getDescription() == null || requestDto.getDescription().isEmpty()) {
            responseDto.setMessage("Description can't be empty");
            responseDto.setErrorCode(APIErrorCode.INVALID_REQUEST);
            return responseDto;
           // throw new CustomException(APIErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }
        if(file.isEmpty()){
            responseDto.setMessage("File can't be empty");
            responseDto.setErrorCode(APIErrorCode.INVALID_REQUEST);
            return responseDto;
            // throw new CustomException(APIErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }

        String fileType = null;
        try {
            fileType = getFileType(file);
        } catch (IOException e) {
            log.error("Invalid File format");
            responseDto.setMessage("Invalid File Format");
            responseDto.setErrorCode(APIErrorCode.INVALID_REQUEST);
            return responseDto;
            // throw new RuntimeException(e);
        }
        if (!fileType.equals(VIDEO_FORMAT)) {
            log.error("Invalid File format");
            responseDto.setMessage("Invalid File Format");
            responseDto.setErrorCode(APIErrorCode.INVALID_REQUEST);
            return responseDto;
            // throw new CustomException(APIErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }
        if (file.getSize() > VEDIO_MAX_FILE_SIZE) {
            log.error("Invalid File size");
            responseDto.setMessage("Invalid File size");
            responseDto.setErrorCode(APIErrorCode.INVALID_REQUEST);
            return responseDto;
            // throw new CustomException(APIErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }


        String videoUrlLocation = fileService.uploadFileToFileStorage(file);

        VideoInfo videoInfo = new VideoInfo();
        int uniqueId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        String code = VIDEO_CODE_PREFIX + uniqueId;
        videoInfo.setCode(code);
        videoInfo.setTitle(requestDto.getTitle());
        videoInfo.setDescription(requestDto.getDescription());
        videoInfo.setVideoUrl(videoUrlLocation);

        if(requestDto.getUserId() != null && requestDto.getUserId() > 0){
            Optional<UserInfo> userDetails = userInfoService.getById(requestDto.getUserId());
            if (userDetails.isPresent() && userDetails.get().getRoles().equals(ROLE_USER)){
                videoInfo.setAssignedToUser(userDetails.get());
            }
        }

        VideoInfo savedObj = repository.save(videoInfo);

        return objectMapper.map(savedObj);
    }

    @Override
    public ResponsePayload<VideoInfoResponseDto> getVideosByUser() {
        List<VideoInfo> videos = new ArrayList<>();
        Set<String> userRoles =  SecurityContextUtils.getUserRoles();
        if(userRoles.contains(ROLE_ADMIN)){
            videos = repository.findAll();
        }else{
            Optional<UserInfo> userInfo =  userDetailsRepository.findByEmail(SecurityContextUtils.getUserName());
            if (userInfo.isPresent()){
                videos = repository.findByAssignedToUserId(userInfo.get().getId());
            }
        }

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

    public String getFileType(MultipartFile file) throws IOException {
        Tika tika = new Tika();

        // Detect the MIME type of the file
        String mimeType = tika.detect(file.getInputStream());

        if (mimeType.startsWith("image")) {
            return "Image";
        } else if (mimeType.startsWith("video")) {
            return "Video";
        } else if (mimeType.startsWith("application/pdf") || mimeType.startsWith("text") || mimeType.startsWith("application/msword")) {
            return "Document";
        } else {
            return "Unknown";
        }
    }
}
