package business;


import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.ShoppingBotLambda;
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
import org.mockito.Mockito;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

public class WorkflowTestCases {
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

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @Test
    public void workflowOrderBread() throws Exception {
        String amount = "123456";
        String product = "bread";
        String unit = "pieces";
        LinkedHashMap<String, Object> requestToBuyBread = ObjectMother.createRequestForBakeryDepartment(product, amount, unit);
        String firstName = ObjectMother.setSessionAttributeWithRundomString(requestToBuyBread, GreetingsIntent.Slot.FirstName);
        String lastName = ObjectMother.setSessionAttributeWithRundomString(requestToBuyBread, GreetingsIntent.Slot.LastName);
        String sessionId = ObjectMother.setSessionAttributeWithRundomString(requestToBuyBread, LexRequestAttribute.SessionAttribute.SessionId);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestToBuyBread, null);

        assertEquals(DialogAction.FulfillmentState.Fulfilled, lexResponse.getDialogAction().getFulfillmentState());

        Mockito.verify(shoppingCartServiceMock, Mockito.atLeastOnce()).getShoppingCartBySessionId(sessionId);
//        Mockito.verify(userRepositoryMock, Mockito.times(1)).getUserByName(firstName, lastName);
    }
}
