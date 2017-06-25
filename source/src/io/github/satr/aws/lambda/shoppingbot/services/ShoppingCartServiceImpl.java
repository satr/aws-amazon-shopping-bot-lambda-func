package io.github.satr.aws.lambda.shoppingbot.services;

import com.sun.istack.internal.NotNull;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.repositories.ShoppingCartRepository;
import io.github.satr.aws.lambda.shoppingbot.repositories.exceptions.UnexpectedMultipleDataItemsException;

import java.util.List;

public class ShoppingCartServiceImpl extends Service implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(@NotNull ShoppingCartRepository shoppingCartRepository, @NotNull Logger logger) {
        super(logger);
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public ShoppingCart getShoppingCartBySessionId(String sessionId) {
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.getShoppingCartsBySessionId(sessionId);
        if(shoppingCarts.size() == 0)
            return null;
        if(shoppingCarts.size() == 1)
            return shoppingCarts.get(0);
        getLogger().log("Keep only latest Shopping Cart and delete others.");
        ShoppingCart shoppingCart = getLatestShoppingCartAndDeleteOthers(shoppingCarts);
        return shoppingCart;
    }

    private ShoppingCart getLatestShoppingCartAndDeleteOthers(List<ShoppingCart> shoppingCarts) {
        shoppingCarts.sort((o1, o2) -> o2.getUpdatedOnAsDate().compareTo(o1.getUpdatedOnAsDate()));
        ShoppingCart shoppingCart = shoppingCarts.get(0);
        for (int i = 1; i < shoppingCarts.size(); i++)
            shoppingCartRepository.delete(shoppingCarts.get(i));
        return shoppingCart;
    }
}
