package com.example.demo.service;

import static org.mockito.Mockito.*;

import com.example.demo.model.DeviceInfo;
import com.example.demo.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

class DeviceAtlasServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceAtlasService deviceAtlasService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchAndStoreDeviceInfo_NewDevice() {
        String userAgent = "TestUserAgent";

        Map<String, Object> mockResponse = Map.of(
            "properties", Map.of(
                "primaryHardwareType", "Tablet",
                "model", "iPad Pro",
                "osVersion", "15.0",
                "vendor", "Apple",
                "browserName", "Safari",
                "osName", "iOS",
                "browserRenderingEngine", "WebKit"
            )
        );

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockResponse);
        when(deviceRepository.findByModelAndOsVersionAndVendorAndBrowserNameAndOsNameAndPrimaryHardwareTypeAndBrowserRenderingEngine(
            any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(Optional.empty());

        deviceAtlasService.fetchAndStoreDeviceInfo(userAgent);

        verify(deviceRepository, times(1)).save(any(DeviceInfo.class));
    }

    @Test
    void testFetchAndStoreDeviceInfo_ExistingDevice() {
        String userAgent = "TestUserAgent";

        DeviceInfo existingDevice = new DeviceInfo();
        existingDevice.setModel("iPad Pro");
        existingDevice.setOsVersion("15.0");

        when(deviceRepository.findByModelAndOsVersionAndVendorAndBrowserNameAndOsNameAndPrimaryHardwareTypeAndBrowserRenderingEngine(
            any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(Optional.of(existingDevice));

        deviceAtlasService.fetchAndStoreDeviceInfo(userAgent);

        verify(deviceRepository, never()).save(any(DeviceInfo.class));
    }
}
