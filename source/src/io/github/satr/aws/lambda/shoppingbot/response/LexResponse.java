package io.github.satr.aws.lambda.shoppingbot.response;

import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;

import java.util.LinkedHashMap;
import java.util.Map;

public class LexResponse {
    private DialogAction dialogAction;
    private Map<String, Object> sessionAttributes = new LinkedHashMap<>();
    public LexResponse(DialogAction dialogAction, Map<String, Object> sessionAttributes) {

        this.dialogAction = dialogAction;
        this.sessionAttributes = sessionAttributes != null ? sessionAttributes : new LinkedHashMap<>();
    }

    public DialogAction getDialogAction() {
        return dialogAction;
    }

    public void setDialogAction(DialogAction dialogAction) {
        this.dialogAction = dialogAction;
    }

    public Object getSessionAttribute(String attributeName) {
        return sessionAttributes.containsKey(attributeName) ? sessionAttributes.get(attributeName) : null;
    }
}
