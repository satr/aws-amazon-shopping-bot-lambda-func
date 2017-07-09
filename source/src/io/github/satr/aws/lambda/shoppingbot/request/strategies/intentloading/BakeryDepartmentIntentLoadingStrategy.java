package io.github.satr.aws.lambda.shoppingbot.request.strategies.intentloading;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.intents.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;

import java.util.Map;

public class BakeryDepartmentIntentLoadingStrategy extends IntentLoaderStrategy {
    @Override
    public void load(LexRequest request, Map<String, Object> slots) {
        readDepartmentSlots(request, slots, BakeryDepartmentIntent.Slot.Product, BakeryDepartmentIntent.Slot.Amount, BakeryDepartmentIntent.Slot.Unit);
    }
}
