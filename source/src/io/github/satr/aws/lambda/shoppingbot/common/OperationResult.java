package io.github.satr.aws.lambda.shoppingbot.common;
// Copyright © 2017, github.com/satr, MIT License

public interface OperationResult {
    void addError(String message);
    boolean isFailed();
    String getErrorsAsString();
}
