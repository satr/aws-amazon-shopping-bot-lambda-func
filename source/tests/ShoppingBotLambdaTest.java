import io.github.satr.aws.lambda.shoppingbot.ShoppingBotLambda;
import io.github.satr.aws.lambda.shoppingbot.entity.BakeryDepartment;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;

import java.util.LinkedHashMap;

import static org.junit.Assert.*;

public class ShoppingBotLambdaTest {

    private ShoppingBotLambda shoppingBotLambda;

    @org.junit.Before
    public void setUp() throws Exception {
        shoppingBotLambda = new ShoppingBotLambda();
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void orderFulfilledBackeryRequest() throws Exception {
        String amount = "123456";
        String product = "bread";
        LinkedHashMap<String, Object> requestMap = ObjectMother.createRequestMap(BakeryDepartment.IntentName, BakeryDepartment.Slot.Product, product, BakeryDepartment.Slot.Amount, amount);
        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(DialogAction.FulfillmentState.Fulfilled, lexResponse.getDialogAction().getFulfillmentState());
        String responseContent = lexResponse.getDialogAction().getMessage().getContent();
        assertTrue(responseContent.contains(amount));
        assertTrue(responseContent.contains(product));
    }

    @org.junit.Test
    public void orderFailuredUnknownIntentRequest() throws Exception {
        LinkedHashMap<String, Object> requestMap = ObjectMother.createRequestMap("UnknownIntent", BakeryDepartment.Slot.Product, "bread", BakeryDepartment.Slot.Amount, "123456");
        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(DialogAction.FulfillmentState.Failed, lexResponse.getDialogAction().getFulfillmentState());
        assertNotNull(lexResponse.getDialogAction().getMessage().getContent());
        assertTrue(lexResponse.getDialogAction().getMessage().getContent().length() > 0);
    }

    @org.junit.Test
    public void orderFailuredUnknownSlotRequest() throws Exception {
        LinkedHashMap<String, Object> requestMap = ObjectMother.createRequestMap(BakeryDepartment.IntentName, "UnknownSlot", "bread", BakeryDepartment.Slot.Amount, "123456");
        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(DialogAction.FulfillmentState.Failed, lexResponse.getDialogAction().getFulfillmentState());
        assertNotNull(lexResponse.getDialogAction().getMessage().getContent());
        assertTrue(lexResponse.getDialogAction().getMessage().getContent().length() > 0);
    }

}