package io.github.satr.aws.lambda.shoppingbot;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.github.satr.aws.lambda.shoppingbot.data.RepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.data.RepositoryFactoryImpl;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestFactory;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.Message;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingBotLambda implements RequestHandler<Map<String, Object>, LexResponse> {

    private ShoppingBotProcessor shoppingBotProcessor;

    public ShoppingBotLambda() {
        this(new ShoppingBotProcessor(new RepositoryFactoryImpl()));
    }

    public ShoppingBotLambda(ShoppingBotProcessor shoppingBotProcessor) {
        this.shoppingBotProcessor = shoppingBotProcessor;
    }

    @Override
    public LexResponse handleRequest(Map<String, Object> input, Context context) {
        LexRequest lexRequest = null;
        try {
            lexRequest = LexRequestFactory.createFromMap(input);
            return shoppingBotProcessor.Process(lexRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> sessionAttributes = lexRequest != null ? lexRequest.getSessionAttributes() : new LinkedHashMap<>();
            return ShoppingBotProcessor.createFailureLexResponse("Error: " + e.getMessage(), sessionAttributes);
        }
    }
}

