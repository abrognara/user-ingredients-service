package com.brognara.useringredientsservice.model;

import com.brognara.useringredientsservice.common.MeasurementMetric;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class PutIngredientRequest {
    private final String lastUpdatedTimestamp;
    private final Map<String, IngredientDetails> ingredientNameToDetailsMap;
}
