package repositories;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import common.ObjectMother;
import common.TestHelper;
import io.github.satr.aws.lambda.shoppingbot.entity.Product;
import io.github.satr.aws.lambda.shoppingbot.entity.User;
import io.github.satr.aws.lambda.shoppingbot.repositories.ProductRepository;
import io.github.satr.aws.lambda.shoppingbot.repositories.ProductRepositoryImpl;
import io.github.satr.aws.lambda.shoppingbot.repositories.UserRepositoryImpl;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ProductRepositoryTestCases {
    private static AmazonDynamoDB dynamoDbClient;
    private static DynamoDBMapper dbMapper;
    private ProductRepository productRepository;

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
        productRepository = new ProductRepositoryImpl(dynamoDbClient, dbMapper);
        TestRepositoryHelper.createTableProduct(dynamoDbClient);
    }

    @After
    public void tearDown() throws Exception {
        TestRepositoryHelper.deleteTableProduct(dynamoDbClient);
    }

    @Test
    public void addProduct() throws Exception {
        Product product = ObjectMother.createProduct();
        productRepository.save(product);

        List<Product> products = productRepository.getAllProducts();

        assertEquals(1, products.size());
        assertEquals(product.toString(), products.get(0).toString());
    }

    @Test
    public void getProductByProductId() throws Exception {
        Product product = ObjectMother.createProduct();
        productRepository.save(product);

        Product dbProduct = productRepository.getByProductId(product.getProductId());

        assertNotNull(dbProduct);
        assertEquals(product.toString(), dbProduct.toString());
    }

    @Test
    public void getAllProducts() throws Exception {
        Product product1 = ObjectMother.createProduct();
        productRepository.save(product1);
        Product product2 = ObjectMother.createProduct();
        productRepository.save(product2);

        List<Product> products = productRepository.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        TestHelper.assertContains(products, product1);
        TestHelper.assertContains(products, product2);
    }
}
