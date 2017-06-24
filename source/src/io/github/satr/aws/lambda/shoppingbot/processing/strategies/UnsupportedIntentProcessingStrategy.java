package io.github.satr.aws.lambda.shoppingbot.processing.strategies;

import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;

public class UnsupportedIntentProcessingStrategy implements IntentProcessingStrategy {
    @Override
    public LexResponse Process(LexRequest lexRequest) {
        return LexResponseHelper.createFailedLexResponse("Request is not recognized", lexRequest);
    }
}
