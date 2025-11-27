package com.aldisued.iot.monitoring.repository;

import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.entity.SensorType;
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

    @Query("""
            SELECT reading.value FROM SensorReading reading
            WHERE reading.sensor.type = :sensorType
            AND
            reading.timestamp >= :from
            AND
            reading.timestamp <= :to
            ORDER BY reading.timestamp ASC
            """)
    List<Double> getReadingsInTimePeriodByType(SensorType sensorType, LocalDateTime from, LocalDateTime to);

}
