package repositories;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import io.github.satr.aws.lambda.shoppingbot.entities.User;
import io.github.satr.aws.lambda.shoppingbot.repositories.OrderRepositoryImpl;
import io.github.satr.aws.lambda.shoppingbot.repositories.ProductRepositoryImpl;
import io.github.satr.aws.lambda.shoppingbot.repositories.ShoppingCartRepositoryImpl;
import io.github.satr.aws.lambda.shoppingbot.repositories.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestRepositoryHelper {
    public static void deleteTableUser(AmazonDynamoDB dynamoDbClient) {
        dynamoDbClient.deleteTable(UserRepositoryImpl.TableName);
    }

    public static void deleteTableProduct(AmazonDynamoDB dynamoDbClient) {
        dynamoDbClient.deleteTable(ProductRepositoryImpl.TableName);
    }

    public static void deleteTableShoppingCart(AmazonDynamoDB dynamoDbClient) {
        dynamoDbClient.deleteTable(ShoppingCartRepositoryImpl.TableName);
    }

    public static void deleteTableOrder(AmazonDynamoDB dynamoDbClient) {
        dynamoDbClient.deleteTable(OrderRepositoryImpl.TableName);
    }

    public static User addUser(DynamoDBMapper dbMapper, String firstName, String lastName, String address) {
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        dbMapper.save(user);
        return user;
    }

    public static void createTableUser(AmazonDynamoDB dynamodb) {
        createTable(dynamodb, UserRepositoryImpl.TableName, UserRepositoryImpl.Attr.UserId);
    }

    public static void createTableShoppingCart(AmazonDynamoDB dynamodb) {
        createTable(dynamodb, ShoppingCartRepositoryImpl.TableName, ShoppingCartRepositoryImpl.Attr.UserId);
    }

    public static void createTableOrder(AmazonDynamoDB dynamodb) {
        createTable(dynamodb, OrderRepositoryImpl.TableName, OrderRepositoryImpl.Attr.OrderId);
    }

    public static void createTableProduct(AmazonDynamoDB dynamodb) {
        createTable(dynamodb, ProductRepositoryImpl.TableName, ProductRepositoryImpl.Attr.ProductId);
    }

    private static void createTable(AmazonDynamoDB dynamodb, String tableName, String tableKeyFieldName) {
        List<AttributeDefinition> attributeDefinitions= new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName(tableKeyFieldName).withAttributeType("S"));

        List<KeySchemaElement> keySchema = new ArrayList<>();
        keySchema.add(new KeySchemaElement().withAttributeName(tableKeyFieldName).withKeyType(KeyType.HASH));

        CreateTableRequest request = new CreateTableRequest()
                .withTableName(tableName)
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(1L)
                        .withWriteCapacityUnits(1L));

        dynamodb.createTable(request);
//TODO: just to look at the result, if needed
//        TableDescription table = dynamodb.describeTable(tableName).getTable();
    }
}
