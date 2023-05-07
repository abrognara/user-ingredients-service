package com.brognara.useringredientsservice.model;

import com.brognara.useringredientsservice.common.MeasurementMetric;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserIngredient {

//    @DynamoAttr(keyName="user_id", type="string")
    private final String userId; // TODO replace with user_email after google sign-in integration
    private final String ingredientName;
    private final MeasurementMetric amountMetric;
    private final float amountNumber;
    private final String timestamp;

}
