package io.github.satr.aws.lambda.shoppingbot.intent;

public class GreetingsIntent {
    public static final String Name = "Greetings";

    public class Slot {
        public static final String FirstName = "FirstName";
        public static final String LastName = "LastName";
        public static final String SessionId = "SessionId";
    }
}
