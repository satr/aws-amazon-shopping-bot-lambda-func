package io.github.satr.aws.lambda.shoppingbot.processing;

import io.github.satr.aws.lambda.shoppingbot.data.RepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.intent.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intent.GreetingsIntent;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.GreetingsProcessingStrategy;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.RequestDepartmentProductProcessingStrategy;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.IntentProcessingStrategy;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.UnsupportedIntentProcessingStrategy;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ShoppingBotProcessor {
    private RepositoryFactory repositoryFactory;
    private final static Map<String, IntentProcessingStrategy> processingStrategies = new LinkedHashMap<>();
    private final static IntentProcessingStrategy unsupportedIntentProcessingStrategy = new UnsupportedIntentProcessingStrategy();

    static {
        processingStrategies.put(BakeryDepartmentIntent.Name, new RequestDepartmentProductProcessingStrategy());
        processingStrategies.put(GreetingsIntent.Name, new GreetingsProcessingStrategy());
    }

    public ShoppingBotProcessor(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
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
