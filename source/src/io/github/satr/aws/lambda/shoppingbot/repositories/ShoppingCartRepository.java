package io.github.satr.aws.lambda.shoppingbot.repositories;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.entities.ShoppingCart;

import java.util.List;

public interface ShoppingCartRepository extends Repository {
    List<ShoppingCart> getAllShoppingCarts();
    ShoppingCart getShoppingCartByUserId(String userId);
    void save(ShoppingCart cart);
    void delete(ShoppingCart cart);
}
