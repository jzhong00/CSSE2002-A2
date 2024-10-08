package farm.inventory;

import farm.core.InvalidStockRequestException;
import farm.inventory.product.Egg;
import farm.inventory.product.Jam;
import farm.inventory.product.Milk;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BasicInventoryTest {

    private BasicInventory inventory;

    private final String shouldNotThrow = "Should not have thrown any exceptions";

    private final Barcode jam = Barcode.JAM;
    private final Barcode milk = Barcode.MILK;
    private final Barcode egg = Barcode.EGG;
    private final Barcode wool = Barcode.WOOL;
    private final Barcode coffee = Barcode.COFFEE;
    private final Barcode bread = Barcode.BREAD;

    private final Quality gold = Quality.GOLD;
    private final Quality silver = Quality.SILVER;
    private final Quality regular = Quality.REGULAR;

    @Before
    public void setUp() {
        inventory = new BasicInventory();
    }

    @Test
    public void addValidProductTest() {
        try {
            inventory.addProduct(jam, gold);
        } catch (Exception e) {
            fail(shouldNotThrow);
        }

        List<Product> products = inventory.getAllProducts();
        assertEquals("Incorrect number of products in inventory", 1, products.size());

        assertTrue("Added product was not of the correct type", products.getFirst() instanceof Jam);
        assertEquals("Incorrect quality of product in inventory", products.getFirst().getQuality(), gold);

    }

    @Test
    public void addMultipleValidProductsTest() {
        inventory.addProduct(jam, gold);
        inventory.addProduct(milk, silver);
        inventory.addProduct(egg, regular);

        List<Product> products = inventory.getAllProducts();
        assertEquals("Incorrect number of products in inventory", 3, products.size());

        assertTrue("Added product was not of the correct type", products.getFirst() instanceof Jam);
        assertEquals("Incorrect quality of product in inventory", products.getFirst().getQuality(), gold);

        assertTrue("Added product was not of the correct type", products.get(1) instanceof Milk);
        assertEquals("Incorrect quality of product in inventory", products.get(1).getQuality(), silver);

        assertTrue("Added product was not of the correct type", products.get(2) instanceof Egg);
        assertEquals("Incorrect quality of product in inventory", products.get(2).getQuality(), regular);
    }

    @Test
    public void testAddProductWithQuantity() {
        String exceptionMsg = assertThrows(InvalidStockRequestException.class,
                () -> inventory.addProduct(Barcode.JAM, Quality.GOLD, 5)).getMessage();

        assertEquals(
                "Current inventory is not fancy enough. Please supply products one at a time.",
                exceptionMsg
        );

        List<Product> products = inventory.getAllProducts();
        assertTrue("Product was added to inventory when it shouldn't have been", inventory.getAllProducts().isEmpty());
    }

    @Test
    public void testAddProductMultipleTimes() {
        for (int i = 0; i < 5; i++) {
            inventory.addProduct(milk, regular);
        }

        List<Product> products = inventory.getAllProducts();
        assertEquals("Incorrect number of products in inventory", 5, products.size());
        for (Product product : products) {
            assertTrue("Added product was not of the correct type", product instanceof Milk);
            assertEquals("Incorrect quality of product in inventory", product.getQuality(), regular);
        }
    }

    @Test
    public void testExistsValidProduct() {
        inventory.addProduct(wool, silver);

        assertTrue("Unable to find product in inventory", inventory.existsProduct(wool));

    }

    @Test
    public void testExistsProductNotInInventory() {
        inventory.addProduct(bread, gold);

        assertFalse("Incorrectly returned if product was in inventory", inventory.existsProduct(wool));
    }

    @Test
    public void testExistProductInMultipleItems() {
        inventory.addProduct(egg, silver);
        inventory.addProduct(wool, regular);
        inventory.addProduct(milk, gold);

        assertTrue("Unable to find product in inventory", inventory.existsProduct(wool));
    }

}