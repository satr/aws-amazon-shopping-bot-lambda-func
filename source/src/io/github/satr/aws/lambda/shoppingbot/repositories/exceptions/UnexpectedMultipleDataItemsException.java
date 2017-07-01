package io.github.satr.aws.lambda.shoppingbot.repositories.exceptions;
// Copyright © 2017, github.com/satr, MIT License

public class UnexpectedMultipleDataItemsException extends Exception {
    public UnexpectedMultipleDataItemsException(String message) {
        super(message);
    }
}
