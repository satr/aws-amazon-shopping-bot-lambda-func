package io.github.satr.aws.lambda.shoppingbot.repositories;

public interface RepositoryFactory{
    UserRepository createUserRepository();
    ShoppingCartRepository createShoppingCartRepository();
    void shutdown();
}
