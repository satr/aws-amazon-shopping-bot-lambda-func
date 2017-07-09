package io.github.satr.aws.lambda.shoppingbot.repositories;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.entities.User;

import java.util.List;

public interface UserRepository extends Repository {
    List<User> getAllUsers();
    User getUserById(String userId);
    List<User> getUserByName(String firstName, String lastName);
    void save(User user);
}
