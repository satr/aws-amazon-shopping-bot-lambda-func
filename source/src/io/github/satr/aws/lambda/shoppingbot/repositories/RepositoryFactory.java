package io.github.satr.aws.lambda.shoppingbot.repositories;
// Copyright © 2017, github.com/satr, MIT License

public interface RepositoryFactory{
    UserRepository createUserRepository();
    ShoppingCartRepository createShoppingCartRepository();
    OrderRepository createOrderRepository();
    ProductRepository createProductRepository();

    void shutdown();
}
