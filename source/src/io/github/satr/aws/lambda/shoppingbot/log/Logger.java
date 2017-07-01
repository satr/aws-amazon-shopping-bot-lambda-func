package io.github.satr.aws.lambda.shoppingbot.log;
// Copyright © 2017, github.com/satr, MIT License

public interface Logger {
    void log(String message);
    void log(Exception e);
}
