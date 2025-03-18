package com.example.demo.repository;

import com.example.demo.model.DeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceInfo, Long> {
    List<DeviceInfo> findByPrimaryHardwareTypeOrderByOsVersionDesc(String hardwareType);
    @Query("SELECT COUNT(d) FROM DeviceInfo d WHERE d.model = :model AND d.osVersion = :osVersion")
    long countByModelAndOsVersion(@Param("model") String model, @Param("osVersion") String osVersion);
}
