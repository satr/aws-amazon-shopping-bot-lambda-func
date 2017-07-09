package io.github.satr.aws.lambda.shoppingbot.services;
// Copyright © 2017, github.com/satr, MIT License

import com.sun.istack.internal.NotNull;
import io.github.satr.aws.lambda.shoppingbot.entities.Order;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.repositories.OrderRepository;

import java.util.List;

public class OrderServiceImpl extends Service implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(@NotNull OrderRepository orderRepository, @NotNull Logger logger) {
        super(logger);
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getByOrderId(String orderId) {
        return orderRepository.getByOrderId(orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.getOrdersByUserId(userId);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void delete(Order order) {
        orderRepository.delete(order);
    }
}
