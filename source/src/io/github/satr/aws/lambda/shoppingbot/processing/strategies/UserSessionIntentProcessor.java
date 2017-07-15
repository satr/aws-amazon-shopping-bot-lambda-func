package io.github.satr.aws.lambda.shoppingbot.processing.strategies;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.common.OperationValueResult;
import io.github.satr.aws.lambda.shoppingbot.common.OperationValueResultImpl;
import io.github.satr.aws.lambda.shoppingbot.entities.User;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.request.UserIdType;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;

import static org.apache.http.util.TextUtils.isEmpty;

public abstract class UserSessionIntentProcessor extends IntentProcessor {
    protected UserService userService;

    public UserSessionIntentProcessor(UserService userService, Logger logger) {
        super(logger);
        this.userService = userService;
    }

    protected OperationValueResult<User> getUser(LexRequest lexRequest) {
        String errorMessage = "I'm sorry, could you please tell your full name?";//Probably not good error message
        OperationValueResultImpl<User> operationResult = new OperationValueResultImpl<>();
        String sessionUserId = (String) lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserId);
        boolean userIdIsEmpty = isEmpty(lexRequest.getUserId());
        boolean sessionUserIdIsEmpty = isEmpty(sessionUserId);
        if(sessionUserIdIsEmpty && userIdIsEmpty) {
            operationResult.addError(errorMessage);
            return operationResult;
        }
        User user = null;
        if(!sessionUserIdIsEmpty)
            user = userService.getUserById(sessionUserId);
        if(user == null && lexRequest.hasValidUserId()) {//TODO
            if (lexRequest.getUserIdType() == UserIdType.Facebook)
                user = userService.getUserById(sessionUserId);
        }

        if(user == null) {
            operationResult.addError(errorMessage);
            return operationResult;
        }

        operationResult.setValue(user);
        return operationResult;
    }
}
