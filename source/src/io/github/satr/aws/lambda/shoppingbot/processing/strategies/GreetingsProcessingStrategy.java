package io.github.satr.aws.lambda.shoppingbot.processing.strategies;

import io.github.satr.aws.lambda.shoppingbot.intent.GreetingsIntent;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;

import java.util.Map;

public class GreetingsProcessingStrategy implements IntentProcessingStrategy {
    private UserService userService;

    public GreetingsProcessingStrategy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        if(!lexRequest.firstNameIsSet() || !lexRequest.lastNameIsSet())
            return LexResponseHelper.createFailedLexResponse("First name or last name are not specified.", lexRequest);

        overrideSessionAttributesWithNonEmptyNames(lexRequest.getSessionAttributes(), lexRequest.getFirstName(), lexRequest.getLastName());

        return LexResponseHelper.createLexResponse(lexRequest, buildContent(lexRequest),
                DialogAction.Type.Close,
                DialogAction.FulfillmentState.Fulfilled);
    }

    private void overrideSessionAttributesWithNonEmptyNames(Map<String, Object> sessionAttributes, String firstName, String lastName) {
        if(firstName != null)
            sessionAttributes.put(GreetingsIntent.Slot.FirstName, firstName);
        if(lastName != null)
            sessionAttributes.put(GreetingsIntent.Slot.LastName, lastName);
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
