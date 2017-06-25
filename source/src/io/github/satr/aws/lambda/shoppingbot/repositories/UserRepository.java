package io.github.satr.aws.lambda.shoppingbot.repositories;

import io.github.satr.aws.lambda.shoppingbot.entity.User;

import java.util.List;

public interface UserRepository extends Repository {
    List<User> getAllUsers();
    User getUserById(String userId);
    List<User> getUserByName(String firstName, String lastName);
    void save(User user);
}
