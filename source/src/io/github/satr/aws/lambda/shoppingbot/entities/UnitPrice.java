package io.github.satr.aws.lambda.shoppingbot.entities;
// Copyright © 2017, github.com/satr, MIT License

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class UnitPrice {
    public static final String UnitPieces = "pieces";
    private List<String> unitForms = new ArrayList<>();
    private double price;
    private String unit;

    @JsonProperty("unit_forms")
    public List<String> getUnitForms() {
        return unitForms;
    }

    public void setUnitForms(List<String> unitForms) {
        this.unitForms = unitForms;
    }

    @JsonProperty("price")
    public double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonProperty("unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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
