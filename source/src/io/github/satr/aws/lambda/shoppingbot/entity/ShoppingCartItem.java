package io.github.satr.aws.lambda.shoppingbot.entity;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.fasterxml.jackson.annotation.JacksonInject;


public class ShoppingCartItem {
    private String product;
    private Double amount = 0.0;
    private String unit = null;
    private double price = 0.0;

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

    @DynamoDBAttribute(attributeName = "price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addAmount(double amount) {
        this.amount += amount;
    }

    @DynamoDBIgnore
    public double getSum() {
        return amount * price;

    }

    @Override
    public String toString() {
        if (amount <= 0.0 || price <= 0.0)
            return "";
        String niceAmount = amount % 1 == 0 ? ""+ Math.round(amount) : "" + amount;
        if(unit == null)
            return String.format("%s %s, price: %s, sum: %s", niceAmount, product, price, getSum());
        return String.format("%s %s of %s, price: %s, sum: %s", niceAmount, unit, product, price, getSum());
    }
}
