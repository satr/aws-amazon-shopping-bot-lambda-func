package io.github.satr.aws.lambda.shoppingbot.data;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sun.media.sound.InvalidDataException;
import io.github.satr.aws.lambda.shoppingbot.entity.User;

import java.util.ArrayList;
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

    public UserRepositoryImpl(AmazonDynamoDB dynamodb) {
        this.dynamodb = dynamodb;
        dbMapper = new DynamoDBMapper(dynamodb);
    }

    @Override
    public List<User> getList() {
        return dbMapper.scan(User.class, new DynamoDBScanExpression());
    }

    @Override
    public User getById(String userId) throws InvalidDataException {
        String attrValueUserId = ":v_user_id";
        String keyConditionExpression = String.format("%s=%s", Attr.UserId, attrValueUserId);
        Map<String, AttributeValue> expressionValueMap = new HashMap<>();
        expressionValueMap.put(attrValueUserId, new AttributeValue(userId));
        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                                                            .withKeyConditionExpression(keyConditionExpression)
                                                            .withExpressionAttributeValues(expressionValueMap);

        PaginatedQueryList<User> queryResult = dbMapper.query(User.class, queryExpression);
        if(queryResult.size() > 1)
            throw new InvalidDataException("Multiple users exist with same UserId.");

        return queryResult.size() > 0 ? queryResult.get(0) : null;
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
}
