package com.example.demo.repository;

import com.example.demo.model.DeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.List;

public interface DeviceRepository extends JpaRepository<DeviceInfo, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM DeviceInfo d WHERE d.model = :model AND d.osVersion = :osVersion AND d.vendor = :vendor " +
            "AND d.browserName = :browserName AND d.osName = :osName AND d.primaryHardwareType = :primaryHardwareType " +
            "AND d.browserRenderingEngine = :browserRenderingEngine")
    Optional<DeviceInfo> findByModelAndOsVersionAndVendorAndBrowserNameAndOsNameAndPrimaryHardwareTypeAndBrowserRenderingEngine(
            @Param("model") String model,
            @Param("osVersion") String osVersion,
            @Param("vendor") String vendor,
            @Param("browserName") String browserName,
            @Param("osName") String osName,
            @Param("primaryHardwareType") String primaryHardwareType,
            @Param("browserRenderingEngine") String browserRenderingEngine
    );

    List<DeviceInfo> findByPrimaryHardwareTypeOrderByOsVersionDesc(String primaryHardwareType);
}
