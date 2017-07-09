package io.github.satr.aws.lambda.shoppingbot.processing.strategies;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.common.OperationValueResult;
import io.github.satr.aws.lambda.shoppingbot.common.OperationValueResultImpl;
import io.github.satr.aws.lambda.shoppingbot.entities.User;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;

public abstract class UserSessionIntentProcessor extends IntentProcessor {
    protected UserService userService;

    public UserSessionIntentProcessor(UserService userService, Logger logger) {
        super(logger);
        this.userService = userService;
    }

    protected OperationValueResult<User> getUser(LexRequest lexRequest) {
        OperationValueResultImpl<User> operationResult = new OperationValueResultImpl<>();
        String userId = (String) lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserId);
        if(userId == null || userId.length() == 0) {
            operationResult.addError("I'm sorry, could you please tell your name?");//Probably not good error message
            return operationResult;
        }

        User user = userService.getUserById(userId);
        if(user == null) {
            operationResult.addError("I'm sorry, could you please repeat your name?");//Probably not good error message
            return operationResult;
        }

        operationResult.setValue(user);
        return operationResult;
    }
}
