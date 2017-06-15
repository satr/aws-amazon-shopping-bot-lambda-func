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
        LexRequest lexRequest = LexRequestFactory.readFromMap(input);
        try {
            String content = "Product is not requested.";
            if(lexRequest.isProductRequested())
                content = String.format("You requested: %s, amount: %s", lexRequest.getRequestedProduct(), lexRequest.getAmount());
            else if(lexRequest.hasError())
                content = lexRequest.getError();

            Message message = new Message(Message.ContentType.PlainText, content);
            DialogAction dialogAction = new DialogAction(DialogAction.Type.Close,
                    DialogAction.FulfillmentState.Fulfilled,
                    message);
            LexResponse response = new LexResponse(dialogAction);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new LexResponse(new DialogAction(DialogAction.Type.Close,null,new Message(null,null)));
        }
    }
}

