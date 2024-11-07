package com.bappi.videoinventorymanagement.controller;

import com.bappi.videoinventorymanagement.config.ApiPath;
import com.bappi.videoinventorymanagement.model.dto.ActivityLogResponseDto;
import com.bappi.videoinventorymanagement.service.ActivityLogService;
import com.bappi.videoinventorymanagement.utils.ResponsePayload;
import com.bappi.videoinventorymanagement.utils.ServiceExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bappi.videoinventorymanagement.config.ApiPath.API_ACTIVITY_LOG;

@Slf4j
@RestController
@RequestMapping(API_ACTIVITY_LOG)
public class ActivityLogController {

    private final ActivityLogService service;

    public ActivityLogController(ActivityLogService service) {
        this.service = service;
    }

    @GetMapping(value = ApiPath.API_GET_ALL_ACTIVITY_LOG)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public HttpEntity<ResponsePayload<ActivityLogResponseDto>> getAllCategoryList(){
        log.info("Get All activity Data: ");
        ServiceExceptionHandler<ResponsePayload<ActivityLogResponseDto>> dataHandler = () -> service.getAll();
        ResponsePayload<ActivityLogResponseDto> responsePayload = dataHandler.executeHandler();
        return new ResponseEntity<>(responsePayload, HttpStatus.OK);
    }

}
