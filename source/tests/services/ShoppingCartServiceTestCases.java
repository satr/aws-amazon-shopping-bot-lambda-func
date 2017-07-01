package services;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.repositories.ShoppingCartRepository;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartServiceImpl;
import org.junit.Before;
import org.mockito.Mockito;

public class ShoppingCartServiceTestCases {

    private ShoppingCartRepository shoppingCartRepositoryMock;
    private ShoppingCartServiceImpl shoppingCartService;
    private Logger logger = Mockito.mock(Logger.class);

    @Before
    public void setUp() throws Exception {
        shoppingCartRepositoryMock = Mockito.mock(ShoppingCartRepository.class);
        shoppingCartService = new ShoppingCartServiceImpl(shoppingCartRepositoryMock, logger);
    }

}
