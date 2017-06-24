package io.github.satr.aws.lambda.shoppingbot.data;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sun.media.sound.InvalidDataException;
import io.github.satr.aws.lambda.shoppingbot.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository {
    public final static String TableName = "User";
    private final DynamoDBMapper dbMapper;

    public final class Attr {
        public final static String UserId = "user_id";
        public final static String FirstName = "first_name";
        public final static String LastName = "last_name";
        public final static String Address = "address";
    }

    private AmazonDynamoDB dynamodb;

    public UserRepositoryImpl(AmazonDynamoDB dynamodb, DynamoDBMapper dbMapper) {
        this.dynamodb = dynamodb;
        this.dbMapper = dbMapper;
    }

    @Override
    public List<User> getList() {
        return dbMapper.scan(User.class, new DynamoDBScanExpression());
    }

    @Override
    public User getById(String userId) throws InvalidDataException {
        return dbMapper.load(User.class, userId);
    }

    @Override
    public List<User> getByName(String firstName, String lastName) {
        String attrValueFirstName = ":v_first_name";
        String attrValueLastName = ":v_last_name";
        String filterExpression = String.format("%s=%s and %s=%s", Attr.FirstName, attrValueFirstName,
                                                                   Attr.LastName, attrValueLastName);
        Map<String, AttributeValue> expressionValueMap = new HashMap<>();
        expressionValueMap.put(attrValueFirstName, new AttributeValue().withS(firstName));
        expressionValueMap.put(attrValueLastName, new AttributeValue().withS(lastName));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                                                .withFilterExpression(filterExpression)
                                                .withExpressionAttributeValues(expressionValueMap);
        return dbMapper.scan(User.class, scanExpression);
    }


    @Override
    public void save(User user) {
        dbMapper.save(user);
    }
}
