package io.github.satr.aws.lambda.shoppingbot.data;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.sun.media.sound.InvalidDataException;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class ShoppingCartRepositoryImpl extends RepositoryImpl implements ShoppingCartRepository {
    public final class Attr {
        public final static String CartId = "cart_id";
        public final static String SessionId = "session_id";
        public final static String UpdatedOn = "updated_on";
        public final static String UserId = "user_id";
        public final static String FirstName = "first_name";
        public final static String LastName = "last_name";
        public final static String Address = "address";
    }

    public final static String TableName = "ShoppingCart";

    public ShoppingCartRepositoryImpl(AmazonDynamoDB dynamodb, DynamoDBMapper dbMapper) {
        super(dynamodb, dbMapper);
    }

    @Override
    public void save(ShoppingCart cart) {
        cart.setUpdatedOn(ZonedDateTime.now(ZoneId.of("UTC")).toString());
        dbMapper.save(cart);
    }

    @Override
    public List<ShoppingCart> getList() {
        return dbMapper.scan(ShoppingCart.class, new DynamoDBScanExpression());
    }

    @Override
    public ShoppingCart getById(String cartId) throws InvalidDataException {
        return dbMapper.load(ShoppingCart.class, cartId);
    }

    @Override
    public List<ShoppingCart> getByUserId(String userId) {
        return scan(ShoppingCart.class, Attr.UserId, userId);
    }

    @Override
    public List<ShoppingCart> getBySessionId(String sessionId) {
        return scan(ShoppingCart.class, Attr.SessionId, sessionId);
    }

    @Override
    public void delete(ShoppingCart cart) {
        dbMapper.delete(cart);
    }
}
