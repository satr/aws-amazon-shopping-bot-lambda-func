package repositories.realconnection;
// Copyright © 2017, github.com/satr, MIT License

import common.ObjectMother;
import io.github.satr.aws.lambda.shoppingbot.entities.Product;
import io.github.satr.aws.lambda.shoppingbot.entities.UnitPrice;
import io.github.satr.aws.lambda.shoppingbot.repositories.RepositoryFactoryImpl;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

//HACK: this class is to populate a real database tables!
@Ignore("Real database access")
public class PopulateDataInRealDatabase {
    private final static RepositoryFactoryImpl repositoryFactory = new RepositoryFactoryImpl();
    private final String kilograms = "kilograms";
    private final String[] unitFormsForKilograms = new String[]{"kilogram", kilograms, "kilo", "kilos", "kg"};
    private final String pieces = UnitPrice.UnitPieces;
    private final String[] unitFormsForPieces = new String[]{"piece", pieces};

    @AfterClass
    public static void fixtureTearDown() throws Exception {
        repositoryFactory.shutdown();
    }

    @Test
    public void populateProductWithApple() throws Exception {
        Product product = createProduct("apple");
        addUnitPriceFor(product, 1.0, kilograms, unitFormsForKilograms);
        addUnitPriceFor(product, 0.15, pieces, unitFormsForPieces);
        repositoryFactory.createProductRepository().save(product);
    }

    @Test
    public void populateProductWithPepper() throws Exception {
        Product product = createProduct("pepper");
        addUnitPriceFor(product, 0.5, kilograms, unitFormsForKilograms);
        addUnitPriceFor(product, 0.05, pieces, unitFormsForPieces);
        repositoryFactory.createProductRepository().save(product);
    }

    @Test
    public void populateProductWithCucumber() throws Exception {
        Product product = createProduct("cucumber");
        addUnitPriceFor(product, 0.3, kilograms, unitFormsForKilograms);
        addUnitPriceFor(product, 0.02, pieces, unitFormsForPieces);
        repositoryFactory.createProductRepository().save(product);
    }

    @Test
    public void populateProductWithLemon() throws Exception {
        Product product = createProduct("lemon");
        addUnitPriceFor(product, 0.9, kilograms, unitFormsForKilograms);
        addUnitPriceFor(product, 0.1, pieces, unitFormsForPieces);
        repositoryFactory.createProductRepository().save(product);
    }

    @Test
    public void populateProductWithCarrot() throws Exception {
        Product product = createProduct("carrot");
        addUnitPriceFor(product, 0.5, kilograms, unitFormsForKilograms);
        addUnitPriceFor(product, 0.15, pieces, unitFormsForPieces);
        repositoryFactory.createProductRepository().save(product);
    }

    @Test
    public void populateProductWithTomato() throws Exception {
        Product product = createProduct("tomato");
        addUnitPriceFor(product, 0.6, kilograms, unitFormsForKilograms);
        addUnitPriceFor(product, 0.2, pieces, unitFormsForPieces);
        repositoryFactory.createProductRepository().save(product);
    }

    @Test
    public void populateProductWithBread() throws Exception {
        Product product = createProduct("bread");
        addUnitPriceFor(product, 2.0, "loafs", new String[]{"loaf", "loafs"});
        addUnitPriceFor(product, 3.0, kilograms, unitFormsForKilograms);
        repositoryFactory.createProductRepository().save(product);
    }

    @Test
    public void populateProductWithMuffin() throws Exception {
        Product product = createProduct("muffin");
        addUnitPriceFor(product, 0.5, pieces, unitFormsForPieces);
        repositoryFactory.createProductRepository().save(product);
    }

    @Test
    public void populateTablesWithBread() throws Exception {
        Product product = createProduct("milk");
        addUnitPriceFor(product, 0.5, "liters", new String[]{"liter", "liters"});
        addUnitPriceFor(product, 0.25, "cartons", new String[]{"carton", "cartons"});
        addUnitPriceFor(product, 0.2, "bottles", new String[]{"bottle", "bottles"});
        addUnitPriceFor(product, 0.6, "quarts", new String[]{"quart", "quarts"});
        addUnitPriceFor(product, 2.0, "gallons", new String[]{"gallon", "gallons"});
        repositoryFactory.createProductRepository().save(product);
    }

    @Test
    public void populateTablesWithCream() throws Exception {
        Product product = createProduct("cream");
        addUnitPriceFor(product, 0.45, "cartons", new String[]{"carton", "cartons"});
        repositoryFactory.createProductRepository().save(product);
    }

    private Product createProduct(String productName) {
        Product product = new Product();
        product.setProductId(productName);
        return product;
    }

    private void addUnitPriceFor(Product bread, double price, String unit, String[] unitForms) {
        bread.getUnitPrices().add(ObjectMother.createUnitPrice(price, unit, unitForms));
    }
}
