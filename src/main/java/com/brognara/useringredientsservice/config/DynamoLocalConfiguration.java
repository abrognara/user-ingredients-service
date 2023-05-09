package com.brognara.useringredientsservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Profile("local-dynamo")
@Configuration
public class DynamoLocalConfiguration {

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create()) // Elastic Beanstalk? or use container var
                .build();
    }

    // TEMP
//    @Bean
//    public DynamoDbClient dynamoDbClient() {
////        ProfileCredentialsProvider provider = ProfileCredentialsProvider.create("test-user");
//        return DynamoDbClient.builder()
//                .region(Region.US_EAST_1)
////                .credentialsProvider(provider)
//                .build();
//    }

}
