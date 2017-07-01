package io.github.satr.aws.lambda.shoppingbot.repositories;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getAllProducts();
    Product getByProductId(String productId);
    void save(Product product);
}
