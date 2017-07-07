package repositories.realconnection;
// Copyright © 2017, github.com/satr, MIT License

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.entity.Product;
import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactoryImpl;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("CAUTION: this class is to populate a real database tables!")
public class PopulateDataInRealDatabase {
    private final static RepositoryFactoryImpl repositoryFactory = new RepositoryFactoryImpl();

    @AfterClass
    public static void fixtureTearDown() throws Exception {
        repositoryFactory.shutdown();
    }

    @Test
    public void populateProductWithBread() throws Exception {
        Product product = createProduct("bread");
        addUnitPriceFor(product, 2.0, new String[]{"loaf", "loafs"});
        addUnitPriceFor(product, 3.0, new String[]{"kilogram", "kilograms", "kilo", "kilos", "kg"});
        repositoryFactory.createProductRepository().save(product);
    }

    @Test
    public void populateProductWithMuffin() throws Exception {
        Product product = createProduct("muffin");
        addUnitPriceFor(product, 0.5, new String[]{"piece", "pieces"});
        repositoryFactory.createProductRepository().save(product);
    }

    @Test
    public void populateTables() throws Exception {
        Product product = createProduct("milk");
        addUnitPriceFor(product, 0.5, new String[]{"liter", "liters"});
        addUnitPriceFor(product, 0.25, new String[]{"carton", "cartons", "bottle", "bottles"});
        addUnitPriceFor(product, 0.6, new String[]{"quart", "quarts"});
        addUnitPriceFor(product, 2.0, new String[]{"gallon", "gallons"});
        repositoryFactory.createProductRepository().save(product);
    }

    private Product createProduct(String productName) {
        Product product = new Product();
        product.setProductId(productName);
        return product;
    }

    private void addUnitPriceFor(Product bread, double price, String[] unitForms) {
        bread.getUnitPrices().add(ObjectMother.createUnitPrice(price, unitForms));
    }
}
