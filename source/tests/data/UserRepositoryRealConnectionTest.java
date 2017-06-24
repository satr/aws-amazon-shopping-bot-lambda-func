package data;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.data.RepositoryFactoryImpl;
import io.github.satr.aws.lambda.shoppingbot.data.UserRepository;
import io.github.satr.aws.lambda.shoppingbot.data.UserRepositoryImpl;
import io.github.satr.aws.lambda.shoppingbot.entity.User;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Ignore  //Real database access
public class UserRepositoryRealConnectionTest {
    private static final RepositoryFactoryImpl repositoryFactory = new RepositoryFactoryImpl();
    private final String testingFirstName1 = "firstName1";
    private final String testingLastName1 = "testingLastName1";
    private final String testingAddress1 = "Testing address 1";
    private UserRepository userRepository;
    private List<User> testUsers = new ArrayList<>();

    @BeforeClass
    public static void fixtureSetUp() throws Exception {
    }

    @AfterClass
    public static void fixtureTearDown() throws Exception {
        repositoryFactory.shutdown();
    }

    @Before
    public void setUp() throws Exception {
        userRepository = repositoryFactory.getUserRepository();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getFullList() throws Exception {
        List<User> list = userRepository.getList();
        assertNotNull(list);
    }

    @Test
    @Ignore//Real database operation
    public void addNewUser() throws Exception {
        User user = new User();
        user.setFirstName(testingFirstName1);
        user.setLastName(testingLastName1);
        user.setAddress(testingAddress1);
        userRepository.save(user);

        User dbUser = userRepository.getById(user.getUserId());
        assertNotNull(dbUser);
        assertEquals(user.toString(), dbUser.toString());
    }
}