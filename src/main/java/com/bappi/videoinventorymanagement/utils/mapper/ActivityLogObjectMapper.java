package com.bappi.videoinventorymanagement.utils.mapper;

import com.bappi.videoinventorymanagement.model.dto.ActivityLogResponseDto;
import com.bappi.videoinventorymanagement.model.entity.ActivityLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ActivityLogObjectMapper {
    @Mapping(source = "videoInfo.id", target = "videoId")
    @Mapping(source = "user.id", target = "userId")
    ActivityLogResponseDto map(ActivityLog obj);
    List<ActivityLogResponseDto> map(List<ActivityLog> list);
}
