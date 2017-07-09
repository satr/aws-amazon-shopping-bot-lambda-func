package repositories;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import common.ObjectMother;
import common.TestHelper;
import io.github.satr.aws.lambda.shoppingbot.entities.Order;
import io.github.satr.aws.lambda.shoppingbot.repositories.OrderRepository;
import io.github.satr.aws.lambda.shoppingbot.repositories.OrderRepositoryImpl;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class OrderRepositoryTestCases {
    private static AmazonDynamoDB dynamoDbClient;
    private static DynamoDBMapper dbMapper;
    private OrderRepository orderRepository;

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
        orderRepository = new OrderRepositoryImpl(dynamoDbClient, dbMapper);
        TestRepositoryHelper.createTableOrder(dynamoDbClient);
    }

    @After
    public void tearDown() throws Exception {
        TestRepositoryHelper.deleteTableOrder(dynamoDbClient);
    }

    @Test
    public void addOrder() throws Exception {
        Order order = ObjectMother.createOrder();
        orderRepository.save(order);

        List<Order> orders = orderRepository.getAllOrders();

        assertEquals(1, orders.size());
        assertEquals(order.toString(), orders.get(0).toString());
    }

    @Test
    public void getOrderByOrderId() throws Exception {
        Order order = ObjectMother.createOrder();
        orderRepository.save(order);

        Order dbOrder = orderRepository.getByOrderId(order.getOrderId());

        assertNotNull(dbOrder);
        assertEquals(order.toString(), dbOrder.toString());
    }

    @Test
    public void getAllOrders() throws Exception {
        Order order1 = ObjectMother.createOrder();
        orderRepository.save(order1);
        Order order2 = ObjectMother.createOrder();
        orderRepository.save(order2);

        List<Order> orders = orderRepository.getAllOrders();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        TestHelper.assertContains(orders, order1);
        TestHelper.assertContains(orders, order2);
    }

    @Test
    public void getByUserId() throws Exception {
        Order order1 = ObjectMother.createOrder();
        orderRepository.save(order1);
        Order order2 = ObjectMother.createOrder();
        orderRepository.save(order2);
        assertFalse(order1.getUserId().equals(order2.getUserId()));

        List<Order> dbOrders = orderRepository.getOrdersByUserId(order1.getUserId());

        assertNotNull(dbOrders);
        assertEquals(1, dbOrders.size());
        assertEquals(order1.toString(), dbOrders.get(0).toString());
    }

    @Test
    public void changeStatus() throws Exception {
        Order order = ObjectMother.createOrder();
        order.setStatus(Order.OrderStatus.Completed);
        orderRepository.save(order);

        order.setStatus(Order.OrderStatus.Shipped);
        orderRepository.save(order);

        Order dbOrder = orderRepository.getByOrderId(order.getOrderId());
        assertEquals(Order.OrderStatus.Shipped, dbOrder.getStatus());
    }
}
