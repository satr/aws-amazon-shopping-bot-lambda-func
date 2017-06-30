package io.github.satr.aws.lambda.shoppingbot.processing;

import io.github.satr.aws.lambda.shoppingbot.intent.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intent.GreetingsIntent;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.GreetingsIntentProcessor;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.OrderProductIntentProcessor;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.IntentProcessor;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.UnsupportedIntentProcessor;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingBotProcessor {
    private final Map<String, IntentProcessor> processingStrategies = new LinkedHashMap<>();
    private final IntentProcessor unsupportedIntentProcessor = new UnsupportedIntentProcessor();

    public ShoppingBotProcessor(UserService userService, ShoppingCartService shoppingCartService) {
        processingStrategies.put(BakeryDepartmentIntent.Name, new OrderProductIntentProcessor(shoppingCartService));
        processingStrategies.put(GreetingsIntent.Name, new GreetingsIntentProcessor(userService));
    }

    public LexResponse Process(LexRequest lexRequest) {
        return getProcessingStrategy(lexRequest.getIntentName()).Process(lexRequest);
    }

    private IntentProcessor getProcessingStrategy(String intentName) {
        return processingStrategies.containsKey(intentName) ? processingStrategies.get(intentName) : unsupportedIntentProcessor;
    }
}
