package io.github.satr.aws.lambda.shoppingbot.services;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.entities.Product;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.repositories.ProductRepository;

public class ProductServiceImpl extends Service implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository, Logger logger) {
        super(logger);
        this.productRepository = productRepository;
    }

    @Override
    public Product getByProductId(String productId) {
        return productRepository.getByProductId(productId);
    }
}
