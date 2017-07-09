package io.github.satr.aws.lambda.shoppingbot.common;
// Copyright © 2017, github.com/satr, MIT License

public class OperationResultImpl implements OperationResult {
    private StringBuilder errors = new StringBuilder();

    @Override
    public void addError(String message) {
        errors.append(message + " \n");
    }

    @Override
    public boolean isFailed() {
        return errors.length() > 0;
    }

    @Override
    public String getErrorsAsString() {
        return errors.toString();
    }
}
