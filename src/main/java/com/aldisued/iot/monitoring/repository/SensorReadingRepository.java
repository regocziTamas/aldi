package com.aldisued.iot.monitoring.repository;

import com.aldisued.iot.monitoring.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorReadingRepository extends JpaRepository<SensorReading, String> {

    @Query("""
            SELECT reading FROM SensorReading reading
            WHERE reading.sensor.type = 'TEMPERATURE'
            AND
            reading.timestamp >= :from
            AND
            reading.timestamp <= :to
            """)
    List<SensorReading> getTemperatureReadingsInTimePeriod(LocalDateTime from, LocalDateTime to);

}
