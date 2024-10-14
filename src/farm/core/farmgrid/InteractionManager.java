package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;

/**
 * Interface describing the required methods for a class that manages the interactions between the grid and the farm
 */
public interface InteractionManager {

    /**
     * Validates and places an entity at a specified position.
     * @param row the row to place the entity at.
     * @param column the column to place the entity at.
     * @param entity the FarmEntity to be placed.
     * @param farmType the type of farm (i.e., animal or plant)
     * @return true iff the entity was successfully placed.
     * @requires the entity type and the farm type to equal.
     */
    boolean place(int row, int column, FarmEntity entity, FarmType farmType);

    /**
     * Harvests the entity at the specified location.
     * @param row the row of the location to be harvested.
     * @param column the column of the location to be harvested.
     * @param quality the quality of the produced product harvested from the entity.
     * @return the harvested Product with the specified quality.
     * @throws UnableToInteractException if the cell is not valid or is empty.
     */
    Product harvest(int row, int column, Quality quality) throws UnableToInteractException;

    /**
     * Feeds an entity if it is able to be fed.
     * @param row the row of the entity to feed
     * @param column the column of the entity to feed.
     * @return true iff the entity was fed successfully.
     * @throws UnableToInteractException if the cell is not valid, is empty or the entity cannot be fed.
     */
    boolean feedEntity(int row, int column) throws UnableToInteractException;

    /**
     * Performs end of day activities for each entity on the farm.
     * @return true iff each entity was successfully reset.
     */
    boolean endDay();
}
