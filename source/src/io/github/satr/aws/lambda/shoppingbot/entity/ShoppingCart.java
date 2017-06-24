package io.github.satr.aws.lambda.shoppingbot.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@DynamoDBTable(tableName = "ShoppingCart")
public class ShoppingCart {
    private String cartId;
    private String userId;
    private User user;
    private String sessionId;
    private String updatedOn;
    private ZonedDateTime updatedOnAsDate;

    public ShoppingCart() {
        setCartId(UUID.randomUUID().toString());
    }

    @DynamoDBHashKey(attributeName="cart_id")
    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    @DynamoDBAttribute(attributeName = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBAttribute(attributeName = "session_id")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
                "cartId='" + cartId + '\'' +
                ", userId='" + userId + '\'' +
                ", sessionId='" + sessionId + '\'' +
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
}
