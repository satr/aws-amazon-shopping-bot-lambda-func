package io.github.satr.aws.lambda.shoppingbot.services;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.entity.User;

public interface UserService {
    User getUserById(String userId);
    User getUserByName(String firstName, String lastName);
    void save(User user);
}
