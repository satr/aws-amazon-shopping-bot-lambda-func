package io.github.satr.aws.lambda.shoppingbot.services;

import com.sun.istack.internal.NotNull;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.repositories.ShoppingCartRepository;

public class ShoppingCartServiceImpl extends Service implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(@NotNull ShoppingCartRepository shoppingCartRepository, @NotNull Logger logger) {
        super(logger);
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public ShoppingCart getShoppingCartByUserId(String userId) {
        return shoppingCartRepository.getShoppingCartByUserId(userId);
    }

    @Override
    public void save(ShoppingCart cart) {
        shoppingCartRepository.save(cart);
    }
}
