package io.github.satr.aws.lambda.shoppingbot.response;

public class LexResponse {
    private DialogAction dialogAction;

    public LexResponse(DialogAction dialogAction) {

        this.dialogAction = dialogAction;
    }

    public DialogAction getDialogAction() {
        return dialogAction;
    }

    public void setDialogAction(DialogAction dialogAction) {
        this.dialogAction = dialogAction;
    }
}
