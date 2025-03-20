package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "device_info", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "model", "os_version", "vendor", "browser_name", "os_name", "primary_hardware_type", "browser_rendering_engine"
        })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String osVersion;

    @Column(nullable = false)
    private String vendor;

    @Column(nullable = false)
    private String browserName;

    @Column(nullable = false)
    private String osName;

    @Column(nullable = false)
    private String primaryHardwareType;

    @Column(nullable = false)
    private String browserRenderingEngine;
}
