package farm.inventory;

import farm.core.FailedTransactionException;
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
    public void testAddProductOneQuantity() {
        String exceptionMsg = assertThrows(InvalidStockRequestException.class,
                () -> inventory.addProduct(Barcode.WOOL, Quality.SILVER, 1)).getMessage();

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
    public void testAddNullProduct() {
        try {
            inventory.addProduct(null, gold);
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            // Expected
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
    public void testProductDoesNotExistWithoutAdding() {
        assertFalse("Product should not exist if it hasn't been added", inventory.existsProduct(milk));
    }


    @Test
    public void testExistProductInMultipleItems() {
        inventory.addProduct(egg, silver);
        inventory.addProduct(wool, regular);
        inventory.addProduct(milk, gold);

        assertTrue("Unable to find product in inventory", inventory.existsProduct(wool));
    }

    @Test
    public void testValidRemoveFromInventory() {
        populateInventory(inventory);

        List<Product> removedProduct = inventory.removeProduct(egg);
        assertEquals("Incorrect number of products removed", 1, removedProduct.size());
        assertFalse("Product was not removed.", inventory.existsProduct(egg));
        assertTrue("Product that should not have been removed does not exist", inventory.existsProduct(bread));
        assertTrue("Product that should not have been removed does not exist", inventory.existsProduct(coffee));
    }

    @Test
    public void testValidRemoveMultipleFromInventory() {
        populateInventory(inventory);
        List<Product> firstRemoved = inventory.removeProduct(egg);
        List<Product> secondRemoved = inventory.removeProduct(bread);

        assertEquals("Incorrect number of products in inventory", 1, inventory.getAllProducts().size());
        assertFalse("Product was not removed", inventory.existsProduct(egg));
        assertFalse("Product was not removed", inventory.existsProduct(bread));
        assertTrue("Product that should not have been removed does not exist", inventory.existsProduct(coffee));
    }

    @Test
    public void testRemoveInvalidProduct() {
        populateInventory(inventory);

        List<Product> products = inventory.removeProduct(wool);

        assertEquals("Incorrect number of products removed", 0, products.size());
        assertTrue("Product that should not have been removed does not exist", inventory.existsProduct(bread));
        assertTrue("Product that should not have been removed does not exist", inventory.existsProduct(egg));
        assertTrue("Product that should not have been removed does not exist", inventory.existsProduct(coffee));

    }

    @Test
    public void testRemovingMultipleOfSameProduct() {
        populateInventory(inventory);
        inventory.addProduct(bread, regular);

        try {
            inventory.removeProduct(bread);
            inventory.removeProduct(bread);
        } catch (Exception e) {
            fail(shouldNotThrow);
        }

        assertFalse("Not all products removed", inventory.existsProduct(bread));
        assertTrue("Incorrect product removed", inventory.existsProduct(egg));
        assertTrue("Incorrect product removed", inventory.existsProduct(coffee));
    }

    @Test
    public void testRemoveProductFromEmptyInventory() {
        List<Product> removedProduct = inventory.removeProduct(bread);

        assertTrue("Should not have removed any products", removedProduct.isEmpty());
        assertTrue("Inventory should still be empty", inventory.getAllProducts().isEmpty());
    }

    @Test
    public void testRemoveProductWithInvalidQuantity() {
        FailedTransactionException exception = assertThrows(
                FailedTransactionException.class,
                () -> inventory.removeProduct(bread, 5)
        );

        assertEquals(
                "Current inventory is not fancy enough. Please purchase products one at a time.",
                exception.getMessage()
        );
    }

    @Test
    public void testRemoveLastProductInInventory() {
        inventory.addProduct(egg, regular);

        inventory.removeProduct(egg);

        assertTrue("Inventory should be empty after removing the last product", inventory.getAllProducts().isEmpty());
    }

    @Test
    public void testRemoveMultipleDifferentProducts() {
        inventory.addProduct(egg, regular);
        inventory.addProduct(wool, silver);
        inventory.addProduct(bread, gold);

        inventory.removeProduct(egg);
        inventory.removeProduct(bread);

        assertFalse("Egg should have been removed", inventory.existsProduct(egg));
        assertFalse("Bread should have been removed", inventory.existsProduct(bread));
        assertTrue("Wool should still exist in the inventory", inventory.existsProduct(wool));
    }

    @Test
    public void testGetAllProductsWhenEmpty() {
        List<Product> products = inventory.getAllProducts();
        assertTrue("Product list should be empty", products.isEmpty());
    }

    @Test
    public void testGetAllProductsWithOneProduct() {
        inventory.addProduct(bread, gold);

        List<Product> products = inventory.getAllProducts();

        assertEquals("Product list should contain one product", 1, products.size());
        assertEquals("First product should be of type bread", bread, products.get(0).getBarcode());
        assertEquals("Product quality should be gold", gold, products.get(0).getQuality());
    }

    @Test
    public void testGetAllProductsWhenValid() {
        populateInventory(inventory);
        List<Product> products = inventory.getAllProducts();
        assertEquals("Incorrect number of products in inventory", 3, products.size());
        assertTrue("Did not return product that is in inventory",
                   products.stream().anyMatch(product -> product.getBarcode() == bread));
        assertTrue("Did not return product that is in inventory",
                   products.stream().anyMatch(product -> product.getBarcode() == egg));
        assertTrue("Did not return product that is in inventory",
                   products.stream().anyMatch(product -> product.getBarcode() == coffee));
    }

    @Test
    public void testGetAllProductsWithDuplicateProducts() {
        inventory.addProduct(bread, regular);
        inventory.addProduct(bread, regular);

        List<Product> products = inventory.getAllProducts();

        assertEquals("Product list should contain two products", 2, products.size());
        assertEquals("Both products should be bread", bread, products.get(0).getBarcode());
        assertEquals("Both products should have regular quality", regular, products.get(0).getQuality());
    }

    @Test
    public void testImmutableProductList() {
        inventory.addProduct(milk, gold);

        List<Product> products = inventory.getAllProducts();
        products.clear();  // Attempt to modify the returned list

        List<Product> productsAfterModification = inventory.getAllProducts();
        assertEquals("Inventory should not be affected by external list modifications", 1, productsAfterModification.size());
    }

    @Test
    public void testGetAllProductsAfterRemovingAllProducts() {
        inventory.addProduct(egg, silver);
        inventory.addProduct(milk, gold);

        inventory.removeProduct(egg);
        inventory.removeProduct(milk);

        List<Product> products = inventory.getAllProducts();

        assertTrue("Product list should be empty after removing all products", products.isEmpty());
    }

    private void populateInventory(BasicInventory inventory) {
        inventory.addProduct(bread, gold);
        inventory.addProduct(egg, silver);
        inventory.addProduct(coffee, regular);
    }

}