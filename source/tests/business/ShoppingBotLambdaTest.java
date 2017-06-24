package business;

import common.ObjectMother;
import data.NullRepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.ShoppingBotLambda;
import io.github.satr.aws.lambda.shoppingbot.processing.ShoppingBotProcessor;
import io.github.satr.aws.lambda.shoppingbot.intent.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intent.GreetingsIntent;
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
    private final String unknownSessionAttribute = "UnknownSessionAttribute";
    private ShoppingBotLambda shoppingBotLambda;

    @org.junit.Before
    public void setUp() throws Exception {
        shoppingBotLambda = new ShoppingBotLambda(new ShoppingBotProcessor(new NullRepositoryFactory()));
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @Test
    public void bakeryDepartmentRequest() throws Exception {
        Map<String, Object> requestMap = ObjectMother.createMapFromJson(FileNames.LexRequestBakeryDepartmentJson);
        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);
        assertNotNull(lexResponse);
    }

    @org.junit.Test
    public void orderFulfilledBackeryRequest() throws Exception {
        String amount = "123456";
        String product = "bread";
        String unit = "pieces";
        LinkedHashMap<String, Object> requestMap = ObjectMother.createRequestForBakeryDepartment(product, amount, unit);

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

    @Test
    public void transferSessionAttributesFromRequestToRespond() throws Exception {
        Map<String, Object> requestMap = ObjectMother.createMapFromJson(FileNames.LexRequestBakeryDepartmentJson);
        String firstName = ObjectMother.setSessionAttributeWithRundomString(requestMap, GreetingsIntent.Slot.FirstName);
        String lastName = ObjectMother.setSessionAttributeWithRundomString(requestMap, GreetingsIntent.Slot.LastName);
        String sessionId = ObjectMother.setSessionAttributeWithRundomString(requestMap, GreetingsIntent.Slot.SessionId);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(firstName, lexResponse.getSessionAttribute(GreetingsIntent.Slot.FirstName));
        assertEquals(lastName, lexResponse.getSessionAttribute(GreetingsIntent.Slot.LastName));
        assertEquals(sessionId, lexResponse.getSessionAttribute(GreetingsIntent.Slot.SessionId));
        assertNull(lexResponse.getSessionAttribute(unknownSessionAttribute));
    }

    @Test
    public void getNameFromGreetingsIntent() throws Exception {
        String firstName = ObjectMother.createRandomString();
        String lastName = ObjectMother.createRandomString();
        Map<String, Object> requestMap = ObjectMother.createGreetingsIntentMapWithNamesInSlots(firstName, lastName);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        String responseContent = lexResponse.getDialogAction().getMessage().getContent();
        assertTrue(responseContent.contains(firstName));
        assertTrue(responseContent.contains(lastName));
    }

    @Test
    public void storeNameSlotsToSessionAttributes() throws Exception {
        String firstName = ObjectMother.createRandomString();
        String lastName = ObjectMother.createRandomString();
        Map<String, Object> requestMap = ObjectMother.createGreetingsIntentMapWithNamesInSlots(firstName, lastName);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(firstName, lexResponse.getSessionAttribute(GreetingsIntent.Slot.FirstName));
        assertEquals(lastName, lexResponse.getSessionAttribute(GreetingsIntent.Slot.LastName));
    }
}

