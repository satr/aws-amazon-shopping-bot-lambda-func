package io.github.satr.aws.lambda.shoppingbot.data;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class RepositoryFactoryImpl implements RepositoryFactory {
    private final AmazonDynamoDB dynamoDbClient;
    private final DynamoDBMapper dbMapper;
    private UserRepository userRepository;

    public RepositoryFactoryImpl() {
        dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1).build();
        dbMapper = new DynamoDBMapper(dynamoDbClient);
    }

    public UserRepository getUserRepository() {
        return userRepository != null ? userRepository : (userRepository = new UserRepositoryImpl(dynamoDbClient, dbMapper));
    }

    @Override
    public void shutdown() {
        userRepository = null;
        dynamoDbClient.shutdown();
    }
}
