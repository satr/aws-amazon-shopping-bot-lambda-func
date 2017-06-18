package io.github.satr.aws.lambda.shoppingbot.data;

import com.sun.media.sound.InvalidDataException;
import io.github.satr.aws.lambda.shoppingbot.entity.User;

import java.util.List;

public interface UserRepository extends Repository {
    List<User> getList();
    User getById(String userId) throws InvalidDataException;
    List<User> getByName(String firstName, String lastName);
}
