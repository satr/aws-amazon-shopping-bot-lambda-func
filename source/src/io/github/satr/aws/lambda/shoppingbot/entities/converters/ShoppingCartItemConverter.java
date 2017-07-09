package io.github.satr.aws.lambda.shoppingbot.entities.converters;
// Copyright © 2017, github.com/satr, MIT License

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.satr.aws.lambda.shoppingbot.entities.ShoppingCartItem;

import java.util.List;

public class ShoppingCartItemConverter extends OrderItemInfoConverter<ShoppingCartItem> {
    @Override
    protected TypeReference<List<ShoppingCartItem>> createTypeReference() {
        return new TypeReference<List<ShoppingCartItem>>(){};
    }
}
