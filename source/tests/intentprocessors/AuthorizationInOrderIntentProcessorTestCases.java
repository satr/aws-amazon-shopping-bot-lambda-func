package intentprocessors;
// Copyright © 2017, github.com/satr, MIT License

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.ShoppingBotLambda;
import io.github.satr.aws.lambda.shoppingbot.entities.Product;
import io.github.satr.aws.lambda.shoppingbot.entities.User;
import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.services.ProductService;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;
import org.junit.Test;
import org.mockito.Mockito;
import testdata.FileNames;

import java.util.Map;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthorizationInOrderIntentProcessorTestCases {
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


    @org.junit.Before
    public void setUp() throws Exception {
        userServiceMock = Mockito.mock(UserService.class);
        shoppingCartServiceMock = Mockito.mock(ShoppingCartService.class);
        productServiceMock = Mockito.mock(ProductService.class);

        shoppingBotLambda = new ShoppingBotLambda(repositoryFactoryMock, userServiceMock, shoppingCartServiceMock, productServiceMock);
    }


    @Test
    public void orderWithUserFromFacebook() throws Exception {
        Map<String, Object> requestMap = ObjectMother.createMapFromJson(FileNames.OrderInVegetableDepartmentByFacebookUser);
        String fbUserId = (String) requestMap.get(LexRequestAttribute.UserId);
        User user = ObjectMother.createUserWithFacebookId(fbUserId);

        when(userServiceMock.getUserByFacebookId(fbUserId)).thenReturn(user);

        shoppingBotLambda.handleRequest(requestMap, null);

        verify(userServiceMock, atLeastOnce()).getUserByFacebookId(fbUserId);

    }
}
