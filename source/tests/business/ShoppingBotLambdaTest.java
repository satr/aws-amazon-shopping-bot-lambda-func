package business;

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.ShoppingBotLambda;
import io.github.satr.aws.lambda.shoppingbot.intent.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import org.junit.Test;
import testdata.FileNames;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ShoppingBotLambdaTest {

    private final String unknownIntentName = "UnknownIntent";
    private final String unknownSlotName = "UnknownSlot";
    private ShoppingBotLambda shoppingBotLambda;

    @org.junit.Before
    public void setUp() throws Exception {
        shoppingBotLambda = new ShoppingBotLambda();
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @Test
    public void s() throws Exception {
        Map<String, Object> requestMap = ObjectMother.createMapFromJson(FileNames.LexRequestBakeryDepartmentJson);
        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);
        assertNotNull(lexResponse);
    }

    @org.junit.Test
    public void orderFulfilledBackeryRequest() throws Exception {
        String amount = "123456";
        String product = "bread";
        String unit = "pieces";
        LinkedHashMap<String, Object> requestMap = ObjectMother.createRequestMap(BakeryDepartmentIntent.Name,
                                                                                 BakeryDepartmentIntent.Slot.Product, product,
                                                                                 BakeryDepartmentIntent.Slot.Amount, amount,
                                                                                 BakeryDepartmentIntent.Slot.Unit, unit);
        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(DialogAction.FulfillmentState.Fulfilled, lexResponse.getDialogAction().getFulfillmentState());
        String responseContent = lexResponse.getDialogAction().getMessage().getContent();
        assertTrue(responseContent.contains(amount));
        assertTrue(responseContent.contains(product));
        assertTrue(responseContent.contains(unit));
    }

    @org.junit.Test
    public void orderFailuredUnknownIntentRequest() throws Exception {
        LinkedHashMap<String, Object> requestMap = ObjectMother.createRequestMap(unknownIntentName, BakeryDepartmentIntent.Slot.Product, "bread", BakeryDepartmentIntent.Slot.Amount, "123456", BakeryDepartmentIntent.Slot.Unit, "pieces");
        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(DialogAction.FulfillmentState.Failed, lexResponse.getDialogAction().getFulfillmentState());
        assertNotNull(lexResponse.getDialogAction().getMessage().getContent());
        assertTrue(lexResponse.getDialogAction().getMessage().getContent().length() > 0);
    }

    @org.junit.Test
    public void orderFailuredUnknownSlotRequest() throws Exception {
        LinkedHashMap<String, Object> requestMap = ObjectMother.createRequestMap(BakeryDepartmentIntent.Name, unknownSlotName, "bread", BakeryDepartmentIntent.Slot.Amount, "123456", BakeryDepartmentIntent.Slot.Unit, "pieces");
        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(DialogAction.FulfillmentState.Failed, lexResponse.getDialogAction().getFulfillmentState());
        assertNotNull(lexResponse.getDialogAction().getMessage().getContent());
        assertTrue(lexResponse.getDialogAction().getMessage().getContent().length() > 0);
    }

}