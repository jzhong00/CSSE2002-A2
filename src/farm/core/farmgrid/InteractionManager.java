package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;


public interface InteractionManager {
    boolean place(int row, int column, FarmEntity entity, FarmType farmType);
    Product harvest(int row, int column, Quality quality) throws UnableToInteractException;
    void feedEntity(int row, int column) throws UnableToInteractException;
    void endDay();
}
