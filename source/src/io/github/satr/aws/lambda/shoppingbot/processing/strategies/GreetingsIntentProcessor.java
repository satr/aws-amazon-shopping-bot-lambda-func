package io.github.satr.aws.lambda.shoppingbot.processing.strategies;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.entities.User;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.request.UserIdType;
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
        User user = tryGetUserBy(lexRequest.getUserId(), lexRequest.getUserIdType());

        if(areSameNamesAsInSession(lexRequest, firstName, lastName)) {
            if(user == null || user.sameNamesAs(firstName, lastName))
                return String.format("Yes %s, what can I do for you?", firstName);
            if(user != null && !user.sameNamesAs(firstName, lastName))
                changeUserNames(firstName, lastName, user);
        }

        String welcomeMessage = "";
        if(user == null)
            user = userService.getUserByName(firstName, lastName);
        if (user == null){
            //TODO:how to set user-id for existing user?
            // || (lexRequest.hasValidUserId() && !user.hasUserId(lexRequest.getUserId(), lexRequest.getUserIdType()))) {
            user = addNewUser(firstName, lastName, lexRequest);
            welcomeMessage = String.format("Nice to meet you %s %s. What would you like to buy?", firstName, lastName);
            String newUserMessage = String.format("A new user has been registered: %s %s (UserId:\"%s\")", firstName, lastName, user.getUserId());
            if(lexRequest.hasValidUserId())
                newUserMessage += String.format(";%s:\"%s\"", lexRequest.getUserIdType(), lexRequest.getUserId());
            logger.log(newUserMessage);
        } else {
            welcomeMessage = String.format("It's nice to see you again %s %s. What would you like to buy today?", firstName, lastName);
            logger.log(String.format("An existing user has been recognized: %s %s (%s)", firstName, lastName, user.getUserId()));
        }
        overrideSessionAttributesWithNonEmptyNames(lexRequest, user);
        return welcomeMessage;
    }

    private User tryGetUserBy(String userId, UserIdType userIdType) {
        if (isEmpty(userId))
            return null;
        if(userIdType == UserIdType.Facebook)
            return userService.getUserByFacebookId(userId);
        return null;
    }

    private boolean areSameNamesAsInSession(LexRequest lexRequest, String firstName, String lastName) {
        String sessionFirstName = (String) lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.FirstName);
        String sessionLastName = (String) lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.LastName);
        return !isEmpty(firstName) && firstName.equals(sessionFirstName) && !isEmpty(lastName) && lastName.equals(sessionLastName);
    }

    private void changeUserNames(String firstName, String lastName, User user) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userService.save(user);
        logger.log(String.format("Changed name for the user with Id %s: %s %s", user.getUserId(), user.getFirstName(), user.getLastName()));
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    private User addNewUser(String firstName, String lastName, LexRequest lexRequest) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        String userId = lexRequest.getUserId();
        if(!isEmpty(userId)) {
            if(lexRequest.getUserIdType() == UserIdType.Facebook)
                user.setFacebookId(userId);
            //TODO: add more settings
        }
        userService.save(user);
        return user;
    }

    private void overrideSessionAttributesWithNonEmptyNames(LexRequest lexRequest, User user) {
        lexRequest.setSessionAttribute(LexRequestAttribute.SessionAttribute.UserId, user.getUserId());
        lexRequest.setSessionAttribute(LexRequestAttribute.SessionAttribute.FirstName, user.getFirstName());
        lexRequest.setSessionAttribute(LexRequestAttribute.SessionAttribute.LastName, user.getLastName());
    }
}
