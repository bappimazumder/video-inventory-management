package com.bappi.videoinventorymanagement.service;

import com.bappi.videoinventorymanagement.model.dto.VideoInfoRequestDto;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface VideoInfoService {

    VideoInfoResponseDto save(VideoInfoRequestDto requestDto, MultipartFile file);

}
