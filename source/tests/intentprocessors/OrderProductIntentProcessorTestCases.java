package intentprocessors;
// Copyright © 2017, github.com/satr, MIT License

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.ShoppingBotLambda;
import io.github.satr.aws.lambda.shoppingbot.entity.Product;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCartItem;
import io.github.satr.aws.lambda.shoppingbot.entity.User;
import io.github.satr.aws.lambda.shoppingbot.intent.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intent.MilkDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intent.VegetableDepartmentIntent;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class OrderProductIntentProcessorTestCases {
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
    private ShoppingBotLambda shoppingBotLambda;
    private UserService userServiceMock;
    private ShoppingCartService shoppingCartServiceMock;
    private ProductService productServiceMock;
    RepositoryFactory repositoryFactoryMock = Mockito.mock(RepositoryFactory.class);
    private String intentName;
    private String productSlotName;
    private String amountSlotName;
    private String unitSlotName;
    private String productName;
    private Double amount;
    private String unit;

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
    }

    @Test
    public void orderProductForSessionUserId() throws Exception {
        LinkedHashMap<String, Object> requestToOrderProduct = ObjectMother.createRequestFor(intentName, productSlotName,
                productName, amountSlotName, amount, unitSlotName, unit);
        User user = ObjectMother.createUser();
        String userId = user.getUserId();
        ObjectMother.setSessionAttribute(requestToOrderProduct, LexRequestAttribute.SessionAttribute.UserId, userId);
        when(userServiceMock.getUserById(user.getUserId())).thenReturn(user);
        ShoppingCart shoppingCart = ObjectMother.createShoppingCart(userId);
        when(shoppingCartServiceMock.getShoppingCartByUserId(user.getUserId())).thenReturn(shoppingCart);
        Product product = ObjectMother.createProduct(this.productName);
        when(productServiceMock.getByProductId(this.productName)).thenReturn(product);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestToOrderProduct, null);

        Assert.assertEquals(DialogAction.FulfillmentState.Fulfilled, lexResponse.getDialogAction().getFulfillmentState());
        verify(productServiceMock, Mockito.times(1)).getByProductId(this.productName);
        verify(shoppingCartServiceMock, Mockito.times(1)).getShoppingCartByUserId(userId);
        ArgumentCaptor<ShoppingCart> cartArgument = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartServiceMock, Mockito.times(1)).save(cartArgument.capture());
        ShoppingCart calledShoppingCart = cartArgument.getValue();
        Assert.assertEquals(1, calledShoppingCart.getItems().size());
        Assert.assertEquals(userId, calledShoppingCart.getUserId());
        ShoppingCartItem cartItem = calledShoppingCart.getItems().get(0);
        Assert.assertEquals(this.productName, cartItem.getProduct());
        Assert.assertEquals(amount, cartItem.getAmount());
        Assert.assertEquals(unit, cartItem.getUnit());
    }

    @Test
    public void canotOrderProductWithoutSessionUserId() throws Exception {
        LinkedHashMap<String, Object> requestToOrderProduct = ObjectMother.createRequestFor(intentName, productSlotName,
                productName, amountSlotName, amount, unitSlotName, unit);
        //No UserId in the session
        LexResponse lexResponse = shoppingBotLambda.handleRequest(requestToOrderProduct, null);

        Assert.assertEquals(DialogAction.FulfillmentState.Failed, lexResponse.getDialogAction().getFulfillmentState());

        verify(shoppingCartServiceMock, Mockito.never()).getShoppingCartByUserId(Mockito.any(String.class));
        verify(shoppingCartServiceMock, Mockito.never()).save(Mockito.any(ShoppingCart.class));
    }
}
