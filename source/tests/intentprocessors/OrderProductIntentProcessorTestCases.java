package intentprocessors;


import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.ShoppingBotLambda;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCartItem;
import io.github.satr.aws.lambda.shoppingbot.entity.User;
import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.repositories.ShoppingCartRepository;
import io.github.satr.aws.lambda.shoppingbot.repositories.UserRepository;
import io.github.satr.aws.lambda.shoppingbot.intent.GreetingsIntent;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;
import io.github.satr.aws.lambda.shoppingbot.services.UserServiceImpl;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class OrderProductIntentProcessorTestCases {
    private ShoppingBotLambda shoppingBotLambda;
    private UserService userServiceMock;
    private ShoppingCartService shoppingCartServiceMock;
    RepositoryFactory repositoryFactoryMock = Mockito.mock(RepositoryFactory.class);

    @org.junit.Before
    public void setUp() throws Exception {
        userServiceMock = Mockito.mock(UserService.class);
        shoppingCartServiceMock = Mockito.mock(ShoppingCartService.class);
        shoppingBotLambda = new ShoppingBotLambda(repositoryFactoryMock, userServiceMock, shoppingCartServiceMock);
    }

    @Test
    public void workflowOrderBread() throws Exception {
        String product = ObjectMother.createRandomString();
        Double amount = ObjectMother.createRandomNumber();
        String unit = ObjectMother.createRandomString();
        LinkedHashMap<String, Object> requestToBuyBread = ObjectMother.createRequestForBakeryDepartment(product, amount, unit);
        String userId = ObjectMother.setSessionAttributeWithRundomString(requestToBuyBread, LexRequestAttribute.SessionAttribute.UserId);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestToBuyBread, null);

        assertEquals(DialogAction.FulfillmentState.Fulfilled, lexResponse.getDialogAction().getFulfillmentState());

        Mockito.verify(shoppingCartServiceMock, Mockito.atLeastOnce()).getShoppingCartByUserId(userId);
        ArgumentCaptor<ShoppingCart> cartArgument = ArgumentCaptor.forClass(ShoppingCart.class);
        Mockito.verify(shoppingCartServiceMock, Mockito.atLeastOnce()).save(cartArgument.capture());
        ShoppingCart shoppingCart = cartArgument.getValue();
        assertEquals(1, shoppingCart.getItems().size());
        ShoppingCartItem cartItem = shoppingCart.getItems().get(0);
        assertEquals(product, cartItem.getProduct());
        assertEquals(amount, cartItem.getAmount());
        assertEquals(unit, cartItem.getUnit());
    }
}
