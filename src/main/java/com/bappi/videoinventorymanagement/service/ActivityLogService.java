package com.bappi.videoinventorymanagement.service;

import com.bappi.videoinventorymanagement.model.dto.ActivityLogResponseDto;
import com.bappi.videoinventorymanagement.model.dto.VideoInfoResponseDto;
import com.bappi.videoinventorymanagement.utils.ResponsePayload;

public interface ActivityLogService {

    ResponsePayload<ActivityLogResponseDto> getAll();

}
