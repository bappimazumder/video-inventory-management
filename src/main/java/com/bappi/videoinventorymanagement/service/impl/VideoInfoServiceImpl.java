package com.bappi.videoinventorymanagement.service.impl;

import com.bappi.videoinventorymanagement.config.SecurityContextUtils;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoRequestDto;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoResponseDto;
import com.bappi.videoinventorymanagement.model.entity.ActivityLog;
import com.bappi.videoinventorymanagement.model.entity.UserInfo;
import com.bappi.videoinventorymanagement.model.entity.VideoInfo;
import com.bappi.videoinventorymanagement.repository.ActivityLogRepository;
import com.bappi.videoinventorymanagement.repository.UserInfoRepository;
import com.bappi.videoinventorymanagement.repository.VideoInfoRepository;
import com.bappi.videoinventorymanagement.service.FileService;
import com.bappi.videoinventorymanagement.service.VideoInfoService;
import com.bappi.videoinventorymanagement.utils.APIErrorCode;
import com.bappi.videoinventorymanagement.utils.ResponsePayload;
import com.bappi.videoinventorymanagement.utils.mapper.VideoInfoObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static com.bappi.videoinventorymanagement.config.Constant.*;

@Slf4j
@Service
public class VideoInfoServiceImpl implements VideoInfoService {

    private final String VIDEO_FORMAT = "Video";
    private static final long VEDIO_MAX_FILE_SIZE = 500 * 1024 * 1024;
    private final VideoInfoRepository repository;
    private final FileService fileService;
    private final UserInfoRepository userInfoRepository;
    private final ActivityLogRepository activityLogRepository;


    private final VideoInfoObjectMapper objectMapper;

    public VideoInfoServiceImpl(VideoInfoRepository repository, FileService fileService, UserInfoRepository userInfoRepository, ActivityLogRepository activityLogRepository) {
        this.repository = repository;
        this.fileService = fileService;
        this.userInfoRepository = userInfoRepository;
        this.activityLogRepository = activityLogRepository;
        this.objectMapper = Mappers.getMapper(VideoInfoObjectMapper.class);
    }

    @Override
    @Transactional
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
            Optional<UserInfo> userDetails = userInfoRepository.findById(requestDto.getUserId());
            if (userDetails.isPresent()){
                UserInfo user = userDetails.get();
                if (user.getRoles() != null && user.getRoles().equals(ROLE_USER)) {
                    videoInfo.setAssignedToUser(user);
                }
            }
        }

        VideoInfo savedObj = repository.save(videoInfo);

        // Activity Log service Save
        saveActivityLog(savedObj,ACTION_TYPE_SAVE);

        return objectMapper.map(savedObj);
    }



    @Override
    public ResponsePayload<VideoInfoResponseDto> getVideosByUser() {
        List<VideoInfo> videos = new ArrayList<>();
        Set<String> userRoles =  SecurityContextUtils.getUserRoles();
        if(userRoles.contains(ROLE_ADMIN)){
            videos = repository.findAll();
        }else{
            Optional<UserInfo> userInfo =  userInfoRepository.findByEmail(SecurityContextUtils.getUserName());
            if (userInfo.isPresent()){
                videos = repository.findByAssignedToUserId(userInfo.get().getId());
            }
        }

        return ResponsePayload.<VideoInfoResponseDto>builder()
                .dataList(objectMapper.map(videos)).build();
    }

    @Override
    @Transactional
    public VideoInfoResponseDto update(Long id ,VideoInfoRequestDto requestDto) {
        Optional<VideoInfo> existingObj = repository.findById(id);
        if(existingObj.isPresent()){
            VideoInfo videoInfo =  existingObj.get();
            videoInfo.setDescription(requestDto.getDescription());
            videoInfo.setTitle(requestDto.getTitle());
            if(requestDto.getUserId() != null){
                Optional<UserInfo> userDetails = userInfoRepository.findById(requestDto.getUserId());
                userDetails.ifPresent(videoInfo::setAssignedToUser);
            }
            videoInfo = repository.save(videoInfo);

            // Activity Log service Update
            saveActivityLog(videoInfo,ACTION_TYPE_UPDATE);

            return objectMapper.map(videoInfo);
        }else {
            VideoInfoResponseDto responseDto = new VideoInfoResponseDto();
            responseDto.setErrorCode(APIErrorCode.NO_RECORD_FOUND);
            responseDto.setMessage("Invalid Video Id");
            return responseDto;
        }

    }

    @Override
    @Transactional
    public Boolean delete(Long id) {
        Optional<VideoInfo> videoInfo = repository.findById(id);
        if (videoInfo.isPresent()){
            // Activity Log service Update
            saveActivityLog(videoInfo.get(),ACTION_TYPE_DELETE);
            repository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public VideoInfoResponseDto assignVideo(Long videoId, Long userId) {

        Optional<VideoInfo> videoInfo = repository.findById(videoId);
        Optional<UserInfo> userDetails = userInfoRepository.findById(userId);
        if (videoInfo.isPresent() && userDetails.isPresent()){
            VideoInfo video = videoInfo.get();
            video.setAssignedToUser(userDetails.get());
            video = repository.save(video);
            // Activity Log service Update
            saveActivityLog(video,ACTION_TYPE_UPDATE);

            return objectMapper.map(video);
        }else {
            VideoInfoResponseDto responseDto = new VideoInfoResponseDto();
            responseDto.setErrorCode(APIErrorCode.INVALID_REQUEST);
            responseDto.setMessage("Invalid Data Found");
            return responseDto;
        }
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
        }else if(mimeType.startsWith("application/octet-stream")){
            return "Video";
        }else {
            return "Unknown";
        }
    }

    private void saveActivityLog(VideoInfo savedObj,String actionType) {
        ActivityLog activityLog = new ActivityLog();
        Optional<UserInfo> userInfo = userInfoRepository.findByEmail(SecurityContextUtils.getUserName());
        userInfo.ifPresent(activityLog::setUser);
        activityLog.setVideoInfo(savedObj);
        activityLog.setActivityAt(new Timestamp(System.currentTimeMillis()));
        activityLog.setActionType(actionType);
        activityLogRepository.save(activityLog);
    }

}
