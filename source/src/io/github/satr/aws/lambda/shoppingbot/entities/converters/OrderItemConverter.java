package io.github.satr.aws.lambda.shoppingbot.entities.converters;
// Copyright © 2017, github.com/satr, MIT License

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.satr.aws.lambda.shoppingbot.entities.OrderItem;

import java.util.List;

public class OrderItemConverter extends OrderItemInfoConverter<OrderItem> {
    @Override
    protected TypeReference<List<OrderItem>> createTypeReference() {
        return new TypeReference<List<OrderItem>>(){};
    }
}
