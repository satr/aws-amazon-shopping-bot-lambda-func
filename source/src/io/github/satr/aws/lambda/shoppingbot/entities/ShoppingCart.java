package io.github.satr.aws.lambda.shoppingbot.entities;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import io.github.satr.aws.lambda.shoppingbot.entities.converters.ShoppingCartItemConverter;

import java.util.List;

@DynamoDBTable(tableName = "ShoppingCart")
public class ShoppingCart extends OrderInfo<ShoppingCartItem> {

    @Override
    @DynamoDBHashKey(attributeName = "user_id")
    public String getUserId() {
        return super.getUserId();
    }

    @Override
    @DynamoDBAttribute(attributeName = "updated_on")
    public String getUpdatedOn() {
        return super.getUpdatedOn();
    }

    @Override
    @DynamoDBAttribute(attributeName = "items")
    @DynamoDBTypeConverted(converter = ShoppingCartItemConverter.class)
    public List<ShoppingCartItem> getItems() {
        return super.getItems();
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "userId='" + userId + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }
}
