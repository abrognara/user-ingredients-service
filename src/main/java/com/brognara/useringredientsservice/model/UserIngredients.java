package com.brognara.useringredientsservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@Getter
@ToString
public class UserIngredients {
    private final String lastUpdatedTimestamp;
    private final Map<String, IngredientDetails> ingredientNameToDetailsMap;
}
