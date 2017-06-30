package repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import common.ObjectMother;
import common.TestHelper;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCartItem;
import io.github.satr.aws.lambda.shoppingbot.repositories.ShoppingCartRepositoryImpl;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.entity.User;
import org.junit.*;

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
    public void updateOnRefreshedOnSave() throws Exception {
        ShoppingCart cart = ObjectMother.createShoppingCart();

        final int defaultYear = 2000;
        String updatedOn = ObjectMother.createZonedDateTimeForYear(defaultYear).toString();
        cart.setUpdatedOn(updatedOn);

        shoppingCartRepository.save(cart);

        ShoppingCart dbCart = shoppingCartRepository.getShoppingCartByUserId(cart.getUserId());
        assertTrue(dbCart.getUpdatedOnAsDate().getYear() > defaultYear);
    }

    @Test
    public void getAllCarts() throws Exception {
        ShoppingCart cart1 = ObjectMother.createShoppingCart();
        shoppingCartRepository.save(cart1);
        ShoppingCart cart2 = ObjectMother.createShoppingCart();
        shoppingCartRepository.save(cart2);

        List<ShoppingCart> dbCarts = shoppingCartRepository.getAllShoppingCarts();

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

        ShoppingCart dbCart = shoppingCartRepository.getShoppingCartByUserId(cart.getUserId());
        assertNull(dbCart);
        List<ShoppingCart> dbCarts = shoppingCartRepository.getAllShoppingCarts();
        assertEquals(0, dbCarts.size());
    }

    @Test
    public void getCartByUserId() throws Exception {
        ShoppingCart cart1 = ObjectMother.createShoppingCart();
        User user = cart1.getUser();
        shoppingCartRepository.save(cart1);
        ShoppingCart cart2 = ObjectMother.createShoppingCart(user);
        shoppingCartRepository.save(cart2);

        ShoppingCart dbCart = shoppingCartRepository.getShoppingCartByUserId(user.getUserId());

        assertNotNull(dbCart);
        assertFalse(cart1.toString().equals(dbCart.toString()));
        assertTrue(cart2.toString().equals(dbCart.toString()));
    }

    @Test
    public void addItemToShoppingCart() throws Exception {
        ShoppingCart cart = ObjectMother.createShoppingCart();
        String product = "bread";
        double amount = 2.0;
        String unit = "loafs";
        ShoppingCartItem cartItem = ObjectMother.createShoppingCartItem(product, amount, unit);
        cart.getItems().add(cartItem);

        shoppingCartRepository.save(cart);
        ShoppingCart dbCart = shoppingCartRepository.getShoppingCartByUserId(cart.getUserId());

        assertNotNull(dbCart.getItems());
        //TODO
/*        assertEquals(1, dbCart.getItems().size());
        ShoppingCartItem dbCartItem = dbCart.getItems().get(0);
        assertEquals(product, dbCartItem.getProduct());
        assertEquals(amount, dbCartItem.getAmount());
        assertEquals(unit, dbCartItem.getUnit());*/
    }
}
