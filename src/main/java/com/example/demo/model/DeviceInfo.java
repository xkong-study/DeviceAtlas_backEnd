package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String primaryHardwareType;
    private String osVersion;
    private String vendor;
    private String browserName;
    private String model;
    private String osName;
    private String browserRenderingEngine;
}
