package com.bappi.videoinventorymanagement.service;

import com.bappi.videoinventorymanagement.model.dto.ActivityLogResponseDto;
import com.bappi.videoinventorymanagement.model.entity.ActivityLog;
import com.bappi.videoinventorymanagement.repository.ActivityLogRepository;
import com.bappi.videoinventorymanagement.service.impl.ActivityLogServiceImpl;
import com.bappi.videoinventorymanagement.utils.ResponsePayload;
import com.bappi.videoinventorymanagement.utils.mapper.ActivityLogObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ActivityLogServiceTests {
    @InjectMocks
    private ActivityLogServiceImpl activityLogService;

    @Mock
    private ActivityLogRepository activityLogRepository;

    @Mock
    private ActivityLogObjectMapper activityLogObjectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        // Arrange: Create mock data
        ActivityLog activityLog1 = new ActivityLog();
        activityLog1.setActionType("SAVE");
        ActivityLog activityLog2 = new ActivityLog();
        activityLog2.setActionType("DELETE");

        List<ActivityLog> activityLogs = Arrays.asList(activityLog1, activityLog2);

        when(activityLogRepository.findAll()).thenReturn(activityLogs);

        ActivityLogResponseDto responseDto1 = new ActivityLogResponseDto();
        responseDto1.setActionType("SAVE");

        ActivityLogResponseDto responseDto2 = new ActivityLogResponseDto();
        responseDto2.setActionType("DELETE");

        when(activityLogObjectMapper.map(activityLogs)).thenReturn(Arrays.asList(responseDto1, responseDto2));

        ResponsePayload<ActivityLogResponseDto> response = activityLogService.getAll();

        assertNotNull(response);
        assertNotNull(response.getDataList());
        assertEquals(2, response.getDataList().size());
        assertEquals("SAVE", response.getDataList().get(0).getActionType());
        assertEquals("DELETE", response.getDataList().get(1).getActionType());

        verify(activityLogRepository).findAll();
        verify(activityLogObjectMapper).map(activityLogs);
    }
}
