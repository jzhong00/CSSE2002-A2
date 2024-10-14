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
import static org.junit.Assert.assertEquals;

public class FancyInventoryTest {

   private FancyInventory inventory;

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
        inventory = new FancyInventory();
    }

    @Test
    public void addValidProductTest() {
        try {
            inventory.addProduct(jam, gold);
        } catch (Exception e) {
            fail(shouldNotThrow);
        }

        assertTrue("Product should exist after being added", inventory.existsProduct(jam));
        assertEquals("Stocked quantity should be 1", 1, inventory.getStockedQuantity(jam));
    }

    @Test
    public void addMultipleValidProductsTest() {
        inventory.addProduct(jam, gold);
        inventory.addProduct(milk, silver);
        inventory.addProduct(egg, regular);

        List<Product> products = inventory.getAllProducts();
        assertEquals("Incorrect number of products in inventory", 3, products.size());

        assertTrue("Added product was not of the correct type", inventory.existsProduct(jam));
        assertEquals("Incorrect number of product in inventory", 1, inventory.getStockedQuantity(jam));

        assertTrue("Added product was not of the correct type", inventory.existsProduct(milk));
        assertEquals("Incorrect number of product in inventory", 1, inventory.getStockedQuantity(milk));

        assertTrue("Added product was not of the correct type", inventory.existsProduct(egg));
        assertEquals("Incorrect number of product in inventory", 1, inventory.getStockedQuantity(egg));
    }

    @Test
    public void testAddSameProductMultipleTimes() {
        inventory.addProduct(egg, regular);
        inventory.addProduct(egg, regular);

        assertTrue("Product should exist after being added", inventory.existsProduct(egg));
        assertEquals("Stocked quantity should be 2", 2, inventory.getStockedQuantity(egg));

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
    public void testAddSingleProduct() throws InvalidStockRequestException {
        inventory.addProduct(bread, gold, 1);
        assertTrue("Product should exist after being added", inventory.existsProduct(bread));
        assertEquals("Stocked quantity should be 1", 1, inventory.getStockedQuantity(bread));
    }


    @Test
    public void testAddMultipleProducts() throws InvalidStockRequestException {
        inventory.addProduct(milk, regular, 5);
        assertTrue("Product should exist in inventory", inventory.existsProduct(milk));
        assertEquals("Stocked quantity should be 5", 5, inventory.getStockedQuantity(milk));
    }

    @Test
    public void testAddProductWithZeroQuantity() throws InvalidStockRequestException {
        inventory.addProduct(wool, gold, 0);
        assertFalse("Product should not exist in inventory after adding zero quantity", inventory.existsProduct(wool));
        assertEquals("Stocked quantity should be 0", 0, inventory.getStockedQuantity(wool));
    }

    @Test
    public void testAddProductWithNegativeQuantity() throws InvalidStockRequestException {
        try {
            inventory.addProduct(wool, gold, -1);
            fail("Should have thrown an Exception");
        } catch (Exception e) {
            // should throw an exception
        }
    }

    @Test
    public void testAddDifferentProducts() throws InvalidStockRequestException {
        inventory.addProduct(egg, gold, 2);
        inventory.addProduct(milk, regular, 3);
        assertTrue("Eggs should exist in inventory", inventory.existsProduct(egg));
        assertTrue("Milk should exist in inventory", inventory.existsProduct(milk));
        assertEquals("Stocked quantity of eggs should be 2", 2, inventory.getStockedQuantity(egg));
        assertEquals("Stocked quantity of milk should be 3", 3, inventory.getStockedQuantity(milk));
    }


    @Test
    public void testAddMultipleProductMultipleTimes() throws InvalidStockRequestException {
        inventory.addProduct(egg, regular, 2);
        inventory.addProduct(egg, gold, 1);
        assertEquals("Stocked quantity of eggs should be 3", 3, inventory.getStockedQuantity(egg));
        List<Product> allProducts = inventory.getAllProducts();
        assertEquals("There should be 3 eggs in total", 3, allProducts.size());
    }

    @Test
    public void testAddMultipleNullProduct() throws InvalidStockRequestException {
        try {
            inventory.addProduct(null, gold, 1);
            fail("Should have thrown an Exception for invalid barcode");
        } catch (Exception e) {
            // Expected behavior
        }
    }

    @Test
    public void testAddProductMultipleMethods() throws InvalidStockRequestException {
        inventory.addProduct(wool, regular);
        inventory.addProduct(wool, gold, 5);

        assertTrue("Product should exist after being added", inventory.existsProduct(wool));
        assertEquals("Stocked quantity of wools should be 6", 6, inventory.getStockedQuantity(wool));
    }

    @Test
    public void testExistsValidProduct() {
        inventory.addProduct(egg, gold);
        assertTrue("Product should exist in inventory after being added", inventory.existsProduct(egg));
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
    public void testExistsProductNotInInventory() {
        inventory.addProduct(bread, gold);

        assertFalse("Incorrectly returned if product was in inventory", inventory.existsProduct(wool));
    }

    @Test
    public void testProductDoesNotExistAfterRemovingAllInstances() throws InvalidStockRequestException, FailedTransactionException {
        inventory.addProduct(egg, gold, 2);
        inventory.removeProduct(egg, 2); // remove all eggs
        assertFalse("Product should not exist after all instances are removed", inventory.existsProduct(egg));
    }

    @Test
    public void testExistsAfterPartialRemoval() throws Exception {
        inventory.addProduct(milk, regular, 3); // Add 3 milk products
        inventory.removeProduct(milk, 1); // Remove 1 milk product
        assertTrue("Product should still exist after partially removing it", inventory.existsProduct(milk));
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
    public void testRemoveSingleProduct() throws FailedTransactionException {
        inventory.addProduct(egg, gold);
        List<Product> removedProducts = inventory.removeProduct(egg, 1);
        assertEquals("Should remove 1 product", 1, removedProducts.size());
        assertEquals("Removed product should be egg", egg, removedProducts.getFirst().getBarcode());
        assertFalse("Product should no longer exist in inventory", inventory.existsProduct(egg));
    }

    @Test
    public void testRemoveMultipleProductsLessThanStock() throws FailedTransactionException, InvalidStockRequestException {
        inventory.addProduct(egg, regular, 5);
        List<Product> removedProducts = inventory.removeProduct(egg, 3);
        assertEquals("Should remove 3 products", 3, removedProducts.size());
        assertEquals("Remaining stock should be 2", 2, inventory.getStockedQuantity(egg));
    }

    @Test
    public void testRemoveProductsEqualToStock() throws FailedTransactionException, InvalidStockRequestException {
        inventory.addProduct(milk, regular, 2);
        List<Product> removedProducts = inventory.removeProduct(milk, 2);
        assertEquals("Should remove 2 products", 2, removedProducts.size());
        assertFalse("Milk should no longer exist in inventory", inventory.existsProduct(milk));
    }

    @Test
    public void testRemoveProductsGreaterThanStock() throws FailedTransactionException, InvalidStockRequestException {
        inventory.addProduct(wool, gold, 3);
        List<Product> removedProducts = inventory.removeProduct(wool, 5); // Request more than stock
        assertEquals("Should remove all available products", 3, removedProducts.size());
        assertFalse("Wool should no longer exist in inventory", inventory.existsProduct(wool));
    }

    @Test
    public void testRemoveProductWhenNoneExist() throws FailedTransactionException {
        List<Product> removedProducts = inventory.removeProduct(egg, 1);
        assertTrue("Should return an empty list when no products exist", removedProducts.isEmpty());
    }

    @Test
    public void testRemoveFromEmptyInventory() throws FailedTransactionException {
        List<Product> removedProducts = inventory.removeProduct(milk, 1);
        assertTrue("Should return an empty list when inventory is empty", removedProducts.isEmpty());
    }

    @Test
    public void testRemoveHighestQualityProductsFirst() throws FailedTransactionException, InvalidStockRequestException {
        inventory.addProduct(egg, regular, 2);
        inventory.addProduct(egg, silver, 1);  // Add higher quality product
        List<Product> removedProducts = inventory.removeProduct(egg, 2);
        assertEquals("Should remove 2 products", 2, removedProducts.size());
        assertEquals("First removed product should be silver", silver, removedProducts.get(0).getQuality());
        assertEquals("Second removed product should be regular", regular, removedProducts.get(1).getQuality());
        assertEquals("Remaining stock should be 1", 1, inventory.getStockedQuantity(egg));
    }

    @Test
    public void testRemoveInvalidQuantity() throws FailedTransactionException {
        inventory.addProduct(egg, regular);
        inventory.removeProduct(egg, -1);
        assertTrue("Should contain an egg", inventory.existsProduct(egg));
        assertEquals("Should have one egg", 1, inventory.getAllProducts().size());
    }

    @Test
    public void testGetAllProductsEmptyInventory() {
        List<Product> allProducts = inventory.getAllProducts();
        assertTrue("Product list should be empty for an empty inventory", allProducts.isEmpty());
    }

    @Test
    public void testGetAllProductsSingleProduct() {
        inventory.addProduct(egg, gold);
        List<Product> allProducts = inventory.getAllProducts();
        assertEquals("Should have 1 product in the inventory", 1, allProducts.size());
        assertEquals("Product should be of type egg", egg, allProducts.get(0).getBarcode());
        assertEquals("Product quality should be gold", gold, allProducts.get(0).getQuality());
    }

    @Test
    public void testGetAllProductsMultipleDifferentProducts() {
        inventory.addProduct(egg, regular);
        inventory.addProduct(milk, gold);
        inventory.addProduct(wool, silver);

        List<Product> allProducts = inventory.getAllProducts();
        assertEquals("Should have 3 products in the inventory", 3, allProducts.size());

        assertTrue("Inventory should contain eggs", allProducts.stream().anyMatch(p -> p.getBarcode() == egg));
        assertTrue("Inventory should contain milk", allProducts.stream().anyMatch(p -> p.getBarcode() == milk));
        assertTrue("Inventory should contain wool", allProducts.stream().anyMatch(p -> p.getBarcode() == wool));
    }

    @Test
    public void testGetAllProductsMultipleQuantitiesSameProduct() throws InvalidStockRequestException {
        inventory.addProduct(egg, regular, 3);

        List<Product> allProducts = inventory.getAllProducts();
        assertEquals("Should have 3 products in the inventory", 3, allProducts.size());
        assertTrue("All products should be eggs", allProducts.stream().allMatch(p -> p.getBarcode() == egg));
    }

    @Test
    public void testGetAllProductsAfterRemovingSomeProducts() throws Exception {
        inventory.addProduct(milk, regular, 2);
        inventory.addProduct(egg, gold, 1);
        inventory.removeProduct(milk, 1); // Remove one milk

        List<Product> allProducts = inventory.getAllProducts();
        assertEquals("Should have 2 products in the inventory", 2, allProducts.size());

        assertTrue("Inventory should still contain one milk", allProducts.stream().anyMatch(p -> p.getBarcode() == milk));
        assertTrue("Inventory should contain the egg", allProducts.stream().anyMatch(p -> p.getBarcode() == egg));
    }

    @Test
    public void testGetAllProductsInCorrectOrder() {
        inventory.addProduct(egg, regular);
        inventory.addProduct(milk, gold);
        inventory.addProduct(wool, silver);

        List<Product> allProducts = inventory.getAllProducts();

        // Order should follow Barcode.values() order: egg -> milk -> wool
        assertEquals("First product should be egg", egg, allProducts.get(0).getBarcode());
        assertEquals("Second product should be milk", milk, allProducts.get(1).getBarcode());
        assertEquals("Third product should be wool", wool, allProducts.get(2).getBarcode());
    }

    @Test
    public void testListImmutability() {
        inventory.addProduct(egg, gold);
        List<Product> allProducts = inventory.getAllProducts();
        allProducts.clear(); // Attempt to modify the list
        // Ensure the inventory still contains the product
        assertTrue("Product should still exist in the inventory after attempted modification", inventory.existsProduct(egg));
    }

    @Test
    public void testNonNullReturn() {
        List<Product> allProducts = inventory.getAllProducts();
        assertNotNull("getAllProducts() should never return null", allProducts);
    }

    @Test
    public void testAddingAfterCallingGetAllProducts() {
        inventory.addProduct(milk, regular);
        List<Product> allProducts = inventory.getAllProducts();
        assertEquals("Inventory should initially have 1 product", 1, allProducts.size());

        // Add more products after getting the list
        inventory.addProduct(wool, gold);
        List<Product> updatedProducts = inventory.getAllProducts();
        assertEquals("Inventory should now have 2 products", 2, updatedProducts.size());

        assertTrue("Inventory should contain milk", updatedProducts.stream().anyMatch(p -> p.getBarcode() == milk));
        assertTrue("Inventory should contain wool", updatedProducts.stream().anyMatch(p -> p.getBarcode() == wool));
    }

    @Test
    public void testProductQualityPreservation() {
        inventory.addProduct(egg, gold);
        inventory.addProduct(milk, regular);

        List<Product> allProducts = inventory.getAllProducts();
        assertEquals("Inventory should have 2 products", 2, allProducts.size());

        assertTrue("Inventory should contain gold-quality egg",
            allProducts.stream().anyMatch(p -> p.getBarcode() == egg && p.getQuality() == gold));
        assertTrue("Inventory should contain regular-quality milk",
            allProducts.stream().anyMatch(p -> p.getBarcode() == milk && p.getQuality() == regular));
    }

    @Test
    public void testGetStockedQuantityForNonExistentProduct() {
        int quantity = inventory.getStockedQuantity(egg);
        assertEquals("Stock quantity should be 0 for a non-existent product", 0, quantity);
    }

    @Test
    public void testGetStockedQuantityAfterAddingProduct() {
        inventory.addProduct(egg, gold);
        int quantity = inventory.getStockedQuantity(egg);
        assertEquals("Stock quantity should be 1 after adding a single product", 1, quantity);
    }

    @Test
    public void testGetStockedQuantityAfterAddingMultipleInstances() throws InvalidStockRequestException {
        inventory.addProduct(milk, regular, 5); // Add 5 milk products
        int quantity = inventory.getStockedQuantity(milk);
        assertEquals("Stock quantity should be 5 after adding 5 instances", 5, quantity);
    }

    @Test
    public void testGetStockedQuantityAfterRemovingSomeProducts() throws Exception {
        inventory.addProduct(wool, gold, 3); // Add 3 wool products
        inventory.removeProduct(wool, 2); // Remove 2 wool products
        int quantity = inventory.getStockedQuantity(wool);
        assertEquals("Stock quantity should be 1 after removing 2 out of 3", 1, quantity);
    }

    @Test
    public void testGetStockedQuantityAfterRemovingAllProducts() throws Exception {
        inventory.addProduct(egg, gold, 4); // Add 4 eggs
        inventory.removeProduct(egg, 4); // Remove all eggs
        int quantity = inventory.getStockedQuantity(egg);
        assertEquals("Stock quantity should be 0 after removing all instances", 0, quantity);
    }

    @Test
    public void testGetStockedQuantityForEmptyInventory() {
        int quantity = inventory.getStockedQuantity(milk);
        assertEquals("Stock quantity should be 0 for an empty inventory", 0, quantity);
    }



    private void populateInventory(FancyInventory inventory) {
        inventory.addProduct(bread, gold);
        inventory.addProduct(egg, silver);
        inventory.addProduct(coffee, regular);
    }



}
