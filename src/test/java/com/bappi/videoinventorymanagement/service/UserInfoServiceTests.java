package com.bappi.videoinventorymanagement.service;

import com.bappi.videoinventorymanagement.model.dto.UserInfoRequestDto;
import com.bappi.videoinventorymanagement.model.entity.UserInfo;
import com.bappi.videoinventorymanagement.repository.UserInfoRepository;
import com.bappi.videoinventorymanagement.service.impl.UserInfoServiceImpl;
import com.bappi.videoinventorymanagement.utils.mapper.UserInfoObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserInfoServiceTests {
    private static final String MSG_USER_CREATED_SUCCESSFULLY = "User Created Successfully";
    @InjectMocks
    private UserInfoServiceImpl userInfoService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private UserInfoObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks
    }

    @Test
    void testGetById_whenUserExists() {
        Long userId = 1L;
        UserInfo mockUser = new UserInfo();
        mockUser.setId(userId);
        mockUser.setEmail("testUser");

        when(userInfoRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        Optional<UserInfo> result = userInfoService.getById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        assertEquals("testUser", result.get().getEmail());
    }

    @Test
    void testGetById_whenUserDoesNotExist() {
        Long userId = 1L;
        when(userInfoRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<UserInfo> result = userInfoService.getById(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void testAddUser() {
        UserInfoRequestDto requestDto = new UserInfoRequestDto();
        requestDto.setEmail("testUser");
        requestDto.setPassword("password123");

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("testUser");
        userInfo.setPassword("encodedPassword");

        when(objectMapper.map(requestDto)).thenReturn(userInfo);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        when(userInfoRepository.save(userInfo)).thenReturn(userInfo);

        String response = userInfoService.addUser(requestDto);

        assertEquals(MSG_USER_CREATED_SUCCESSFULLY, response);

        verify(objectMapper).map(requestDto);
        verify(passwordEncoder).encode("password123");
        verify(userInfoRepository).save(userInfo);
    }

}
