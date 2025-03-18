package com.example.demo.service;

import com.example.demo.model.DeviceInfo;
import com.example.demo.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class DeviceAtlasService {
    private final RestTemplate restTemplate;
    private final DeviceRepository deviceRepository;

    @Value("${deviceatlas.api.url}")
    private String apiUrl;

    @Value("${deviceatlas.api.key}")
    private String apiKey;

    @Autowired
    public DeviceAtlasService(RestTemplate restTemplate, DeviceRepository deviceRepository) {
        this.restTemplate = restTemplate;
        this.deviceRepository = deviceRepository;
    }

    // Process a single User-Agent and store it in the database
    public void fetchAndStoreDeviceInfo(String userAgent) {
        try {
            //encryption User-Agent
            String encodedUserAgent = URLEncoder.encode(userAgent, StandardCharsets.UTF_8);
            String url = apiUrl + "?licencekey=" + apiKey + "&useragent=" + encodedUserAgent;

            // send request to DeviceAtlas API
            Map response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("properties")) {
                Map<String, Object> properties = (Map<String, Object>) response.get("properties");

                // Only store `Tablet` devices
                String hardwareType = (String) properties.get("primaryHardwareType");
                if ("Tablet".equalsIgnoreCase(hardwareType)) {
                    String model = (String) properties.get("model");
                    String osVersion = (String) properties.get("osVersion");

                    // Check if the device already exists in the database
                    if (deviceRepository.countByModelAndOsVersion(model, osVersion) == 0) {
                        DeviceInfo device = new DeviceInfo();
                        device.setPrimaryHardwareType(hardwareType);
                        device.setOsVersion(osVersion);
                        device.setVendor((String) properties.get("vendor"));
                        device.setBrowserName((String) properties.get("browserName"));
                        device.setModel(model);
                        device.setOsName((String) properties.get("osName"));
                        device.setBrowserRenderingEngine((String) properties.get("browserRenderingEngine"));

                        deviceRepository.save(device);
                        System.out.println("New device saved: " + model + " (OS: " + osVersion + ")");
                    } else {
                        System.out.println("Device already exists: " + model + " (OS: " + osVersion + ")");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching data from DeviceAtlas: " + e.getMessage());
        }
    }


    // Get all `Tablet` devices and sort them by OS version in descending order
    public List<DeviceInfo> getTablets() {
        return deviceRepository.findByPrimaryHardwareTypeOrderByOsVersionDesc("Tablet");
    }
}
