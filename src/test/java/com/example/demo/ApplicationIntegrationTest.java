package com.example.demo;

import com.example.demo.model.DeviceInfo;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.service.DeviceAtlasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")  // use application-test.properties
class ApplicationIntegrationTest {

    @MockBean
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceAtlasService deviceAtlasService;

    @BeforeEach
    void setUp() {
        DeviceInfo mockDevice = new DeviceInfo();
        mockDevice.setModel("iPad Pro");
        mockDevice.setOsVersion("15.0");
        mockDevice.setVendor("Apple");
        mockDevice.setBrowserName("Safari");
        mockDevice.setOsName("iOS");
        mockDevice.setPrimaryHardwareType("Tablet");
        mockDevice.setBrowserRenderingEngine("WebKit");

        when(deviceRepository.findByPrimaryHardwareTypeOrderByOsVersionDesc("Tablet"))
                .thenReturn(List.of(mockDevice));
    }

    @Test
    void testGetTablets() {
        List<DeviceInfo> tablets = deviceAtlasService.getTablets();
        assertThat(tablets).isNotEmpty();
        assertThat(tablets.get(0).getModel()).isEqualTo("iPad Pro");
    }
}
