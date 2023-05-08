package com.brognara.useringredientsservice.model;

import com.brognara.useringredientsservice.common.MeasurementMetric;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class IngredientDetails {
    @JsonProperty("amount_metric")
    private final MeasurementMetric amountMetric;
    @JsonProperty("amount_number")
    private final float amountNumber;
    @JsonProperty("last_updated_timestamp")
    private final long lastUpdatedTimestamp;
}
