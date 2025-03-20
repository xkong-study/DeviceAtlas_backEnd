package com.example.demo.repository;

import com.example.demo.model.DeviceInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceRepositoryTest {

    @Mock  // mock Repository
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceInfo testDevice;

    @BeforeEach
    void setUp() {
        testDevice = new DeviceInfo();
        testDevice.setId(1L);
        testDevice.setModel("Pixel C");
        testDevice.setOsVersion("6.0");
        testDevice.setVendor("Google");
        testDevice.setBrowserName("Chrome");
        testDevice.setOsName("Android");
        testDevice.setPrimaryHardwareType("Tablet");
        testDevice.setBrowserRenderingEngine("Blink");

        when(deviceRepository.findAll()).thenReturn(List.of(testDevice));
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));
    }

    @Test
    void testMockRepository() {
        List<DeviceInfo> allDevices = deviceRepository.findAll();

        Optional<DeviceInfo> foundDevice = deviceRepository.findById(1L);
        assertThat(foundDevice).isPresent();
        assertThat(foundDevice.get().getModel()).isEqualTo("Pixel C");
    }
}
