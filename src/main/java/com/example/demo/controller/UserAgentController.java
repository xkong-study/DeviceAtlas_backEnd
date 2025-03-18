package com.example.demo.controller;

import com.example.demo.model.DeviceInfo;
import com.example.demo.service.DeviceAtlasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserAgentController {
    private final DeviceAtlasService deviceAtlasService;

    public UserAgentController(DeviceAtlasService deviceAtlasService) {
        this.deviceAtlasService = deviceAtlasService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/tablets")
    public List<DeviceInfo> getTablets() {
        return deviceAtlasService.getTablets();
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/fetch-devices")
    public ResponseEntity<String> fetchDevices(@RequestBody List<String> userAgents) {
        if (userAgents == null || userAgents.isEmpty()) {
            return ResponseEntity.badRequest().body("No User-Agents provided!");
        }

        userAgents.forEach(deviceAtlasService::fetchAndStoreDeviceInfo);
        return ResponseEntity.ok("Devices fetched and stored successfully");
    }
}
