package io.github.satr.aws.lambda.shoppingbot.repositories;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import io.github.satr.aws.lambda.shoppingbot.entities.ShoppingCart;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class ShoppingCartRepositoryImpl extends RepositoryImpl implements ShoppingCartRepository {
    public final static String TableName = "ShoppingCart";
    public final class Attr {
        public final static String UserId = "user_id";
    }

    public ShoppingCartRepositoryImpl(AmazonDynamoDB dynamodb, DynamoDBMapper dbMapper) {
        super(dynamodb, dbMapper);
    }

    @Override
    public void save(ShoppingCart cart) {
        cart.setUpdatedOn(ZonedDateTime.now(ZoneId.of("UTC")).toString());
        dbMapper.save(cart);
    }

    @Override
    public List<ShoppingCart> getAllShoppingCarts() {
        return dbMapper.scan(ShoppingCart.class, new DynamoDBScanExpression());
    }

    public ShoppingCart getShoppingCartByUserId(String userId) {
        return dbMapper.load(ShoppingCart.class, userId);
    }

    @Override
    public void delete(ShoppingCart cart) {
        dbMapper.delete(cart);
    }
}
