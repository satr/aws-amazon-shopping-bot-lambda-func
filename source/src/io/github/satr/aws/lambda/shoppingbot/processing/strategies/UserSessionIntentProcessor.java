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
    protected final UserService userService;
    private static final String ERROR_MESSAGE = "I'm sorry, could you please tell your full name?";//Probably not good error message

    public UserSessionIntentProcessor(UserService userService, Logger logger) {
        super(logger);
        this.userService = userService;
    }

    protected OperationValueResult<User> getUser(LexRequest lexRequest) {
        OperationValueResultImpl<User> operationResult = new OperationValueResultImpl<>();
        String sessionUserId = (String) lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserId);
        boolean userIdIsEmpty = isEmpty(lexRequest.getUserId());
        boolean sessionUserIdIsEmpty = isEmpty(sessionUserId);
        if(sessionUserIdIsEmpty && userIdIsEmpty) {
            operationResult.addError(ERROR_MESSAGE);
            return operationResult;
        }
        User user = null;
        if(!sessionUserIdIsEmpty)
            user = userService.getUserById(sessionUserId);
        if(user == null && lexRequest.hasValidUserId()) {
            if (lexRequest.getUserIdType() == UserIdType.Facebook)
                user = userService.getUserByFacebookId(lexRequest.getUserId());
        }

        if(user == null) {
            operationResult.addError(ERROR_MESSAGE);
            return operationResult;
        }

        operationResult.setValue(user);
        return operationResult;
    }
}
