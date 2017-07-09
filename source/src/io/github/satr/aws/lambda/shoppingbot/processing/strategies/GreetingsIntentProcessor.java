package io.github.satr.aws.lambda.shoppingbot.processing.strategies;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.entities.User;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;

public class GreetingsIntentProcessor extends IntentProcessor {
    private UserService userService;

    public GreetingsIntentProcessor(UserService userService, Logger logger) {
        super(logger);
        this.userService = userService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        if(!lexRequest.firstNameIsSet() || !lexRequest.lastNameIsSet())
            return createLexErrorResponse(lexRequest, "First name or last name are not specified.");

        String welcomeMessage = processNames(lexRequest);
        return LexResponseHelper.createLexResponse(lexRequest, welcomeMessage,
                DialogAction.Type.Close,
                DialogAction.FulfillmentState.Fulfilled);
    }

    private String processNames(LexRequest lexRequest) {
        String firstName = lexRequest.getFirstName();
        String lastName = lexRequest.getLastName();
        if(!isEmpty(firstName) && firstName.equals(lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.FirstName))
            && !isEmpty(lastName) && lastName.equals(lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.LastName))) {
            return String.format("Yes %s, what can I do for you?", firstName);
        }

        String welcomeMessage = "";
        User user = userService.getUserByName(firstName, lastName);
        if (user == null) {
            user = addNewUser(firstName, lastName);
            welcomeMessage = String.format("Nice to meet you %s %s. What would you like to buy?", firstName, lastName);
            logger.log(String.format("A new user has been registered: %s %s (%s)", firstName, lastName, user.getUserId()));
        } else {
            welcomeMessage = String.format("It's nice to see you again %s %s. What would you like to buy today?", firstName, lastName);
            logger.log(String.format("An existing user has been recognized: %s %s (%s)", firstName, lastName, user.getUserId()));
        }
        overrideSessionAttributesWithNonEmptyNames(lexRequest, user);
        return welcomeMessage;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    private User addNewUser(String firstName, String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userService.save(user);
        return user;
    }

    private void overrideSessionAttributesWithNonEmptyNames(LexRequest lexRequest, User user) {
        lexRequest.setSessionAttribute(LexRequestAttribute.SessionAttribute.UserId, user.getUserId());
        lexRequest.setSessionAttribute(LexRequestAttribute.SessionAttribute.FirstName, user.getFirstName());
        lexRequest.setSessionAttribute(LexRequestAttribute.SessionAttribute.LastName, user.getLastName());
    }
}
