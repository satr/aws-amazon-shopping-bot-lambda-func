package services;
// Copyright © 2017, github.com/satr, MIT License

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.entities.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.entities.User;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.repositories.ShoppingCartRepository;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartServiceImpl;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class ShoppingCartServiceTestCases {

    private final User user = ObjectMother.createUser();
    private ShoppingCartRepository shoppingCartRepositoryMock;
    private ShoppingCartServiceImpl shoppingCartService;
    private Logger logger = Mockito.mock(Logger.class);
    private UserService userServiceMock;
    private ShoppingCart dbShoppingCart;

    @Before
    public void setUp() throws Exception {
        shoppingCartRepositoryMock = Mockito.mock(ShoppingCartRepository.class);
        userServiceMock = Mockito.mock(UserService.class);
        when(userServiceMock.getUserById(user.getUserId())).thenReturn(user);
        dbShoppingCart = new ShoppingCart();
        dbShoppingCart.setUserId(user.getUserId());
        shoppingCartService = new ShoppingCartServiceImpl(shoppingCartRepositoryMock, userServiceMock, logger);
    }

    @Test
    public void loadedShoppingCartHasUser() throws Exception {
        when(shoppingCartRepositoryMock.getShoppingCartByUserId(user.getUserId())).thenReturn(dbShoppingCart);

        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(user.getUserId());

        assertEquals(user.getUserId(), shoppingCart.getUserId());
        assertNotNull(shoppingCart.getUser());
    }

    @Test
    public void returnNullForNotExistingUserId() throws Exception {
        when(shoppingCartRepositoryMock.getShoppingCartByUserId(user.getUserId())).thenReturn(dbShoppingCart);

        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(ObjectMother.createRandomString());

        assertNull(shoppingCart);
    }

    @Test
    public void returnNullForExistingUserAndNotExistingShoppingCart() throws Exception {
        when(shoppingCartRepositoryMock.getShoppingCartByUserId(user.getUserId())).thenReturn(null);

        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(user.getUserId());

        assertNull(shoppingCart);
    }

}
