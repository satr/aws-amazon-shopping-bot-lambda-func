package io.github.satr.aws.lambda.shoppingbot.repositories;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class RepositoryFactoryImpl implements RepositoryFactory {
    private final AmazonDynamoDB dynamoDbClient;
    private final DynamoDBMapper dbMapper;
    private UserRepository userRepository;
    private ShoppingCartRepository shoppingCartRepository;
    private ProductRepository productRepository;

    public RepositoryFactoryImpl() {
        dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1).build();
        dbMapper = new DynamoDBMapper(dynamoDbClient);
    }

    @Override
    public UserRepository createUserRepository() {
        return userRepository != null ? userRepository : (userRepository = new UserRepositoryImpl(dynamoDbClient, dbMapper));
    }

    @Override
    public ShoppingCartRepository createShoppingCartRepository() {
        return shoppingCartRepository != null ? shoppingCartRepository : (shoppingCartRepository = new ShoppingCartRepositoryImpl(dynamoDbClient, dbMapper));
    }

    @Override
    public ProductRepository createProductRepository() {
        return productRepository != null ? productRepository : (productRepository = new ProductRepositoryImpl(dynamoDbClient, dbMapper));
    }

    @Override
    public void shutdown() {
        userRepository = null;
        shoppingCartRepository = null;
        dynamoDbClient.shutdown();
    }
}
