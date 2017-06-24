package io.github.satr.aws.lambda.shoppingbot.request.strategies.intentloading;

import io.github.satr.aws.lambda.shoppingbot.intent.MilkDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;

import java.util.Map;

public class MilkDepartmentIntentLoadingStrategy extends IntentLoaderStrategy {
    @Override
    public void load(LexRequest request, Map<String, Object> slots) {
        readDepartmentSlots(request, slots, MilkDepartmentIntent.Slot.Product, MilkDepartmentIntent.Slot.Amount, MilkDepartmentIntent.Slot.Unit);
    }
}
