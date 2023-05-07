package com.brognara.useringredientsservice.repository;

import com.brognara.useringredientsservice.common.MeasurementMetric;
import com.brognara.useringredientsservice.model.PutIngredientRequest;
import com.brognara.useringredientsservice.model.UserIngredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UserIngredientsRepository {

    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public UserIngredientsRepository(final DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // TODO will need to scan user_id, or use a random unique PK instead of user_id to be able to query multiple items by a user_id
    public List<UserIngredient> getUserIngredients(final String userId) {
        Map<String, AttributeValue> primaryKey = Map.of(
                "user_id", AttributeValue.builder().s(userId).build(),
                "ingredient_name", AttributeValue.builder().s("Olive Oil").build()
        );
        GetItemRequest request = GetItemRequest.builder().key(primaryKey).tableName("test.users_ingredients").build();

        GetItemResponse response = dynamoDbClient.getItem(request);
        if (!response.hasItem())
            return Collections.emptyList();

        System.out.println(response.item().toString());
        return List.of(mapToUserIngredient(response.item()));
    }

    private UserIngredient mapToUserIngredient(Map<String, AttributeValue> item) {
        return new UserIngredient(
                item.get("user_id").s(),
                item.get("ingredient_name").s(),
                MeasurementMetric.valueOf(item.get("amount_metric").s()),
                Float.parseFloat(item.get("amount_number").n()),
                item.get("timestamp").s()
        );
    }

    public boolean putItem(final String userId, final PutIngredientRequest putIngredientRequest) {
        Map<String, AttributeValue> userIngredientAttributes = mapToAttributes(userId, putIngredientRequest);
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("test.users_ingredients")
                .item(userIngredientAttributes)
                .build();
        try {
            dynamoDbClient.putItem(putItemRequest);
        } catch (Exception e) {
            System.err.println("Failed to create item in DynamoDb: " + e);
            return false;
        }
        return true;
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

    private Map<String, AttributeValue> mapToAttributes
            (final String userId, final PutIngredientRequest putIngredientRequest) {
        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put("user_id", AttributeValue.builder().s(userId).build());
        attributes.put("ingredients",
                AttributeValue.builder().m(putIngredientRequest.getIngredientNameToDetailsMap()).build());
        attributes.put("last_updated_timestamp",
                AttributeValue.builder().s(putIngredientRequest.getLastUpdatedTimestamp()).build());
        return attributes;
    }

}
