package io.github.satr.aws.lambda.shoppingbot.processing;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.intent.*;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.processing.strategies.*;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.services.ProductService;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingBotProcessor {
    private final IntentProcessor unsupportedIntentProcessor;
    private final Map<String, IntentProcessor> processingStrategies = new LinkedHashMap<>();

    public ShoppingBotProcessor(UserService userService, ShoppingCartService shoppingCartService, ProductService productService, Logger logger) {
        unsupportedIntentProcessor = new UnsupportedIntentProcessor(logger);
        OrderProductIntentProcessor orderProductIntentProcessor = new OrderProductIntentProcessor(shoppingCartService, userService, productService, logger);
        processingStrategies.put(BakeryDepartmentIntent.Name, orderProductIntentProcessor);
        processingStrategies.put(MilkDepartmentIntent.Name, orderProductIntentProcessor);
        processingStrategies.put(VegetableDepartmentIntent.Name, orderProductIntentProcessor);
        processingStrategies.put(GreetingsIntent.Name, new GreetingsIntentProcessor(userService, logger));
        processingStrategies.put(ReviewShoppingCartIntent.Name, new ReviewShoppingCartIntentProcessor(shoppingCartService, userService, logger));
    }

    public LexResponse Process(LexRequest lexRequest) {
        return getProcessingStrategy(lexRequest.getIntentName()).Process(lexRequest);
    }

    private IntentProcessor getProcessingStrategy(String intentName) {
        return processingStrategies.getOrDefault(intentName, unsupportedIntentProcessor);
    }
}
