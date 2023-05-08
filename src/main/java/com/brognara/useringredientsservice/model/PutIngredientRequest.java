package com.brognara.useringredientsservice.model;

import com.brognara.useringredientsservice.common.MeasurementMetric;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@Getter
@ToString
public class PutIngredientRequest {
    @JsonProperty("last_updated_timestamp")
    private final long lastUpdatedTimestamp;
    @JsonProperty("ingredients")
    private final Map<String, IngredientDetails> ingredientNameToDetailsMap;
}
