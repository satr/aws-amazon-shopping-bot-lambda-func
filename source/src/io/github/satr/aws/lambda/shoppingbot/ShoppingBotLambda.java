package io.github.satr.aws.lambda.shoppingbot;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.github.satr.aws.lambda.shoppingbot.log.CompositeLogger;
import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactoryImpl;
import io.github.satr.aws.lambda.shoppingbot.processing.ShoppingBotProcessor;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestFactory;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;
import io.github.satr.aws.lambda.shoppingbot.services.*;

import java.util.Map;

public class ShoppingBotLambda implements RequestHandler<Map<String, Object>, LexResponse> {

    private final RepositoryFactory repositoryFactory;
    private ShoppingBotProcessor shoppingBotProcessor;
    private CompositeLogger logger = new CompositeLogger();

    public ShoppingBotLambda() {
        repositoryFactory = new RepositoryFactoryImpl();
        UserServiceImpl userService = new UserServiceImpl(repositoryFactory.createUserRepository(), logger);
        init(userService,
             new ShoppingCartServiceImpl(repositoryFactory.createShoppingCartRepository(), userService, this.logger),
             new ProductServiceImpl(repositoryFactory.createProductRepository(), this.logger),
             new OrderServiceImpl(repositoryFactory.createOrderRepository(), this.logger));
    }

    public ShoppingBotLambda(RepositoryFactory repositoryFactory, UserService userService, ShoppingCartService shoppingCartService, ProductService productService) {
        this.repositoryFactory = repositoryFactory;
        init(userService, shoppingCartService, productService, new OrderServiceImpl(repositoryFactory.createOrderRepository(), this.logger));
    }

    private void init(UserService userService, ShoppingCartService shoppingCartService, ProductService productService, OrderService orderService) {
        this.shoppingBotProcessor = new ShoppingBotProcessor(userService, shoppingCartService, productService, orderService, logger);
    }

    @Override
    public LexResponse handleRequest(Map<String, Object> input, Context context) {
        if(context != null)
            logger.setLambdaLogger(context.getLogger());

        LexRequest lexRequest = null;
        try {
            lexRequest = LexRequestFactory.createFromMap(input);
            LexResponse lexRespond = shoppingBotProcessor.Process(lexRequest);
            logStatus(lexRespond);
            return lexRespond;
        } catch (Exception e) {
            logger.log(e);
            return LexResponseHelper.createFailedLexResponse("Error: " + e.getMessage(), lexRequest);
        }
    }

    private void logStatus(LexResponse lexRespond) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("FulfillmentState: %s\n", lexRespond.getDialogAction().getFulfillmentState()));
        builder.append("Session:\n");
        Map<String, Object> sessionAttributes = lexRespond.getSessionAttributes();
        for (String key: sessionAttributes.keySet())
            builder.append(String.format("  %s:\"%s\"\n", key, sessionAttributes.get(key)));
        logger.log(builder.toString());
    }

    @Override
    public void finalize() {
        repositoryFactory.shutdown();
    }
}

