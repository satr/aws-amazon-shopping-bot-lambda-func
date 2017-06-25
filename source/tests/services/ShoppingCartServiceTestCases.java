package services;

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.repositories.ShoppingCartRepository;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShoppingCartServiceTestCases {

    private ShoppingCartRepository shoppingCartRepositoryMock;
    private ShoppingCartServiceImpl shoppingCartService;
    private Logger logger = Mockito.mock(Logger.class);

    @Before
    public void setUp() throws Exception {
        shoppingCartRepositoryMock = Mockito.mock(ShoppingCartRepository.class);
        shoppingCartService = new ShoppingCartServiceImpl(shoppingCartRepositoryMock, logger);
    }

    @Test
    public void deletedExtraCartsWithSameSessionId() throws Exception {
        String sessionId = ObjectMother.createRandomString();
        ArrayList<ShoppingCart> carts = new ArrayList<>();
        ShoppingCart cart2001 = ObjectMother.createShoppingCart(sessionId, 2001);
        ShoppingCart cart2010 = ObjectMother.createShoppingCart(sessionId, 2010);
        ShoppingCart cart2005 = ObjectMother.createShoppingCart(sessionId, 2005);
        carts.add(cart2001);
        carts.add(cart2010);
        carts.add(cart2005);

        when(shoppingCartRepositoryMock.getShoppingCartsBySessionId(sessionId)).thenReturn(carts);

        ShoppingCart cart = shoppingCartService.getShoppingCartBySessionId(sessionId);
        assertEquals(2010, cart.getUpdatedOnAsDate().getYear());
        verify(shoppingCartRepositoryMock, times(1)).delete(cart2001);
        verify(shoppingCartRepositoryMock, times(1)).delete(cart2005);
    }
}
