package io.github.satr.aws.lambda.shoppingbot.common;
// Copyright © 2017, github.com/satr, MIT License

public interface OperationValueResult<T> extends OperationResult {
    T getValue();
    void setValue(T value);
}
