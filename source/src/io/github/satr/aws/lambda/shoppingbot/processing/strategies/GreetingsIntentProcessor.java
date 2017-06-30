package io.github.satr.aws.lambda.shoppingbot.processing.strategies;

import io.github.satr.aws.lambda.shoppingbot.entity.User;
import io.github.satr.aws.lambda.shoppingbot.intent.GreetingsIntent;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;

import java.util.Map;

public class GreetingsIntentProcessor implements IntentProcessor {
    private UserService userService;

    public GreetingsIntentProcessor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        if(!lexRequest.firstNameIsSet() || !lexRequest.lastNameIsSet())
            return LexResponseHelper.createFailedLexResponse("First name or last name are not specified.", lexRequest);
        String firstName = lexRequest.getFirstName();
        String lastName = lexRequest.getLastName();
        User user = userService.getUserByName(firstName, lastName);
        if(user == null)
            user = addNewUser(firstName, lastName);
        overrideSessionAttributesWithNonEmptyNames(lexRequest.getSessionAttributes(), user);

        return LexResponseHelper.createLexResponse(lexRequest, buildContent(lexRequest),
                DialogAction.Type.Close,
                DialogAction.FulfillmentState.Fulfilled);
    }

    private User addNewUser(String firstName, String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userService.save(user);
        return user;
    }

    private void overrideSessionAttributesWithNonEmptyNames(Map<String, Object> sessionAttributes, User user) {
        sessionAttributes.put(LexRequestAttribute.SessionAttribute.UserId, user.getUserId());
        sessionAttributes.put(LexRequestAttribute.SessionAttribute.FirstName, user.getFirstName());
        sessionAttributes.put(LexRequestAttribute.SessionAttribute.LastName, user.getLastName());
    }

    private String buildContent(LexRequest lexRequest) {
        String address = lexRequest.getAddress();
        return address != null && address.length() > 0
                ? String.format("Hello %s %s from %s.",
                                lexRequest.getFirstName(), lexRequest.getLastName(), lexRequest.getAddress())
                : String.format("Hello %s %s.",
                                lexRequest.getFirstName(), lexRequest.getLastName(), lexRequest.getRequestedProduct());
    }
}
