package io.github.satr.aws.lambda.shoppingbot.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@DynamoDBTable(tableName = "ShoppingCart")
public class ShoppingCart {
    private String userId;
    private User user;
    private String updatedOn;
    private ZonedDateTime updatedOnAsDate;
    private List<ShoppingCartItem> items = new ArrayList<>();

    @DynamoDBHashKey(attributeName = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBAttribute(attributeName = "updated_on")
    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
        updatedOnAsDate = null;
    }

    private ZonedDateTime getUtc() {
        return ZonedDateTime.now(ZoneId.of("UTC"));
    }

    @DynamoDBIgnore
    public User getUser() {
        return user;
    }

    @DynamoDBIgnore
    public void setUser(User user) {
        this.user = user;
        if(user != null)
            setUserId(user.getUserId());
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "userId='" + userId + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }

    @DynamoDBIgnore
    public ZonedDateTime getUpdatedOnAsDate() {
        if(updatedOnAsDate != null)
            return updatedOnAsDate;
        try {
            updatedOnAsDate = updatedOn != null && updatedOn.length() > 0 ? ZonedDateTime.parse(updatedOn) : getUtc();
        } catch (Exception e) {
            e.printStackTrace();
            updatedOnAsDate = getUtc();
        }
        return updatedOnAsDate;
    }

//    @DynamoDBAttribute(attributeName = "items")
    @DynamoDBIgnore
    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCartItem> items) {
        this.items = items;
    }

    public ShoppingCartItem getItemByProduct(String product) {
        List<ShoppingCartItem> cartItems = getItems();
        ShoppingCartItem cartItem = null;
        for (ShoppingCartItem item : cartItems) {
            if (!item.getProduct().equals(product))
                continue;
            cartItem = item;
            break;
        }
        if(cartItem == null) {
            cartItem = new ShoppingCartItem();
            cartItem.setProduct(product);
            cartItems.add(cartItem);
        }
        return cartItem;
    }
}
