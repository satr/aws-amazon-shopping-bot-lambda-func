package io.github.satr.aws.lambda.shoppingbot.request.strategies.intentloading;

import io.github.satr.aws.lambda.shoppingbot.intent.VegetableDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;

import java.util.Map;

public class VegetableDepartmentIntentLoadingStrategy extends IntentLoaderStrategy {
    @Override
    public void load(LexRequest request, Map<String, Object> slots) {
        readDepartmentSlots(request, slots, VegetableDepartmentIntent.Slot.Product, VegetableDepartmentIntent.Slot.Amount, VegetableDepartmentIntent.Slot.Unit);
    }
}
