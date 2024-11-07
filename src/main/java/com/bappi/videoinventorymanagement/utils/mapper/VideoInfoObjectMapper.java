package com.bappi.videoinventorymanagement.utils.mapper;

import com.bappi.videoinventorymanagement.model.dto.VideoInfoRequestDto;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoResponseDto;
import com.bappi.videoinventorymanagement.model.entity.VideoInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface VideoInfoObjectMapper {
    @Mapping(source = "assignedToUser.id", target = "userId")
    @Mapping(source = "assignedToUser.email", target = "userName")
    VideoInfoResponseDto map(VideoInfo obj);
    List<VideoInfoResponseDto> map(List<VideoInfo> list);
    VideoInfo map(VideoInfoRequestDto requestDto);

}
