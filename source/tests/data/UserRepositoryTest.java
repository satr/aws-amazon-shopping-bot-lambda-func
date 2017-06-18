package data;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.data.UserRepositoryImpl;
import io.github.satr.aws.lambda.shoppingbot.entity.User;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryTest {
    private static AmazonDynamoDB dynamoDbClient;
    private static DynamoDBMapper dbMapper;
    private final String testingFirstName1 = "firstName1";
    private final String testingLastName1 = "testingLastName1";
    private UserRepositoryImpl userRepository;
    private List<User> testUsers = new ArrayList<>();

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
        userRepository = new UserRepositoryImpl(dynamoDbClient);
        TestRepositoryHelper.createTableUser(dynamoDbClient);
        testUsers.add(TestRepositoryHelper.addUser(dbMapper, testingFirstName1, testingLastName1, "address1"));
        testUsers.add(TestRepositoryHelper.addUser(dbMapper, testingFirstName1, testingLastName1, "address2"));
        testUsers.add(TestRepositoryHelper.addUser(dbMapper, "firstName2", "lastName2", "address3"));
    }

    @After
    public void tearDown() throws Exception {
        TestRepositoryHelper.deleteTableUser(dynamoDbClient);

    }

    @Test
    public void getFullList() throws Exception {
        List<User> list = userRepository.getList();
        assertEquals(testUsers.size(), list.size());
    }

    @Test
    public void getUserById() throws Exception {
        User testUser = testUsers.get(0);
        User dbUser = userRepository.getById(testUser.getUserId());
        assertNotNull(dbUser);
        assertEquals(testUser.toString(), dbUser.toString());
    }

    @Test
    public void getUserByName() throws Exception {
        List<User> dbUsers = userRepository.getByName(testingFirstName1, testingLastName1);
        assertNotNull(dbUsers);
        assertTrue(dbUsers.size() > 1);
        assertEquals(testingFirstName1, dbUsers.get(0).getFirstName());
        assertEquals(testingLastName1, dbUsers.get(0).getLastName());
    }
}