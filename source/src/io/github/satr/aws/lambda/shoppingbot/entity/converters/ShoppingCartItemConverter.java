package io.github.satr.aws.lambda.shoppingbot.entity.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCartItem;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartItemConverter  implements DynamoDBTypeConverter<String, List<ShoppingCartItem>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(List<ShoppingCartItem> object) {
        List<ShoppingCartItem> entities = (List<ShoppingCartItem>) object;
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
    public List<ShoppingCartItem> unconvert(String inputString) {

        List<ShoppingCartItem> entities = null;
        try {
            if (inputString != null && inputString.length() != 0)
                entities = (List<ShoppingCartItem>)objectMapper.readValue(inputString, new TypeReference<List<ShoppingCartItem>>(){});
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return entities;
    }
}
