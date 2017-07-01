package io.github.satr.aws.lambda.shoppingbot.repositories;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import io.github.satr.aws.lambda.shoppingbot.entity.Product;

import java.util.List;

public class ProductRepositoryImpl extends RepositoryImpl implements ProductRepository {
    public final class Attr {

        public final static String ProductId = "product_id";
        public final static String NameForms = "name_forms";
        public final static String Price = "prices";
    }
    public final static String TableName = "Product";

    public ProductRepositoryImpl(AmazonDynamoDB dynamodb, DynamoDBMapper dbMapper) {
        super(dynamodb, dbMapper);
    }

    @Override
    public void save(Product product) {
        dbMapper.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return dbMapper.scan(Product.class, new DynamoDBScanExpression());
    }

    @Override
    public Product getByProductId(String productId) {
        return dbMapper.load(Product.class, productId);
    }
}
