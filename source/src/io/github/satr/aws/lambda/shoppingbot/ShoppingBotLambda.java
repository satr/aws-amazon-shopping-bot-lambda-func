package io.github.satr.aws.lambda.shoppingbot;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestFactory;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.Message;

import java.util.Map;

public class ShoppingBotLambda implements RequestHandler<Map<String, Object>, LexResponse> {

    @Override
    public LexResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            LexRequest lexRequest = LexRequestFactory.readFromMap(input);
            if(lexRequest.hasError())
                return createFailureLexResponse(lexRequest.getError());
            if(!lexRequest.requestedAmountIsSet() || !lexRequest.requestedProductIsSet())
                return createFailureLexResponse("Product or amount are not requested.");

            String content = String.format("You requested: %s, amount: %s", lexRequest.getRequestedProduct(), lexRequest.getRequestedAmount());

            Message message = new Message(Message.ContentType.PlainText, content);
            DialogAction dialogAction = new DialogAction(DialogAction.Type.Close,
                                                            DialogAction.FulfillmentState.Fulfilled,
                                                            message);
            return new LexResponse(dialogAction);
        } catch (Exception e) {
            e.printStackTrace();
            return createFailureLexResponse("Error: " + e.getMessage());
        }
    }

    private LexResponse createFailureLexResponse(String message) {
        return new LexResponse(new DialogAction(DialogAction.Type.Close, DialogAction.FulfillmentState.Failed,
                                                new Message(Message.ContentType.PlainText, message)));
    }
}

