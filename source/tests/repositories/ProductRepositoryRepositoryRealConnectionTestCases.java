package repositories;
// Copyright © 2017, github.com/satr, MIT License

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.entity.Product;
import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactoryImpl;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Ignore
@RunWith(Parameterized.class)
public class ProductRepositoryRepositoryRealConnectionTestCases {
    private final static RepositoryFactoryImpl repositoryFactory = new RepositoryFactoryImpl();
    private final String productName;
    private final String unit;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"bread","loaf"},
                {"bread","loafs"},
                {"bread","kilo"},
                {"bread","kilos"},
                {"bread","kilogram"},
                {"bread","kilograms"},
                {"bread","kg"},
                {"milk","carton"},
                {"milk","cartons"},
                {"milk","liter"},
                {"milk","liters"},
                {"milk","quart"},
                {"milk","quarts"},
        });
    }

    public ProductRepositoryRepositoryRealConnectionTestCases(String productName, String unit) {
        this.productName = productName;
        this.unit = unit;
    }

    @AfterClass
    public static void fixtureTearDown() throws Exception {
        repositoryFactory.shutdown();
    }

    @Test
    public void getProductAndPriceByName() throws Exception {
        Product product = repositoryFactory.createProductRepository().getByProductId(productName);

        assertNotNull(product);
        assertNotNull(product.getUnitPrices());
        assertTrue(product.getUnitPrices().size() > 0);
        assertNotNull(product.getUnitPrices().get(0).getPrice());
        assertNotNull(product.getUnitPrices().get(0).getUnitForms());
        assertTrue(product.getUnitPrices().get(0).getUnitForms().size() > 0);
        assertNotNull(product.getUnitPrices().get(0).getUnitForms().get(0));
        Double priceForLoaf = product.getUnitPriceFor(unit);
        assertNotNull(priceForLoaf);
        assertTrue(priceForLoaf > 0);
    }

    @Test
    @Ignore
    public void createRandomProduct() throws Exception {
        repositoryFactory.createProductRepository().save(ObjectMother.createProduct());
    }
}
