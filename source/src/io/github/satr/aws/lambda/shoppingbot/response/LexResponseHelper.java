package io.github.satr.aws.lambda.shoppingbot.response;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import java.util.LinkedHashMap;
import java.util.Map;

public final class LexResponseHelper {
    public static LexResponse createFailedLexResponse(String message, LexRequest lexRequest) {
        Map<String, Object> sessionAttributes = lexRequest != null ? lexRequest.getSessionAttributes() : new LinkedHashMap<>();
        return new LexResponse(new DialogAction(DialogAction.Type.Close, DialogAction.FulfillmentState.Failed,
                new Message(Message.ContentType.PlainText, message)), sessionAttributes);
    }

    public static LexResponse createLexResponse(LexRequest lexRequest, String content, String type, String fulfillmentState) {
        Message message = new Message(Message.ContentType.PlainText, content);
        DialogAction dialogAction = new DialogAction(type, fulfillmentState, message);
        return new LexResponse(dialogAction, lexRequest.getSessionAttributes());
    }

}
