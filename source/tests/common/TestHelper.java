package common;

import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import org.junit.Assert;

import java.util.List;

public class TestHelper {
    public static void assertContains(List<ShoppingCart> list, ShoppingCart cart) {
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).toString().equals(cart.toString()))
                return;
        }
        Assert.fail("Not exist: " + cart.toString());
    }
}
