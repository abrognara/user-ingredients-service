package com.brognara.useringredientsservice.repository;

import com.brognara.useringredientsservice.model.IngredientDetails;
import com.brognara.useringredientsservice.model.PutIngredientRequest;
import com.brognara.useringredientsservice.model.UserIngredients;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserIngredientsRepository {

    private static final String TABLE_NAME = "test.users_ingredients.v3";

    private final DynamoDbClient dynamoDbClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserIngredientsRepository(final DynamoDbClient dynamoDbClient, ObjectMapper objectMapper) {
        this.dynamoDbClient = dynamoDbClient;
        this.objectMapper = objectMapper;
    }

    public UserIngredients getUserIngredients(final String userId) {
        Map<String, AttributeValue> primaryKey = Map.of(
                "user_id", AttributeValue.builder().s(userId).build()
        );
        GetItemRequest request = GetItemRequest.builder().key(primaryKey).tableName(TABLE_NAME).build();

        GetItemResponse response = dynamoDbClient.getItem(request);
        if (!response.hasItem())
            return null;

        System.out.println(response.item().toString());
        return mapToUserIngredient(response.item());
    }

    // TODO use m() type instead of bytes
    private UserIngredients mapToUserIngredient(Map<String, AttributeValue> item) {
        SdkBytes ingredientsBytes = item.get("ingredients").b();
        Map<String, IngredientDetails> ingredientNameToDetailsMap;
        try {
            ingredientNameToDetailsMap = objectMapper.readValue(ingredientsBytes.asByteArray(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new UserIngredients(
                item.get("last_updated_timestamp").s(),
                ingredientNameToDetailsMap
        );
    }

    public boolean putUserIngredients(final String userId, final PutIngredientRequest putIngredientRequest) {
        Map<String, AttributeValue> userIngredientAttributes = mapToAttributes(userId, putIngredientRequest);
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(userIngredientAttributes)
                .build();
        try {
            PutItemResponse response = dynamoDbClient.putItem(putItemRequest);
            System.out.println("Write consumed capacity: " + response.consumedCapacity());
        } catch (Exception e) {
            System.err.println("Failed to create item in DynamoDb: " + e);
            return false;
        }
        return true;
    }

    private Map<String, AttributeValue> mapToAttributes
            (final String userId, final PutIngredientRequest putIngredientRequest) {
        byte[] bytes;
        String temp;
        try {
            temp = objectMapper.writeValueAsString(putIngredientRequest.getIngredientNameToDetailsMap());
//            bytes = objectMapper.writeValueAsBytes(putIngredientRequest.getIngredientNameToDetailsMap());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Json str = " + temp);
        bytes = temp.getBytes(StandardCharsets.UTF_8);
        System.out.println("Len of ingredients bytes = " + bytes.length);

        System.out.println("user_id = " + userId);
        System.out.println("timestamp = " + putIngredientRequest.getLastUpdatedTimestamp());

        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put("user_id", AttributeValue.builder().s(userId).build());
        attributes.put("ingredients",
                AttributeValue.builder().b(SdkBytes.fromByteArray(bytes)).build());
        attributes.put("last_updated_timestamp",
                AttributeValue.builder().s(String.valueOf(putIngredientRequest.getLastUpdatedTimestamp())).build());

        return attributes;
    }

    // note: batch allows max 25 req's, 16 MB total
//    public boolean putItems(final String userId, final List<PutIngredientRequest> putIngredientRequests) {
//        List<WriteRequest> requests = putIngredientRequests.stream()
//                .map(r -> {
//                    PutRequest putRequest = PutRequest.builder()
//                            .item(
//                                    Map.of(
//                                            "user_id", AttributeValue.builder().s(userId).build(),
//                                            "ingredient_name", AttributeValue.builder().s(r.getIngredientName()).build(),
//                                            "amount_metric", AttributeValue.builder().s(r.getAmountMetric().name()).build(),
//                                            "amount_number", AttributeValue.builder().n(String.valueOf(r.getAmountNumber())).build(),
//                                            "timestamp", AttributeValue.builder().s(r.getTimestamp()).build()
//                                    )
//                            )
//                            .build();
//                    return WriteRequest.builder().putRequest(putRequest).build();
//                })
//                .collect(Collectors.toList());
//        BatchWriteItemRequest batchRequest = BatchWriteItemRequest.builder()
//                .requestItems(Map.of(userId, requests))
//                .build();
//        try {
//            // TODO backoff logic for retires
//            int retries = 0;
//            while (batchRequest.hasRequestItems() && retries < 5) {
//                BatchWriteItemResponse batchResponse = dynamoDbClient.batchWriteItem(batchRequest);
//                System.out.println("Consumed capacities for batch write: " + batchResponse.consumedCapacity());
//
//                // if there are unprocessed items then add to batch request
//                if (batchResponse.hasUnprocessedItems()) {
//                    batchRequest = BatchWriteItemRequest.builder()
//                            .requestItems(batchResponse.unprocessedItems())
//                            .build();
//                }
//                retries++;
//            }
//        } catch (Exception e) {
//            System.err.println("Failed to batch put items in DynamoDb: " + e);
//            return false;
//        }
//        return true;
//    }

}
