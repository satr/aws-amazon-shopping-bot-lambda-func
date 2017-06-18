package data;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import io.github.satr.aws.lambda.shoppingbot.data.UserRepositoryImpl;
import io.github.satr.aws.lambda.shoppingbot.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestRepositoryHelper {
    public static void deleteTableUser(AmazonDynamoDB dynamoDbClient) {
        dynamoDbClient.deleteTable(UserRepositoryImpl.TableName);
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
        List<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName(UserRepositoryImpl.Attr.UserId).withAttributeType("S"));

        List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
        keySchema.add(new KeySchemaElement().withAttributeName(UserRepositoryImpl.Attr.UserId).withKeyType(KeyType.HASH));

        CreateTableRequest request = new CreateTableRequest()
                .withTableName(UserRepositoryImpl.TableName)
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(1L)
                        .withWriteCapacityUnits(1L));

        dynamodb.createTable(request);

        TableDescription table = dynamodb.describeTable(UserRepositoryImpl.TableName).getTable();
    }
}
