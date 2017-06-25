package io.github.satr.aws.lambda.shoppingbot.services;

import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart getShoppingCartBySessionId(String sessionId);
}
