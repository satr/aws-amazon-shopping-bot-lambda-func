package business;


import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.ShoppingBotLambda;
import io.github.satr.aws.lambda.shoppingbot.data.RepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.data.UserRepository;
import io.github.satr.aws.lambda.shoppingbot.intent.GreetingsIntent;
import io.github.satr.aws.lambda.shoppingbot.processing.ShoppingBotProcessor;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

public class WorkflowTestCases {
    private final UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
    private ShoppingBotLambda shoppingBotLambda;

    @org.junit.Before
    public void setUp() throws Exception {
        RepositoryFactory repositoryFactoryMock = Mockito.mock(RepositoryFactory.class);
        Mockito.when(repositoryFactoryMock.getUserRepository()).thenReturn(userRepositoryMock);
        shoppingBotLambda = new ShoppingBotLambda(new ShoppingBotProcessor(repositoryFactoryMock));

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

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestToBuyBread, null);

        assertEquals(DialogAction.FulfillmentState.Fulfilled, lexResponse.getDialogAction().getFulfillmentState());

//        Mockito.verify(userRepositoryMock, Mockito.times(1)).getByName(firstName, lastName);
    }
}
