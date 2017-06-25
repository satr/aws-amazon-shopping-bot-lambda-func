package io.github.satr.aws.lambda.shoppingbot;

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
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartServiceImpl;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;
import io.github.satr.aws.lambda.shoppingbot.services.UserServiceImpl;

import java.util.Map;

public class ShoppingBotLambda implements RequestHandler<Map<String, Object>, LexResponse> {

    private final RepositoryFactory repositoryFactory;
    private ShoppingBotProcessor shoppingBotProcessor;
    private CompositeLogger logger = new CompositeLogger();

    public ShoppingBotLambda() {
        repositoryFactory = new RepositoryFactoryImpl();
        init(new UserServiceImpl(repositoryFactory.createUserRepository(), logger),
                new ShoppingCartServiceImpl(repositoryFactory.createShoppingCartRepository(), this.logger));
    }

    public ShoppingBotLambda(RepositoryFactory repositoryFactory, UserService userService, ShoppingCartService shoppingCartService) {
        this.repositoryFactory = repositoryFactory;
        init(userService, shoppingCartService);
    }

    private void init(UserService userService, ShoppingCartService shoppingCartService) {
        this.shoppingBotProcessor = new ShoppingBotProcessor(userService, shoppingCartService);
    }

    @Override
    public LexResponse handleRequest(Map<String, Object> input, Context context) {
        if(context != null)
            logger.setLambdaLogger(context.getLogger());

        LexRequest lexRequest = null;
        try {
            lexRequest = LexRequestFactory.createFromMap(input);
            return shoppingBotProcessor.Process(lexRequest);
        } catch (Exception e) {
            logger.log(e);
            return LexResponseHelper.createFailedLexResponse("Error: " + e.getMessage(), lexRequest);
        }
    }

    @Override
    public void finalize() {
        repositoryFactory.shutdown();
    }
}

