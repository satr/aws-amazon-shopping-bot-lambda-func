package io.github.satr.aws.lambda.shoppingbot.services;
// Copyright © 2017, github.com/satr, MIT License

import com.sun.istack.internal.NotNull;
import io.github.satr.aws.lambda.shoppingbot.entities.User;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.repositories.UserRepository;

import java.util.List;

public class UserServiceImpl extends Service implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(@NotNull UserRepository userRepository, @NotNull Logger logger) {
        super(logger);
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public User getUserByFacebookId(String facebookId) {
        return userRepository.getUserByFacebookId(facebookId);
    }

    @Override
    public User getUserByName(String firstName, String lastName) {
        List<User> users = userRepository.getUserByName(firstName, lastName);
        if(users.size() == 0)
            return null;
        return users.get(0);//TODO: implement a strategy to specify users by address or other criteria
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
