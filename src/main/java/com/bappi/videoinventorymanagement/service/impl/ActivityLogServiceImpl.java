package com.bappi.videoinventorymanagement.service.impl;

import com.bappi.videoinventorymanagement.model.dto.ActivityLogResponseDto;
import com.bappi.videoinventorymanagement.model.entity.ActivityLog;
import com.bappi.videoinventorymanagement.repository.ActivityLogRepository;
import com.bappi.videoinventorymanagement.service.ActivityLogService;
import com.bappi.videoinventorymanagement.utils.ResponsePayload;
import com.bappi.videoinventorymanagement.utils.mapper.ActivityLogObjectMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository repository;
    private final ActivityLogObjectMapper objectMapper;
    public ActivityLogServiceImpl(ActivityLogRepository repository) {
        this.repository = repository;
        this.objectMapper = Mappers.getMapper(ActivityLogObjectMapper.class);
    }

    @Override
    public ResponsePayload<ActivityLogResponseDto> getAll() {
        List<ActivityLog> list = repository.findAll();
        return ResponsePayload.<ActivityLogResponseDto>builder()
                .dataList(objectMapper.map(list)).build();
    }
}
