package io.github.satr.aws.lambda.shoppingbot.processing.strategies;

import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;

public class UnsupportedIntentProcessor implements IntentProcessor {
    @Override
    public LexResponse Process(LexRequest lexRequest) {
        return LexResponseHelper.createFailedLexResponse("Request is not recognized", lexRequest);
    }
}
