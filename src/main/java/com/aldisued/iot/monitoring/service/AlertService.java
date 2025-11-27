package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertService {

  private final AlertRepository alertRepository;
  private final SensorRepository sensorRepository;
  private final KafkaTemplate<String, AlertDto> kafkaTemplate;

  public AlertService(AlertRepository alertRepository, SensorRepository sensorRepository,
      KafkaTemplate<String, AlertDto> kafkaTemplate) {
    this.alertRepository = alertRepository;
    this.sensorRepository = sensorRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  public Alert saveAlert(AlertDto alertDto) {

    Alert savedAlert = this.saveAlertInTransaction(alertDto);

    kafkaTemplate.send("alerts", alertDto);

    return savedAlert;
  }

  public Optional<AlertDto> findLastAlertBySensorId(UUID sensorId) {
    return this.alertRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId)
            .map(alert -> new AlertDto(
                    alert.getSensor().getId(),
                    alert.getMessage(),
                    alert.getTimestamp()
            ));
  }

  @Transactional
  private Alert saveAlertInTransaction(AlertDto alertDto) {
    Sensor sensor = this.sensorRepository.findById(alertDto.sensorId())
            .orElseThrow(() -> new IllegalArgumentException(
                    MessageFormat.format("Could not find sensor with id: {0}", alertDto.sensorId()))
            );

    Alert newAlert = new Alert(
            alertDto.message(),
            alertDto.timestamp(),
            sensor);

    return alertRepository.save(newAlert);
  }
}
