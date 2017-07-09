package io.github.satr.aws.lambda.shoppingbot.services;
// Copyright © 2017, github.com/satr, MIT License

import com.sun.istack.internal.NotNull;
import io.github.satr.aws.lambda.shoppingbot.entities.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.entities.User;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.repositories.ShoppingCartRepository;

public class ShoppingCartServiceImpl extends Service implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private UserService userService;

    public ShoppingCartServiceImpl(@NotNull ShoppingCartRepository shoppingCartRepository, UserService userService, @NotNull Logger logger) {
        super(logger);
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
    }

    @Override
    public ShoppingCart getShoppingCartByUserId(String userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
           getLogger().log(String.format("User not found by UserId: %s", userId));
            return null;
        }
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(userId);
        if(shoppingCart != null)
            shoppingCart.setUser(user);
        return shoppingCart;
    }

    @Override
    public void save(ShoppingCart cart) {
        shoppingCartRepository.save(cart);
    }

    @Override
    public void delete(ShoppingCart shoppingCart) {
        shoppingCartRepository.delete(shoppingCart);
    }
}
