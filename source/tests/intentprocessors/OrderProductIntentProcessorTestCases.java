package intentprocessors;
// Copyright © 2017, github.com/satr, MIT License

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.ShoppingBotLambda;
import io.github.satr.aws.lambda.shoppingbot.entities.Product;
import io.github.satr.aws.lambda.shoppingbot.entities.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.entities.ShoppingCartItem;
import io.github.satr.aws.lambda.shoppingbot.entities.User;
import io.github.satr.aws.lambda.shoppingbot.intents.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intents.MilkDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intents.VegetableDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.services.ProductService;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class OrderProductIntentProcessorTestCases {

    private final double doubleDelta = 0.001;
    private String userId;
    private ShoppingBotLambda shoppingBotLambda;
    private UserService userServiceMock;
    private ShoppingCartService shoppingCartServiceMock;
    private ProductService productServiceMock;
    private RepositoryFactory repositoryFactoryMock = Mockito.mock(RepositoryFactory.class);
    private String intentName;
    private String productSlotName;
    private String amountSlotName;
    private String unitSlotName;
    private String productName;
    private Double amount;
    private String unit;
    private Double price;
    private Product product;
    private User user;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {   BakeryDepartmentIntent.Name,
                        BakeryDepartmentIntent.Slot.Product,
                        BakeryDepartmentIntent.Slot.Amount,
                        BakeryDepartmentIntent.Slot.Unit},
                {   MilkDepartmentIntent.Name,
                        MilkDepartmentIntent.Slot.Product,
                        MilkDepartmentIntent.Slot.Amount,
                        MilkDepartmentIntent.Slot.Unit},
                {
                        VegetableDepartmentIntent.Name,
                        VegetableDepartmentIntent.Slot.Product,
                        VegetableDepartmentIntent.Slot.Amount,
                        VegetableDepartmentIntent.Slot.Unit},
        });
    }

    public OrderProductIntentProcessorTestCases(String intentName, String productSlotNAme, String amountSlotNAme, String unitSlotNAme) {
        this.intentName = intentName;
        this.productSlotName = productSlotNAme;
        this.amountSlotName = amountSlotNAme;
        this.unitSlotName = unitSlotNAme;
    }

    @org.junit.Before
    public void setUp() throws Exception {
        userServiceMock = Mockito.mock(UserService.class);
        shoppingCartServiceMock = Mockito.mock(ShoppingCartService.class);
        productServiceMock = Mockito.mock(ProductService.class);

        shoppingBotLambda = new ShoppingBotLambda(repositoryFactoryMock, userServiceMock, shoppingCartServiceMock, productServiceMock);

        productName = ObjectMother.createRandomString();
        amount = ObjectMother.createRandomNumber();
        unit = ObjectMother.createRandomString();
        price = ObjectMother.createRandomNumber();

        user = ObjectMother.createUser();
        userId = user.getUserId();
        when(userServiceMock.getUserById(user.getUserId())).thenReturn(user);

        product = ObjectMother.createProduct(productName, price, unit, new String[]{unit});
        when(productServiceMock.getByProductId(productName)).thenReturn(product);
    }

    @Test
    public void addProduct() throws Exception {
        LinkedHashMap<String, Object> requestMap = createRequestMap(productName, amount, unit, userId);
        ShoppingCart shoppingCart = ObjectMother.createShoppingCart(userId);
        when(shoppingCartServiceMock.getShoppingCartByUserId(user.getUserId())).thenReturn(shoppingCart);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        Assert.assertEquals(DialogAction.FulfillmentState.Fulfilled, lexResponse.getDialogAction().getFulfillmentState());

        verify(productServiceMock, Mockito.times(1)).getByProductId(productName);
        verify(shoppingCartServiceMock, Mockito.times(1)).getShoppingCartByUserId(userId);
        ArgumentCaptor<ShoppingCart> cartArgument = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartServiceMock, Mockito.times(1)).save(cartArgument.capture());

        ShoppingCart calledShoppingCart = cartArgument.getValue();
        Assert.assertEquals(userId, calledShoppingCart.getUserId());
        Assert.assertEquals(1, calledShoppingCart.getItems().size());

        ShoppingCartItem cartItem = calledShoppingCart.getItems().get(0);
        Assert.assertEquals(productName, cartItem.getProduct());
        Assert.assertEquals(amount, cartItem.getAmount());
        Assert.assertEquals(unit, cartItem.getUnit());
        Assert.assertEquals(amount * price, cartItem.getSum(), doubleDelta);
        Assert.assertEquals(amount * price, calledShoppingCart.getTotalSum(), doubleDelta);
    }

    @Test
    public void addOrderedProductWithSameUnit() throws Exception {
        LinkedHashMap<String, Object> requestMap = createRequestMap(productName, amount, unit, userId);
        ShoppingCart shoppingCart = ObjectMother.createShoppingCart(userId);
        double amountExistingInCart = ObjectMother.createRandomNumber();
        addItemToShoppingCart(shoppingCart, productName, amountExistingInCart, price, unit);
        when(shoppingCartServiceMock.getShoppingCartByUserId(user.getUserId())).thenReturn(shoppingCart);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        Assert.assertEquals(DialogAction.FulfillmentState.Fulfilled, lexResponse.getDialogAction().getFulfillmentState());

        verify(productServiceMock, Mockito.times(1)).getByProductId(productName);
        verify(shoppingCartServiceMock, Mockito.times(1)).getShoppingCartByUserId(userId);
        ArgumentCaptor<ShoppingCart> cartArgument = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartServiceMock, Mockito.times(1)).save(cartArgument.capture());

        ShoppingCart calledShoppingCart = cartArgument.getValue();
        Assert.assertEquals(userId, calledShoppingCart.getUserId());
        Assert.assertEquals(1, calledShoppingCart.getItems().size());

        double expectedAmount = amountExistingInCart + amount;
        ShoppingCartItem cartItem = calledShoppingCart.getItems().get(0);
        Assert.assertEquals(productName, cartItem.getProduct());
        Assert.assertEquals(expectedAmount , cartItem.getAmount(), doubleDelta);
        Assert.assertEquals(unit, cartItem.getUnit());

        double expectedSum = expectedAmount * price;
        Assert.assertEquals(expectedSum, cartItem.getSum(), doubleDelta);
        Assert.assertEquals(expectedSum, calledShoppingCart.getTotalSum(), doubleDelta);
    }

    @Test
    public void addOrderedProductWithDifferentUnit() throws Exception {
        LinkedHashMap<String, Object> requestMap = createRequestMap(productName, amount, unit, userId);
        ShoppingCart shoppingCart = ObjectMother.createShoppingCart(userId);
        double amountExistingInCart = ObjectMother.createRandomNumber();
        double priceExistingInCart = ObjectMother.createRandomNumber();
        String unitExistingInCart = ObjectMother.createRandomString();
        addItemToShoppingCart(shoppingCart, productName, amountExistingInCart, priceExistingInCart, unitExistingInCart);
        when(shoppingCartServiceMock.getShoppingCartByUserId(user.getUserId())).thenReturn(shoppingCart);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        Assert.assertEquals(DialogAction.FulfillmentState.Fulfilled, lexResponse.getDialogAction().getFulfillmentState());

        verify(productServiceMock, Mockito.times(1)).getByProductId(productName);
        verify(shoppingCartServiceMock, Mockito.times(1)).getShoppingCartByUserId(userId);
        ArgumentCaptor<ShoppingCart> cartArgument = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartServiceMock, Mockito.times(1)).save(cartArgument.capture());

        ShoppingCart calledShoppingCart = cartArgument.getValue();
        Assert.assertEquals(userId, calledShoppingCart.getUserId());
        Assert.assertEquals(1, calledShoppingCart.getItems().size());

        ShoppingCartItem cartItem = calledShoppingCart.getItems().get(0);
        Assert.assertEquals(productName, cartItem.getProduct());
        Assert.assertEquals(amount, cartItem.getAmount(), doubleDelta);
        Assert.assertEquals(unit, cartItem.getUnit());

        double expectedSum = (double) amount * this.price;
        Assert.assertEquals(expectedSum, cartItem.getSum(), doubleDelta);
        Assert.assertEquals(expectedSum, calledShoppingCart.getTotalSum(), doubleDelta);
    }
    
    @Test
    public void addDifferentProduct() throws Exception {
        LinkedHashMap<String, Object> requestMap = createRequestMap(productName, amount, unit, userId);
        ShoppingCart shoppingCart = ObjectMother.createShoppingCart(userId);
        String productExistingInCart = ObjectMother.createRandomString();
        double amountExistingInCart = ObjectMother.createRandomNumber();
        double priceExistingInCart = ObjectMother.createRandomNumber();
        String unitExistingInCart = ObjectMother.createRandomString();
        ShoppingCartItem existingCartItem = addItemToShoppingCart(shoppingCart, productExistingInCart,
                                                        amountExistingInCart, priceExistingInCart, unitExistingInCart);
        when(shoppingCartServiceMock.getShoppingCartByUserId(user.getUserId())).thenReturn(shoppingCart);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        Assert.assertEquals(DialogAction.FulfillmentState.Fulfilled, lexResponse.getDialogAction().getFulfillmentState());

        verify(productServiceMock, Mockito.times(1)).getByProductId(productName);
        verify(shoppingCartServiceMock, Mockito.times(1)).getShoppingCartByUserId(userId);
        ArgumentCaptor<ShoppingCart> cartArgument = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartServiceMock, Mockito.times(1)).save(cartArgument.capture());

        ShoppingCart calledShoppingCart = cartArgument.getValue();
        Assert.assertEquals(userId, calledShoppingCart.getUserId());
        Assert.assertEquals(2, calledShoppingCart.getItems().size());

        ShoppingCartItem cartItem = getCartItemByProductName(calledShoppingCart, productName);
        assertNotNull(cartItem);
        Assert.assertEquals(productName, cartItem.getProduct());
        Assert.assertEquals(amount, cartItem.getAmount(), doubleDelta);
        Assert.assertEquals(unit, cartItem.getUnit());

        double expectedOrderedSum = (double) amount * this.price;
        Assert.assertEquals(expectedOrderedSum, cartItem.getSum(), doubleDelta);
        Assert.assertEquals(expectedOrderedSum + existingCartItem.getSum(), calledShoppingCart.getTotalSum(), doubleDelta);
    }

    private ShoppingCartItem getCartItemByProductName(ShoppingCart calledShoppingCart, String productName) {
        for(ShoppingCartItem item: calledShoppingCart.getItems()) {
            if(item.getProduct().equals(productName))
                return item;
        }
        return null;
    }

    private ShoppingCartItem addItemToShoppingCart(ShoppingCart shoppingCart, String productName, double amount, double price, String unit) {
        ShoppingCartItem shoppingCartItem = ObjectMother.createShoppingCartItem(productName, amount, price, unit);
        shoppingCart.getItems().add(shoppingCartItem);
        return shoppingCartItem;
    }

    @Test
    public void cannotOrderProductWithoutSessionUserId() throws Exception {
        LinkedHashMap<String, Object> requestMap = createRequestMap(productName, amount, unit, userId);
        removeUserIdFromSessionAttributes(requestMap);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestMap, null);

        Assert.assertEquals(DialogAction.FulfillmentState.Failed, lexResponse.getDialogAction().getFulfillmentState());

        verify(shoppingCartServiceMock, Mockito.never()).getShoppingCartByUserId(Mockito.any(String.class));
        verify(shoppingCartServiceMock, Mockito.never()).save(Mockito.any(ShoppingCart.class));
    }

    private void removeUserIdFromSessionAttributes(LinkedHashMap<String, Object> requestMap) {
        ObjectMother.removeSessionAttribute(requestMap, LexRequestAttribute.SessionAttribute.UserId);
    }

    private LinkedHashMap<String, Object> createRequestMap(String productName, Double amount, String unit, String userId) {
        LinkedHashMap<String, Object> map = ObjectMother.createRequestFor(intentName, productSlotName, productName,
                amountSlotName, amount, unitSlotName, unit);
        ObjectMother.setSessionAttribute(map, LexRequestAttribute.SessionAttribute.UserId, userId);
        return map;
    }
}
