package com.aldisued.iot.monitoring.service;


import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class MeasurementCalculatorService {

  public List<Double> filterByAverageDeviation(List<Double> values, Double deviation) {
    if (deviation == null || deviation < 0.0 || deviation > 1.0) {
      throw new IllegalArgumentException("Invalid deviation value: " + deviation);
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
    if (windowSize <= 0 || windowSize > data.size()) {
      throw new IllegalArgumentException("Invalid window size value: " + windowSize);
    }

    List<Double> averages = new ArrayList<>();

    for (int i = 0; i <= data.size() - windowSize; i++) {

      double total = 0.0;

      for (int j = 0; j < windowSize; j++) {
        total += data.get(i+j);
      }

      averages.add(total / windowSize);
    }

    return averages;
  }

  private boolean isWithinRange(Double val, Double upperLimit, Double lowerLimit) {
    return val != null && val < upperLimit && val > lowerLimit;
  }
}
