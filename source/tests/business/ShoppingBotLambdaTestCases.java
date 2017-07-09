package business;
// Copyright © 2017, github.com/satr, MIT License

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.ShoppingBotLambda;
import io.github.satr.aws.lambda.shoppingbot.entities.Product;
import io.github.satr.aws.lambda.shoppingbot.entities.UnitPrice;
import io.github.satr.aws.lambda.shoppingbot.entities.User;
import io.github.satr.aws.lambda.shoppingbot.intents.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intents.GreetingsIntent;
import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.services.ProductService;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;
import org.junit.Test;
import org.mockito.Mockito;
import testdata.FileNames;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ShoppingBotLambdaTestCases {

    private final String unknownIntentName = "UnknownIntent";
    private final String unknownSlotName = "UnknownSlot";
    private final String unknownSessionAttribute = "UnknownSessionAttribute";
    private ShoppingBotLambda shoppingBotLambda;
    private UserService userServiceMock;
    private ShoppingCartService shoppingCartServiceMock;
    private ProductService productServiceMock;
    RepositoryFactory repositoryFactoryMock = Mockito.mock(RepositoryFactory.class);

    @org.junit.Before
    public void setUp() throws Exception {
        userServiceMock = Mockito.mock(UserService.class);
        shoppingCartServiceMock = Mockito.mock(ShoppingCartService.class);
        productServiceMock = Mockito.mock(ProductService.class);
        shoppingBotLambda = new ShoppingBotLambda(repositoryFactoryMock, userServiceMock, shoppingCartServiceMock, productServiceMock);
    }

    @Test
    public void bakeryDepartmentRequest() throws Exception {
        Map<String, Object> requestMap = ObjectMother.createMapFromJson(FileNames.LexRequestBakeryDepartmentJson);
        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);
        assertNotNull(lexResponse);
    }

    @org.junit.Test
    public void orderFulfilledBackeryRequest() throws Exception {
        String productName = ObjectMother.createRandomString();
        Double amount = ObjectMother.createRandomNumber();
        String unit = ObjectMother.createRandomString();
        LinkedHashMap<String, Object> requestMap = ObjectMother.createRequestForBakeryDepartment(productName, amount, unit);
        User user = ObjectMother.createUser();
        ObjectMother.setSessionAttribute(requestMap, LexRequestAttribute.SessionAttribute.UserId, user.getUserId());
        when(userServiceMock.getUserById(user.getUserId())).thenReturn(user);
        Product product = ObjectMother.createProduct(productName, unit, new String[]{unit});
        when(productServiceMock.getByProductId(productName)).thenReturn(product);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(DialogAction.FulfillmentState.Fulfilled, lexResponse.getDialogAction().getFulfillmentState());
        String responseContent = lexResponse.getDialogAction().getMessage().getContent();
        assertTrue(responseContent.contains(amount.toString()));
        assertTrue(responseContent.contains(productName));
        assertTrue(responseContent.contains(unit));
    }


    @org.junit.Test
    public void orderFailuredUnknownIntentRequest() throws Exception {
        LinkedHashMap<String, Object> requestMap = ObjectMother.createRequestMap(unknownIntentName, BakeryDepartmentIntent.Slot.Product, "bread", BakeryDepartmentIntent.Slot.Amount, "123456", BakeryDepartmentIntent.Slot.Unit, UnitPrice.UnitPieces);

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
        String sessionId = ObjectMother.setSessionAttributeWithRundomString(requestMap, LexRequestAttribute.SessionAttribute.UserId);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(firstName, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.FirstName));
        assertEquals(lastName, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.LastName));
        assertEquals(sessionId, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserId));
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
    public void keepUserIdIfExistsInSessionAttributes() throws Exception {
        Map<String, Object> requestMap = ObjectMother.createMapFromJson(FileNames.LexRequestBakeryDepartmentJson);
        String sessionId = ObjectMother.setSessionAttributeWithRundomString(requestMap, LexRequestAttribute.SessionAttribute.UserId);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        assertEquals(sessionId, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserId));
    }

}

