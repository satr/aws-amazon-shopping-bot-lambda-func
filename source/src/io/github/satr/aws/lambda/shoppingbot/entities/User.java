package io.github.satr.aws.lambda.shoppingbot.entities;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import io.github.satr.aws.lambda.shoppingbot.request.UserIdType;

import java.util.UUID;

import static org.apache.http.util.TextUtils.isEmpty;

@DynamoDBTable(tableName = "User")
public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String facebookId;
    private String address;

    public User() {
        setUserId(UUID.randomUUID().toString());
    }

    @DynamoDBHashKey(attributeName="user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBAttribute(attributeName = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @DynamoDBAttribute(attributeName = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @DynamoDBAttribute(attributeName = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @DynamoDBAttribute(attributeName = "facebook_id")
    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", facebook_id='" + facebookId + '\'' +
                '}';
    }

    public boolean sameNamesAs(String firstName, String lastName) {
         return firstName.equals(getFirstName()) && lastName.equals(getLastName());
    }

    public boolean hasUserId(String userId, UserIdType userIdType) {
        if(isEmpty(userId) || userIdType == UserIdType.Undefined)
            return false;
        if(userIdType == UserIdType.Facebook)
            return userId.equals(getFacebookId());
        return false;
    }
}
