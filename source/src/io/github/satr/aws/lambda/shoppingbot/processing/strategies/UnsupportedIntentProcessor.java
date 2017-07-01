package io.github.satr.aws.lambda.shoppingbot.processing.strategies;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;

public class UnsupportedIntentProcessor extends IntentProcessor {
    public UnsupportedIntentProcessor(Logger logger) {
        super(logger);
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        return createLexErrorResponse(lexRequest, "Request is not recognized");
    }
}
