package io.github.satr.aws.lambda.shoppingbot.repositories;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import io.github.satr.aws.lambda.shoppingbot.entities.Order;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderRepositoryImpl extends RepositoryImpl implements OrderRepository {
    public final static String TableName = "Order";
    public final class Attr {
        public final static String OrderId = "order_id";
    }

    public OrderRepositoryImpl(AmazonDynamoDB dynamodb, DynamoDBMapper dbMapper) {
        super(dynamodb, dbMapper);
    }

    @Override
    public Order getByOrderId(String orderId) {
        return dbMapper.load(Order.class, orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        return dbMapper.scan(Order.class, new DynamoDBScanExpression());
    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        return scan(Order.class, "user_id", userId);
    }

    @Override
    public void save(Order order) {
        order.setUpdatedOn(ZonedDateTime.now(ZoneId.of("UTC")).toString());
        dbMapper.save(order);
    }

    @Override
    public void delete(Order order) {
        dbMapper.delete(order);
    }
}
