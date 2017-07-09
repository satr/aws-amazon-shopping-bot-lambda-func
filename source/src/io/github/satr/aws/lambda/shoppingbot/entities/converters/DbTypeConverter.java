package io.github.satr.aws.lambda.shoppingbot.entities.converters;
// Copyright © 2017, github.com/satr, MIT License

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class DbTypeConverter {
    protected final ObjectMapper objectMapper = new ObjectMapper();

    public DbTypeConverter() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
