package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class SensorReadingService {

  private final SensorReadingRepository sensorReadingRepository;
  private final SensorRepository sensorRepository;

  public SensorReadingService(SensorReadingRepository sensorReadingRepository,
      SensorRepository sensorRepository) {
    this.sensorReadingRepository = sensorReadingRepository;
    this.sensorRepository = sensorRepository;
  }

  public SensorReading saveSensorReading(SensorReadingDto sensorReadingDto) {

    Sensor sensor = this.sensorRepository.findById(sensorReadingDto.sensorId())
            .orElseThrow(() -> new IllegalArgumentException(
                    MessageFormat.format("Could not find sensor with id: {0}", sensorReadingDto.sensorId()))
            );

    SensorReading newSensorReading = new SensorReading(
            sensorReadingDto.value(),
            sensorReadingDto.timestamp(),
            sensor
    );

    return sensorReadingRepository.save(newSensorReading);
  }
}
