package io.github.satr.aws.lambda.shoppingbot.entities.converters;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.satr.aws.lambda.shoppingbot.entities.OrderItemInfo;

import java.util.List;

public abstract class OrderItemInfoConverter<T extends OrderItemInfo> extends DbTypeConverter implements DynamoDBTypeConverter<String, List<T>> {

    private TypeReference<List<T>> typeReference;

    @Override
    public String convert(List<T> entities) {
        String outputString = null;
        try {
            outputString = objectMapper.writeValueAsString(entities);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return outputString;
    }

    @Override
    public List<T> unconvert(String inputString) {
        List<T> entities = null;
        try {
            if (inputString != null && inputString.length() != 0)
                entities = objectMapper.readValue(inputString, getTypeReference());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }

    private TypeReference<List<T>> getTypeReference() {
        return typeReference == null ? (typeReference = createTypeReference()) : typeReference;
    }

    //Due to the creating of TypeReference does not return proper type with generics
    protected abstract TypeReference<List<T>> createTypeReference();
}
