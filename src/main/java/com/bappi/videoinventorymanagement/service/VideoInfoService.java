package com.bappi.videoinventorymanagement.service;

import com.bappi.videoinventorymanagement.model.dto.VideoInfoRequestDto;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoResponseDto;
import com.bappi.videoinventorymanagement.utils.ResponsePayload;
import org.springframework.web.multipart.MultipartFile;

public interface VideoInfoService {

    VideoInfoResponseDto save(VideoInfoRequestDto requestDto, MultipartFile file);
    ResponsePayload<VideoInfoResponseDto> getVideosByUser();
    VideoInfoResponseDto update(Long id ,VideoInfoRequestDto requestDto);
    Boolean delete(Long id);
    VideoInfoResponseDto assignVideo(Long videoId,Long userId);
}
