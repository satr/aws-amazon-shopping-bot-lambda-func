package repositories;
// Copyright © 2017, github.com/satr, MIT License

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactoryImpl;
import io.github.satr.aws.lambda.shoppingbot.repositories.ShoppingCartRepository;
import io.github.satr.aws.lambda.shoppingbot.repositories.UserRepository;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.entity.User;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

@Ignore  //Real database access
public class UserRepositoryRealConnectionTestCases {
    private final static String testingFirstName1 = "firstName1";
    private final static String testingLastName1 = "testingLastName1";
    private final static String testingAddress1 = "Testing address 1";
    private final static RepositoryFactoryImpl repositoryFactory = new RepositoryFactoryImpl();

    @AfterClass
    public static void fixtureTearDown() throws Exception {
        repositoryFactory.shutdown();
    }

    @Test
    public void getFullList() throws Exception {
        List<User> list = repositoryFactory.createUserRepository().getAllUsers();
        assertNotNull(list);
    }

    @Test
    public void addNewUser() throws Exception {
        User user = new User();
        user.setFirstName(testingFirstName1);
        user.setLastName(testingLastName1);
        user.setAddress(testingAddress1);
        UserRepository userRepository = repositoryFactory.createUserRepository();
        userRepository.save(user);

        User dbUser = userRepository.getUserById(user.getUserId());
        assertNotNull(dbUser);
        assertEquals(user.toString(), dbUser.toString());
    }

    @Test
    public void addNewCart() throws Exception {
        User user = new User();
        user.setFirstName(testingFirstName1);
        user.setLastName(testingLastName1);
        user.setAddress(testingAddress1);
        ShoppingCart shoppingCart = ObjectMother.createShoppingCart(user);
        ShoppingCartRepository shoppingCartRepository = repositoryFactory.createShoppingCartRepository();
        shoppingCartRepository.save(shoppingCart);

        ShoppingCart dbCart = shoppingCartRepository.getShoppingCartByUserId(shoppingCart.getUserId());
        assertNotNull(dbCart);
        assertEquals(shoppingCart.toString(), dbCart.toString());
    }
}