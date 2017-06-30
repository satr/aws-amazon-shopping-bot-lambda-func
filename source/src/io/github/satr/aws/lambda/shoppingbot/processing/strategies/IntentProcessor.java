package io.github.satr.aws.lambda.shoppingbot.processing.strategies;

import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;

public interface IntentProcessor {
    LexResponse Process(LexRequest lexRequest);
}
