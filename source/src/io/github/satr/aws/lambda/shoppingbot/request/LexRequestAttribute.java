package io.github.satr.aws.lambda.shoppingbot.request;
// Copyright © 2017, github.com/satr, MIT License

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
    public static final String UserId = "userId";
    public static final String InputTranscript = "inputTranscript";

    public final class SessionAttribute{
        public static final String FirstName = "FirstName";
        public static final String LastName = "LastName";
        public static final String UserId = "UserId";
    }

    public final class InvocationSourceValue{
        public static final String FulfillmentCodeHook = "FulfillmentCodeHook";
        public static final String DialogCodeHook = "DialogCodeHook";
    }

    public final class OutputDialogModeValue{
        public static final String Text = "Text";
        public static final String Voice = "Voice";
    }

    public final class ConfirmationStatusValue {
        public static final String None = "None";
        public static final String Confirmed = "Confirmed";
        public static final String Denied = "Denied";
    }
}
