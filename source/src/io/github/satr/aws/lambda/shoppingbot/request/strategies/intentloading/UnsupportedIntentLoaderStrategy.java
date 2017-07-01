package io.github.satr.aws.lambda.shoppingbot.request.strategies.intentloading;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import java.util.Map;

public class UnsupportedIntentLoaderStrategy extends IntentLoaderStrategy {
    @Override
    public void load(LexRequest request, Map<String, Object> slots) {
        request.setError("Not recognized request.");
    }
}
