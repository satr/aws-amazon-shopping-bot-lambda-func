package io.github.satr.aws.lambda.shoppingbot.entities;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;


public class ShoppingCartItem extends OrderItemInfo {
    @Override
    @DynamoDBAttribute(attributeName = "product")
    public String getProduct() {
        return super.getProduct();
    }

    @Override
    @DynamoDBAttribute(attributeName = "amount")
    public Double getAmount() {
        return super.getAmount();
    }

    @Override
    @DynamoDBAttribute(attributeName = "unit")
    public String getUnit() {
        return super.getUnit();
    }

    @Override
    @DynamoDBAttribute(attributeName = "price")
    public double getPrice() {
        return super.getPrice();
    }

    @DynamoDBIgnore
    public double getSum() {
        return super.getSum();
    }

    @DynamoDBIgnore
    public boolean isEmpty() {
        return super.isEmpty();
    }
}
