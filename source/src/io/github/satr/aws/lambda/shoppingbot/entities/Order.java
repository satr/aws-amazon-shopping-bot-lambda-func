package io.github.satr.aws.lambda.shoppingbot.entities;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import io.github.satr.aws.lambda.shoppingbot.entities.converters.OrderItemConverter;

import java.util.List;
import java.util.UUID;

@DynamoDBTable(tableName = "Order")
public class Order extends OrderInfo<OrderItem> {
    private String orderId;
    private String status = OrderStatus.Completed;

    public Order() {
        this.orderId = UUID.randomUUID().toString();
    }

    @DynamoDBHashKey(attributeName = "order_id")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @DynamoDBAttribute(attributeName = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    @DynamoDBAttribute(attributeName = "user_id")
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
    @DynamoDBTypeConverted(converter = OrderItemConverter.class)
    public List<OrderItem> getItems() {
        return super.getItems();
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", orderId='" + orderId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public final class OrderStatus {
        public final static String Completed = "Completed";
        public final static String Shipped = "Shipped";
        public final static String Cancelled = "Cancelled";
    }
}