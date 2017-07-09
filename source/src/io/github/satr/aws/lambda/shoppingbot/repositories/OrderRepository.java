package io.github.satr.aws.lambda.shoppingbot.repositories;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.entities.Order;

import java.util.List;

public interface OrderRepository extends Repository {
    Order getByOrderId(String orderId);
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(String userId);
    void save(Order order);
    void delete(Order order);
}
