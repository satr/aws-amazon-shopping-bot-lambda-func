package io.github.satr.aws.lambda.shoppingbot.entity;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;


public class ShoppingCartItem {
    private String product;
    private Double amount;
    private String unit;

    public void setProduct(String product) {
        this.product = product;
    }
    @DynamoDBAttribute(attributeName = "product")
    public String getProduct() {
        return product;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @DynamoDBAttribute(attributeName = "amount")
    public Double getAmount() {
        return amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @DynamoDBAttribute(attributeName = "unit")
    public String getUnit() {
        return unit;
    }

    public void addAmount(double amount) {
        this.amount += amount;
    }
}
