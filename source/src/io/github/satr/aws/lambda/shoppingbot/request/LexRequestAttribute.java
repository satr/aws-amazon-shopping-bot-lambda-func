package io.github.satr.aws.lambda.shoppingbot.request;

public final class LexRequestAttribute {
    public static final String Bot = "bot";
    public static final String BotName = "name";
    public static final String CurrentIntent = "currentIntent";
    public static final String CurrentIntentName = "name";
    public static final String ConfirmationStatus = "confirmationStatus";
    public static final String Slots = "slots";
    public static final String InvocationSource = "invocationSource";
    public static final String OutputDialogMode = "outputDialogMode";
    public static final String SessionAttributes = "sessionAttributes";
    public final class SessionAttribute{
        public static final String FirstName = "FirstName";
        public static final String LastName = "LastName";
        public static final String UserId = "UserId";
    }
}
