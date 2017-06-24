package io.github.satr.aws.lambda.shoppingbot;

import io.github.satr.aws.lambda.shoppingbot.data.RepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.Message;

import java.util.Map;

public class ShoppingBotProcessor {
    private RepositoryFactory repositoryFactory;

    public ShoppingBotProcessor(RepositoryFactory repositoryFactory) {

        this.repositoryFactory = repositoryFactory;
    }

    public LexResponse Process(LexRequest lexRequest) {

        if(!lexRequest.requestedAmountIsSet() || !lexRequest.requestedProductIsSet())
            return createFailureLexResponse("Product or amount are not requested.", lexRequest.getSessionAttributes());

        Message message = new Message(Message.ContentType.PlainText, buildProductRequestContent(lexRequest));
        DialogAction dialogAction = new DialogAction(DialogAction.Type.Close, DialogAction.FulfillmentState.Fulfilled, message);
        LexResponse lexResponse = new LexResponse(dialogAction, lexRequest.getSessionAttributes());
        return lexResponse;
    }

    private String buildProductRequestContent(LexRequest lexRequest) {
        String requestedUnit = lexRequest.getRequestedUnit();
        return requestedUnit != null && requestedUnit.length() > 0
                ? String.format("You requested: %s %s of %s.", lexRequest.getRequestedAmount(), requestedUnit, lexRequest.getRequestedProduct())
                : String.format("You requested: %s %s.", lexRequest.getRequestedAmount(), lexRequest.getRequestedProduct());
    }

    public static LexResponse createFailureLexResponse(String message, Map<String, Object> sessionAttributes) {
        return new LexResponse(new DialogAction(DialogAction.Type.Close, DialogAction.FulfillmentState.Failed,
                new Message(Message.ContentType.PlainText, message)), sessionAttributes);
    }


}
