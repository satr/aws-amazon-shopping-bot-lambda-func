package common;
// Copyright © 2017, github.com/satr, MIT License

import org.junit.Assert;

import java.util.List;

public class TestHelper {
    public static void assertContains(List list, Object cart) {
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).toString().equals(cart.toString()))
                return;
        }
        Assert.fail("Not exist: " + cart.toString());
    }
}
