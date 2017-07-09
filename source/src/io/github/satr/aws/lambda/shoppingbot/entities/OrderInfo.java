package io.github.satr.aws.lambda.shoppingbot.entities;
// Copyright © 2017, github.com/satr, MIT License

import java.lang.reflect.ParameterizedType;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class OrderInfo<T extends OrderItemInfo> {
    protected String userId;
    protected String updatedOn;
    private User user;
    private ZonedDateTime updatedOnAsDate;
    private List<T> items = new ArrayList<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if(user != null)
            setUserId(user.getUserId());
    }

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

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {

        this.items = items == null ? new ArrayList<>() : items;
    }

    public T getItemByProduct(String product) {
        for (T item : getItems()) {
            if (!item.getProduct().equals(product))
                continue;
            return item;
        }
        T item = createItem();
        if(item != null) {
            item.setProduct(product);
            items.add(item);
        }
        return item;
    }

    private T createItem()
    {
        try {
            return (T) ((Class) ((ParameterizedType) this.getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0])
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getTotalSum() {
        double totalSum = 0.0;
        for(T item: getItems()) {
            if(!item.isEmpty())
                totalSum += item.getSum();
        }
        return totalSum;
    }

    public boolean isEmpty(){
        for(T cartItem: getItems()){
            if(!cartItem.isEmpty())
                return false;
        }
        return true;
    }
}
