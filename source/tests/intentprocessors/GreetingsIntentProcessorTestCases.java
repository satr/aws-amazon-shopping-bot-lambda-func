package intentprocessors;
// Copyright © 2017, github.com/satr, MIT License

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.ShoppingBotLambda;
import io.github.satr.aws.lambda.shoppingbot.entities.User;
import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactory;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.services.ProductService;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GreetingsIntentProcessorTestCases {
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
    public void setUserAttrsToSessionForNewUserWhenGreetingsWithNames() throws Exception {
        String firstName = ObjectMother.createRandomString();
        String lastName = ObjectMother.createRandomString();
        Map<String, Object> greetingsRequestMap = ObjectMother.createGreetingsIntentMapWithNamesInSlots(firstName, lastName);
        removeUserSessionAttributes(greetingsRequestMap);
        when(userServiceMock.getUserByName(firstName, lastName)).thenReturn(null);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(greetingsRequestMap, null);

        verify(userServiceMock, times(1)).getUserByName(firstName, lastName);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userServiceMock, times(1)).save(userArgumentCaptor.capture());
        User newUser = userArgumentCaptor.getValue();
        assertEquals(firstName, newUser.getFirstName());
        assertEquals(lastName, newUser.getLastName());
        assertEquals(firstName, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.FirstName));
        assertEquals(lastName, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.LastName));
        assertEquals(newUser.getUserId(), lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserId));
    }

    @Test
    public void setUserAttrsToSessionForExistingUserWhenGreetingsWithNames() throws Exception {
        User user = ObjectMother.createUser();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        Map<String, Object> greetingsRequestMap = ObjectMother.createGreetingsIntentMapWithNamesInSlots(firstName, lastName);
        removeUserSessionAttributes(greetingsRequestMap);
        when(userServiceMock.getUserByName(firstName, lastName)).thenReturn(user);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(greetingsRequestMap, null);

        verify(userServiceMock, times(1)).getUserByName(firstName, lastName);
        verify(userServiceMock, Mockito.never()).save(Mockito.any(User.class));
        assertEquals(firstName, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.FirstName));
        assertEquals(lastName, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.LastName));
        assertEquals(user.getUserId(), lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserId));
    }

    @Test
    public void noActionForUserSetToSessionWhenGreetingsWithNames() throws Exception {
        User user = ObjectMother.createUser();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        Map<String, Object> greetingsRequestMap = ObjectMother.createGreetingsIntentMapWithNamesInSlots(firstName, lastName);
        ObjectMother.setSessionAttribute(greetingsRequestMap, LexRequestAttribute.SessionAttribute.FirstName, firstName);
        ObjectMother.setSessionAttribute(greetingsRequestMap, LexRequestAttribute.SessionAttribute.LastName, lastName);
        ObjectMother.setSessionAttribute(greetingsRequestMap, LexRequestAttribute.SessionAttribute.UserId, user.getUserId());
        when(userServiceMock.getUserByName(firstName, lastName)).thenReturn(user);

        LexResponse lexResponse = shoppingBotLambda.handleRequest(greetingsRequestMap, null);

        verify(userServiceMock, Mockito.never()).getUserById(user.getUserId());
        verify(userServiceMock, Mockito.never()).getUserByName(firstName, lastName);
        verify(userServiceMock, Mockito.never()).save(Mockito.any(User.class));
        assertEquals(firstName, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.FirstName));
        assertEquals(lastName, lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.LastName));
        assertEquals(user.getUserId(), lexResponse.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserId));
    }

    private void removeUserSessionAttributes(Map<String, Object> greetingsRequestMap) {
        ObjectMother.removeSessionAttribute(greetingsRequestMap, LexRequestAttribute.SessionAttribute.FirstName);
        ObjectMother.removeSessionAttribute(greetingsRequestMap, LexRequestAttribute.SessionAttribute.LastName);
        ObjectMother.removeSessionAttribute(greetingsRequestMap, LexRequestAttribute.SessionAttribute.UserId);
    }
}