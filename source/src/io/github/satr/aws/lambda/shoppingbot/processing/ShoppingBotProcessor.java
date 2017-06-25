package io.github.satr.aws.lambda.shoppingbot.processing;

import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.intent.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intent.GreetingsIntent;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.GreetingsProcessingStrategy;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.RequestDepartmentProductProcessingStrategy;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.IntentProcessingStrategy;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.UnsupportedIntentProcessingStrategy;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartServiceImpl;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;
import io.github.satr.aws.lambda.shoppingbot.services.UserServiceImpl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ShoppingBotProcessor {
    private final Map<String, IntentProcessingStrategy> processingStrategies = new LinkedHashMap<>();
    private final IntentProcessingStrategy unsupportedIntentProcessingStrategy = new UnsupportedIntentProcessingStrategy();

    public ShoppingBotProcessor(UserService userService, ShoppingCartService shoppingCartService) {
        processingStrategies.put(BakeryDepartmentIntent.Name, new RequestDepartmentProductProcessingStrategy(shoppingCartService));
        processingStrategies.put(GreetingsIntent.Name, new GreetingsProcessingStrategy(userService));
    }

    public LexResponse Process(LexRequest lexRequest) {
        if(lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.SessionId) == null)
            lexRequest.setSessionAttribute(LexRequestAttribute.SessionAttribute.SessionId, UUID.randomUUID().toString());
        return getProcessingStrategy(lexRequest.getIntentName()).Process(lexRequest);
    }

    private IntentProcessingStrategy getProcessingStrategy(String intentName) {
        return processingStrategies.containsKey(intentName) ? processingStrategies.get(intentName) : unsupportedIntentProcessingStrategy;
    }
}
