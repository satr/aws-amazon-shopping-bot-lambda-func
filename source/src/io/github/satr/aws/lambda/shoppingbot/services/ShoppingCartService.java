package io.github.satr.aws.lambda.shoppingbot.services;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart getShoppingCartByUserId(String userId);
    void save(ShoppingCart cart);
}
