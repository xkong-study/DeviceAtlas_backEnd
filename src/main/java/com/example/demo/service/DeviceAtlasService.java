package com.example.demo.service;

import com.example.demo.model.DeviceInfo;
import com.example.demo.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.ConcurrentHashMap;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.List;
import java.util.Map;

@Service
public class DeviceAtlasService {
    private final RestTemplate restTemplate;
    private final DeviceRepository deviceRepository;
    private final ConcurrentHashMap<String, Boolean> deviceCache = new ConcurrentHashMap<>(); // ✅ 缓存已处理的 `userAgent`

    @Value("${deviceatlas.api.url}")
    private String apiUrl;

    @Value("${deviceatlas.api.key}")
    private String apiKey;

    @Autowired
    public DeviceAtlasService(RestTemplate restTemplate, DeviceRepository deviceRepository) {
        this.restTemplate = restTemplate;
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    public void fetchAndStoreDeviceInfo(String userAgent) {
        if (deviceCache.containsKey(userAgent)) {
            System.out.println("jump userAgent: " + userAgent);
            return;
        }

        try {
            String encodedUserAgent = URLEncoder.encode(userAgent, StandardCharsets.UTF_8);
            String url = apiUrl + "?licencekey=" + apiKey + "&useragent=" + encodedUserAgent;

            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("properties")) {
                Map<String, Object> properties = (Map<String, Object>) response.get("properties");

                String hardwareType = (String) properties.get("primaryHardwareType");
                if (!"Tablet".equalsIgnoreCase(hardwareType)) return;

                String model = (String) properties.get("model");
                String osVersion = (String) properties.get("osVersion");
                String vendor = (String) properties.get("vendor");
                String browserName = (String) properties.get("browserName");
                String osName = (String) properties.get("osName");
                String browserRenderingEngine = (String) properties.get("browserRenderingEngine");

                Optional<DeviceInfo> existingDevice = deviceRepository.findByModelAndOsVersionAndVendorAndBrowserNameAndOsNameAndPrimaryHardwareTypeAndBrowserRenderingEngine(
                        model, osVersion, vendor, browserName, osName, hardwareType, browserRenderingEngine
                );

                if (existingDevice.isPresent()) {
                    System.out.println("device exist: " + model + " (OS: " + osVersion + ")");
                    return;
                }

                DeviceInfo device = new DeviceInfo();
                device.setModel(model);
                device.setOsVersion(osVersion);
                device.setVendor(vendor);
                device.setBrowserName(browserName);
                device.setOsName(osName);
                device.setPrimaryHardwareType(hardwareType);
                device.setBrowserRenderingEngine(browserRenderingEngine);

                deviceRepository.save(device);
                deviceCache.put(userAgent, true);
                System.out.println("new device: " + model + " (OS: " + osVersion + ")");
            }
        } catch (Exception e) {
            System.err.println("Error fetching data from DeviceAtlas: " + e.getMessage());
        }
    }

    public List<DeviceInfo> getTablets() {
        return deviceRepository.findByPrimaryHardwareTypeOrderByOsVersionDesc("Tablet");
    }
}
