package io.github.satr.aws.lambda.shoppingbot.services;

import com.sun.istack.internal.NotNull;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.repositories.UserRepository;

public class UserServiceImpl extends Service implements UserService {
    public UserServiceImpl(@NotNull UserRepository userRepository, @NotNull Logger logger) {
        super(logger);
    }
}
