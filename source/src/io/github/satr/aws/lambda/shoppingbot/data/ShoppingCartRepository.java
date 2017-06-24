package io.github.satr.aws.lambda.shoppingbot.data;

import com.sun.media.sound.InvalidDataException;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartRepository extends Repository {
    List<ShoppingCart> getList();

    ShoppingCart getById(String cartId) throws InvalidDataException;

    List<ShoppingCart> getByUserId(String userId);
    List<ShoppingCart> getBySessionId(String sessionId);
    void save(ShoppingCart cart);
    void delete(ShoppingCart cart);
}
