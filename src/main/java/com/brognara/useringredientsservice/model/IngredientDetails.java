package com.brognara.useringredientsservice.model;

import com.brognara.useringredientsservice.common.MeasurementMetric;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IngredientDetails {
    private final MeasurementMetric amountMetric;
    private final float amountNumber;
    private final String timestamp;
}
