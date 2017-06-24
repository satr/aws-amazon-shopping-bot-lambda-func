package data;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.data.RepositoryFactoryImpl;
import io.github.satr.aws.lambda.shoppingbot.data.ShoppingCartRepository;
import io.github.satr.aws.lambda.shoppingbot.data.UserRepository;
import io.github.satr.aws.lambda.shoppingbot.data.UserRepositoryImpl;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.entity.User;
import org.junit.*;

import java.util.ArrayList;
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
        List<User> list = repositoryFactory.getUserRepository().getList();
        assertNotNull(list);
    }

    @Test
    public void addNewUser() throws Exception {
        User user = new User();
        user.setFirstName(testingFirstName1);
        user.setLastName(testingLastName1);
        user.setAddress(testingAddress1);
        UserRepository userRepository = repositoryFactory.getUserRepository();
        userRepository.save(user);

        User dbUser = userRepository.getById(user.getUserId());
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
        ShoppingCartRepository shoppingCartRepository = repositoryFactory.getShoppingCartRepository();
        shoppingCartRepository.save(shoppingCart);

        ShoppingCart dbCart = shoppingCartRepository.getById(shoppingCart.getCartId());
        assertNotNull(dbCart);
        assertEquals(shoppingCart.toString(), dbCart.toString());
    }
}