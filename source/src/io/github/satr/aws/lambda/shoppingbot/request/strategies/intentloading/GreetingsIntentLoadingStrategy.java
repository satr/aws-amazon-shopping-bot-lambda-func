package io.github.satr.aws.lambda.shoppingbot.request.strategies.intentloading;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.intent.GreetingsIntent;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;

import java.util.Map;

public class GreetingsIntentLoadingStrategy extends IntentLoaderStrategy {
    @Override
    public void load(LexRequest request, Map<String, Object> slots) {
        request.setFirstName(getSlotValueFor(slots, GreetingsIntent.Slot.FirstName, null));
        request.setLastName(getSlotValueFor(slots, GreetingsIntent.Slot.LastName, null));
    }
}
