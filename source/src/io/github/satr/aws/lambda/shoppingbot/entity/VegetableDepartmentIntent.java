package io.github.satr.aws.lambda.shoppingbot.entity;

public class VegetableDepartmentIntent {
    public static final String Name = "VegetableDepartment";

    public class Slot {
        public static final String Amount = "Amount";
        public static final String Product = "VegetableProduct";
        public static final String Unit = "VegetableDepartmentUnit";
    }
}