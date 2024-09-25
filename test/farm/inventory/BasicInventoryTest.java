package farm.inventory;

import farm.core.InvalidStockRequestException;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicInventoryTest {

    private BasicInventory inventory;

    @Before
    public void setUp() {
        inventory = new BasicInventory();
    }

    @Test
    public void addValidProductTest() {
        inventory.addProduct(Barcode.JAM, Quality.GOLD);

        assertTrue(inventory.existsProduct(Barcode.JAM));
        assertEquals(1, inventory.getAllProducts().size());

    }

    @Test(expected = InvalidStockRequestException.class)
    public void testAddProductWithQuantity() throws InvalidStockRequestException {
        inventory.addProduct(Barcode.JAM, Quality.GOLD, 5);
    }

}