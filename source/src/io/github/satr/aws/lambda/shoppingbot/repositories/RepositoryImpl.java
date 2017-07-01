package io.github.satr.aws.lambda.shoppingbot.repositories;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryImpl {
    protected final DynamoDBMapper dbMapper;
    protected AmazonDynamoDB dynamodb;

    public RepositoryImpl(AmazonDynamoDB dynamodb, DynamoDBMapper dbMapper) {
        this.dynamodb = dynamodb;
        this.dbMapper = dbMapper;
    }

    protected <T> List<T> scan(Class<T> type, String filterExpression, Map<String, AttributeValue> expressionValueMap) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(filterExpression)
                .withExpressionAttributeValues(expressionValueMap);
        return dbMapper.scan(type, scanExpression);
    }

    protected <T> List<T> scan(Class<T> type, String name, String value) {
        String attrValue = ":v_attr";
        String filterExpression = String.format("%s=%s", name, attrValue);
        Map<String, AttributeValue> expressionValueMap = new HashMap<>();
        expressionValueMap.put(attrValue, new AttributeValue().withS(value));
        return scan(type, filterExpression, expressionValueMap);
    }
}
