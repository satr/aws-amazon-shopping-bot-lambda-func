package repositories.realconnection;
// Copyright © 2017, github.com/satr, MIT License

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactoryImpl;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Real database access")
public class OrderRepositoryRealConnectionTestCases {
    private final static RepositoryFactoryImpl repositoryFactory = new RepositoryFactoryImpl();

    @AfterClass
    public static void fixtureTearDown() throws Exception {
        repositoryFactory.shutdown();
    }

    @Test
    public void addOrder() throws Exception {
        repositoryFactory.createOrderRepository().save(ObjectMother.createOrder());
    }
}
