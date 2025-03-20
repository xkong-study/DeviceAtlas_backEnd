package com.example.demo.controller;

import com.example.demo.service.DeviceAtlasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserAgentControllerTest {

    @Mock
    private DeviceAtlasService deviceAtlasService;

    @InjectMocks
    private UserAgentController userAgentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userAgentController).build();
    }

    @Test
    void testGetTablets_Success() throws Exception {
        when(deviceAtlasService.getTablets()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tablets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetTablets_ServiceThrowsException() throws Exception {
        doThrow(new RuntimeException("Service error")).when(deviceAtlasService).getTablets();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tablets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Service error"));
    }

    @Test
    void testFetchDevices_Success() throws Exception {
        String requestBody = "[\"Mozilla/5.0\", \"Safari/537.36\"]";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/fetch-devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Devices fetched and stored successfully"));

        verify(deviceAtlasService, times(2)).fetchAndStoreDeviceInfo(anyString());
    }

    @Test
    void testFetchDevices_EmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/fetch-devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No User-Agents provided!"));
    }

    @Test
    void testFetchDevices_ServiceThrowsException() throws Exception {
        doThrow(new RuntimeException("Fetching error")).when(deviceAtlasService).fetchAndStoreDeviceInfo(anyString());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/fetch-devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"Mozilla/5.0\"]"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Fetching error"));
    }
}
