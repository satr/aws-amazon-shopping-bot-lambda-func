package io.github.satr.aws.lambda.shoppingbot.entity;
// Copyright © 2017, github.com/satr, MIT License

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class UnitPrice {
    private List<String> unitForms = new ArrayList<>();
    private Double price;

    @JsonProperty("unit_forms")
    public List<String> getUnitForms() {
        return unitForms;
    }

    public void setUnitForms(List<String> unitForms) {
        this.unitForms = unitForms;
    }

    @JsonProperty("price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UnitPrice{'price='" + price + "'}\n");
        for(String unitForm: unitForms)
            builder.append(unitForm + "|");
        return builder.toString();
    }
}
