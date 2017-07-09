package io.github.satr.aws.lambda.shoppingbot.entities.converters;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.satr.aws.lambda.shoppingbot.entities.UnitPrice;

import java.util.List;

public class UnitPriceConverter extends DbTypeConverter implements DynamoDBTypeConverter<String, List<UnitPrice>> {

    @Override
    public String convert(List<UnitPrice> entities) {
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
    public List<UnitPrice> unconvert(String inputString) {
        List<UnitPrice> entities = null;
        try {
            if (inputString != null && inputString.length() != 0)
                entities = objectMapper.readValue(inputString, new TypeReference<List<UnitPrice>>(){});
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }
}
