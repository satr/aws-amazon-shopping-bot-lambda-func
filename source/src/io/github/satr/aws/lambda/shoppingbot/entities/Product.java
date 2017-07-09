package io.github.satr.aws.lambda.shoppingbot.entities;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import io.github.satr.aws.lambda.shoppingbot.entities.converters.UnitPriceConverter;

import java.util.ArrayList;
import java.util.List;

@DynamoDBTable(tableName = "Product")
public class Product {
    public final static double notFoundPrice = -1.0;
    private String productId;
    private List<UnitPrice> unitPrices = new ArrayList<>();

    @DynamoDBHashKey(attributeName = "product_id")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @DynamoDBTypeConverted(converter = UnitPriceConverter.class)
    @DynamoDBAttribute(attributeName = "unit_prices")
    public List<UnitPrice> getUnitPrices() {
        return unitPrices;
    }

    public void setUnitPrices(List<UnitPrice> unitPrices) {
        this.unitPrices = unitPrices != null ? unitPrices : new ArrayList<>();
    }

    public UnitPrice getUnitPriceFor(String unit){
        for (UnitPrice unitPrice: getUnitPrices()){
            for(String unitForm: unitPrice.getUnitForms()){
                if(unitForm.equals(unit))
                    return unitPrice;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Product{'productId='" + productId + "'}\n");
        for(UnitPrice unitPrice: unitPrices)
            builder.append(unitPrice.toString() + "\n");
        return builder.toString();
    }
}
