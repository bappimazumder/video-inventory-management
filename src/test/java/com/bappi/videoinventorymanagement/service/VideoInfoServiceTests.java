package com.bappi.videoinventorymanagement.service.impl;

import com.bappi.videoinventorymanagement.config.SecurityContextUtils;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoRequestDto;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoResponseDto;
import com.bappi.videoinventorymanagement.model.entity.UserInfo;
import com.bappi.videoinventorymanagement.model.entity.VideoInfo;
import com.bappi.videoinventorymanagement.repository.ActivityLogRepository;
import com.bappi.videoinventorymanagement.repository.UserInfoRepository;
import com.bappi.videoinventorymanagement.repository.VideoInfoRepository;
import com.bappi.videoinventorymanagement.service.FileService;
import com.bappi.videoinventorymanagement.utils.APIErrorCode;
import com.bappi.videoinventorymanagement.utils.ResponsePayload;
import com.bappi.videoinventorymanagement.utils.mapper.VideoInfoObjectMapper;
import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VideoInfoServiceTests {

    @Mock
    private VideoInfoRepository videoInfoRepository;

    @Mock
    private FileService fileService;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private ActivityLogRepository activityLogRepository;

    @Mock
    private VideoInfoObjectMapper videoInfoObjectMapper;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private VideoInfoServiceImpl videoInfoService;

    @Mock
    private Tika tika;

    @Mock
    private SecurityContextUtils securityContextUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveValidRequest() throws IOException {
        VideoInfoRequestDto requestDto = new VideoInfoRequestDto();
        requestDto.setTitle("Test Video");
        requestDto.setDescription("Test Description");
        requestDto.setUserId(1L);

        when(multipartFile.isEmpty()).thenReturn(false);
        when(fileService.uploadFileToFileStorage(multipartFile)).thenReturn("videoUrl");
        when(videoInfoRepository.save(any(VideoInfo.class))).thenReturn(new VideoInfo());
        when(userInfoRepository.findById(anyLong())).thenReturn(Optional.of(new UserInfo()));
        when(videoInfoObjectMapper.map(any(VideoInfo.class))).thenReturn(new VideoInfoResponseDto());

        VideoInfoResponseDto response = videoInfoService.save(requestDto, multipartFile);

        assertNotNull(response);
        assertEquals("Test Video", response.getTitle());
        assertNull(response.getMessage());
        assertEquals(APIErrorCode.INVALID_REQUEST, response.getErrorCode());
        verify(videoInfoRepository).save(any(VideoInfo.class));
    }

    @Test
    void testSaveInvalidFile() throws IOException {

        VideoInfoRequestDto requestDto = new VideoInfoRequestDto();
        requestDto.setTitle("Test Video");
        requestDto.setDescription("Test Description");

        when(multipartFile.isEmpty()).thenReturn(true);


        VideoInfoResponseDto response = videoInfoService.save(requestDto, multipartFile);

        assertNotNull(response);
        assertEquals("File can't be empty", response.getMessage());
        assertEquals(APIErrorCode.INVALID_REQUEST, response.getErrorCode());
    }

    @Test
    void testSaveInvalidFileFormat() throws IOException {

        VideoInfoRequestDto requestDto = new VideoInfoRequestDto();
        requestDto.setTitle("Test Video");
        requestDto.setDescription("Test Description");

        when(multipartFile.isEmpty()).thenReturn(false);
        when(fileService.uploadFileToFileStorage(multipartFile)).thenReturn("videoUrl");
        when(tika.detect(multipartFile.getInputStream())).thenReturn("image/jpeg");


        VideoInfoResponseDto response = videoInfoService.save(requestDto, multipartFile);

        assertNotNull(response);
        assertEquals("Invalid File Format", response.getMessage());
        assertEquals(APIErrorCode.INVALID_REQUEST, response.getErrorCode());
    }

    @Test
    void testGetVideosByUserAdmin() {
        when(securityContextUtils.getUserRoles()).thenReturn(Set.of("ROLE_ADMIN"));
        when(videoInfoRepository.findAll()).thenReturn(List.of(new VideoInfo()));

        ResponsePayload<VideoInfoResponseDto> response = videoInfoService.getVideosByUser();

        assertNotNull(response);
        assertFalse(response.getDataList().isEmpty());
    }

    @Test
    void testGetVideosByUserRegularUser() {
        when(securityContextUtils.getUserRoles()).thenReturn(Set.of("ROLE_USER"));
        when(securityContextUtils.getUserName()).thenReturn("user@example.com");
        when(userInfoRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserInfo()));
        when(videoInfoRepository.findByAssignedToUserId(anyLong())).thenReturn(List.of(new VideoInfo()));

        ResponsePayload<VideoInfoResponseDto> response = videoInfoService.getVideosByUser();

        assertNotNull(response);
        assertFalse(response.getDataList().isEmpty());
    }

    @Test
    void testUpdateValidRequest() {

        VideoInfoRequestDto requestDto = new VideoInfoRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setDescription("Updated Description");

        VideoInfo existingVideo = new VideoInfo();
        existingVideo.setId(1L);
        existingVideo.setTitle("Old Title");
        existingVideo.setDescription("Old Description");

        when(videoInfoRepository.findById(1L)).thenReturn(Optional.of(existingVideo));
        when(videoInfoRepository.save(any(VideoInfo.class))).thenReturn(existingVideo);

        VideoInfoResponseDto response = videoInfoService.update(1L, requestDto);

        assertNotNull(response);
        assertEquals("Updated Title", response.getTitle());
        assertEquals("Updated Description", response.getDescription());
    }

    @Test
    void testUpdateInvalidVideoId() {

        VideoInfoRequestDto requestDto = new VideoInfoRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setDescription("Updated Description");

        when(videoInfoRepository.findById(1L)).thenReturn(Optional.empty());

        VideoInfoResponseDto response = videoInfoService.update(1L, requestDto);

        assertNotNull(response);
        assertEquals("Invalid Video Id", response.getMessage());
        assertEquals(APIErrorCode.NO_RECORD_FOUND, response.getErrorCode());
    }

    @Test
    void testDeleteVideo() {
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setId(1L);

        when(videoInfoRepository.findById(1L)).thenReturn(Optional.of(videoInfo));

        Boolean result = videoInfoService.delete(1L);

        assertTrue(result);
        verify(videoInfoRepository).deleteById(1L);
    }

    @Test
    void testDeleteVideoNotFound() {
        when(videoInfoRepository.findById(1L)).thenReturn(Optional.empty());

        Boolean result = videoInfoService.delete(1L);

        assertFalse(result);
    }
}