package data;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import common.ObjectMother;
import common.TestHelper;
import io.github.satr.aws.lambda.shoppingbot.data.ShoppingCartRepositoryImpl;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.entity.User;
import org.junit.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.*;

public class ShoppingCartRepositoryTestCases {
    private static AmazonDynamoDB dynamoDbClient;
    private static DynamoDBMapper dbMapper;
    private final String testingFirstName1 = "firstName1";
    private final String testingLastName1 = "testingLastName1";
    private ShoppingCartRepositoryImpl shoppingCartRepository;

    @BeforeClass
    public static void fixtureSetUp() throws Exception {
        dynamoDbClient = ObjectMother.createInMemoryDb();
        dbMapper = new DynamoDBMapper(dynamoDbClient);
    }

    @AfterClass
    public static void fixtureTearDown() throws Exception {
        if(dynamoDbClient != null)
            dynamoDbClient.shutdown();
    }

    @Before
    public void setUp() throws Exception {
        shoppingCartRepository = new ShoppingCartRepositoryImpl(dynamoDbClient, dbMapper);
        TestRepositoryHelper.createTableShoppingCart(dynamoDbClient);
    }

    @After
    public void tearDown() throws Exception {
        TestRepositoryHelper.deleteTableShoppingCart(dynamoDbClient);
    }

    @Test
    public void createCartAndGetById() throws Exception {
        ShoppingCart cart = ObjectMother.createShoppingCart();

        shoppingCartRepository.save(cart);

        ShoppingCart dbCart = shoppingCartRepository.getById(cart.getCartId());
        assertNotNull(dbCart);
        assertEquals(cart.getUserId(), dbCart.getUserId());
        assertNull(dbCart.getUser());
    }

    @Test
    public void getBySessionId() throws Exception {
        ShoppingCart cart = ObjectMother.createShoppingCart();

        shoppingCartRepository.save(cart);

        List<ShoppingCart> dbCarts = shoppingCartRepository.getBySessionId(cart.getSessionId());
        assertNotNull(dbCarts);
        assertEquals(1, dbCarts.size());
        assertEquals(cart.toString(), dbCarts.get(0).toString());
    }

    @Test
    public void updateOnRefreshedOnSave() throws Exception {
        ShoppingCart cart = ObjectMother.createShoppingCart();

        final int defaultYear = 2000;
        String updatedOn = ObjectMother.createZonedDateTimeForYear(defaultYear).toString();
        cart.setUpdatedOn(updatedOn);

        shoppingCartRepository.save(cart);

        ShoppingCart dbCart = shoppingCartRepository.getById(cart.getCartId());
        assertTrue(dbCart.getUpdatedOnAsDate().getYear() > defaultYear);
    }

    @Test
    public void getAllCarts() throws Exception {
        ShoppingCart cart1 = ObjectMother.createShoppingCart();
        shoppingCartRepository.save(cart1);
        ShoppingCart cart2 = ObjectMother.createShoppingCart();
        shoppingCartRepository.save(cart2);

        List<ShoppingCart> dbCarts = shoppingCartRepository.getList();

        assertNotNull(dbCarts);
        assertEquals(2, dbCarts.size());
        TestHelper.assertContains(dbCarts, cart1);
        TestHelper.assertContains(dbCarts, cart2);
    }

    @Test
    public void deleteCart() throws Exception {
        ShoppingCart cart = ObjectMother.createShoppingCart();
        shoppingCartRepository.save(cart);

        shoppingCartRepository.delete(cart);

        ShoppingCart dbCart = shoppingCartRepository.getById(cart.getCartId());
        assertNull(dbCart);
        List<ShoppingCart> dbCarts = shoppingCartRepository.getList();
        assertEquals(0, dbCarts.size());
    }

    @Test
    public void getCartByUserId() throws Exception {
        ShoppingCart cart1 = ObjectMother.createShoppingCart();
        shoppingCartRepository.save(cart1);
        User user = cart1.getUser();
        ShoppingCart cart2 = ObjectMother.createShoppingCart(user);
        shoppingCartRepository.save(cart2);

        List<ShoppingCart> dbCarts = shoppingCartRepository.getByUserId(user.getUserId());

        assertNotNull(dbCarts);
        TestHelper.assertContains(dbCarts, cart1);
        TestHelper.assertContains(dbCarts, cart2);
    }

}
