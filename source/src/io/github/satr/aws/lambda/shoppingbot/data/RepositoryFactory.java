package io.github.satr.aws.lambda.shoppingbot.data;

public interface RepositoryFactory{
    UserRepository getUserRepository();
    ShoppingCartRepository getShoppingCartRepository();
    void shutdown();
}
