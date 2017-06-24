package business;

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.ShoppingBotLambda;
import io.github.satr.aws.lambda.shoppingbot.data.RepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.processing.ShoppingBotProcessor;
import io.github.satr.aws.lambda.shoppingbot.intent.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intent.GreetingsIntent;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import org.junit.Test;
import org.mockito.Mockito;
import testdata.FileNames;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ShoppingBotLambdaTestCases {

    private final String unknownIntentName = "UnknownIntent";
    private final String unknownSlotName = "UnknownSlot";
    private final String unknownSessionAttribute = "UnknownSessionAttribute";
    private ShoppingBotLambda shoppingBotLambda;

    @org.junit.Before
    public void setUp() throws Exception {
        shoppingBotLambda = new ShoppingBotLambda(new ShoppingBotProcessor(Mockito.mock(RepositoryFactory.class)));
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
        String firstName = ObjectMother.setSessionAttributeWithRundomString(requestMap, LexRequestAttribute.SessionAttribute.FirstName);
        String lastName = ObjectMother.setSessionAttributeWithRundomString(requestMap, LexRequestAttribute.SessionAttribute.LastName);
        String sessionId = ObjectMother.setSessionAttributeWithRundomString(requestMap, LexRequestAttribute.SessionAttribute.SessionId);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(firstName, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.FirstName));
        assertEquals(lastName, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.LastName));
        assertEquals(sessionId, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.SessionId));
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
    public void overrideSessionAttributeNamesWithNamesFromIntentSlots() throws Exception {
        String firstName = ObjectMother.createRandomString();
        String lastName = ObjectMother.createRandomString();
        Map<String, Object> requestMap = ObjectMother.createGreetingsIntentMapWithNamesInSlots(firstName, lastName);

        ObjectMother.setSessionAttributeWithRundomString(requestMap, GreetingsIntent.Slot.FirstName);
        ObjectMother.setSessionAttributeWithRundomString(requestMap, GreetingsIntent.Slot.LastName);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(firstName, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.FirstName));
        assertEquals(lastName, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.LastName));
    }

    @Test
    public void createSessionIdIfNotExistsInSessionAttributes() throws Exception {
        Map<String, Object> requestMap = ObjectMother.createMapFromJson(FileNames.LexRequestBakeryDepartmentJson);
        ObjectMother.removeSessionAttribute(requestMap, LexRequestAttribute.SessionAttribute.SessionId);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertNotNull(lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.SessionId));
    }

    @Test
    public void keepSessionIdIfExistsInSessionAttributes() throws Exception {
        Map<String, Object> requestMap = ObjectMother.createMapFromJson(FileNames.LexRequestBakeryDepartmentJson);
        String sessionId = ObjectMother.setSessionAttributeWithRundomString(requestMap, LexRequestAttribute.SessionAttribute.SessionId);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(sessionId, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.SessionId));
    }

}

