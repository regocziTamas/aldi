package com.aldisued.iot.monitoring.service;


import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class MeasurementCalculatorService {

  public List<Double> filterByAverageDeviation(List<Double> values, Double deviation) {
    if (deviation == null || deviation < 0.0 || deviation > 1.0) {
      throw new IllegalArgumentException("Incorrect deviation value: " + deviation);
    }

    DoubleSummaryStatistics summaryStatistics = values
            .stream()
            .collect(Collectors.summarizingDouble(e -> e));

    Double average = summaryStatistics.getAverage();
    Double upperLimit = average + average * deviation;
    Double lowerLimit = average - average * deviation;

    return values
            .stream()
            .filter(val -> isWithinRange(val, upperLimit, lowerLimit))
            .collect(Collectors.toList());
  }

  public List<Double> getMovingAverage(List<Double> data, int windowSize) {
    // TODO: Task 10
    return List.of();
  }

  private boolean isWithinRange(Double val, Double upperLimit, Double lowerLimit) {
    return val != null && val < upperLimit && val > lowerLimit;
  }

}
