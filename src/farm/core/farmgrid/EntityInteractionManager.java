package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;

import java.util.List;

/**
 * A class that manages interactions between entities on the farm and the grid.
 */
public class EntityInteractionManager implements InteractionManager {

    private final GridManager farmGridManager;

    /**
     * Constructor for the EntityInteractionManager
     * @param farmGridManager an implementation of a GridManager
     */
    public EntityInteractionManager(GridManager farmGridManager) {
        this.farmGridManager = farmGridManager;
    }

    @Override
    public boolean place(int row, int column, FarmEntity entity, FarmType farmType) {
        // An entity can only be placed if it matches the farm type
        if (!farmType.getName().equals(entity.getType())) {
            throw new IllegalArgumentException(
                    "You cannot place that on a " + farmType.getName() + " farm!"
            );
        }
        List<String> positionInfo = entity.getPositionInfo();
        boolean initialPlace = true;
        // Add the entity to the specified position
        return farmGridManager.placeEntity(row, column, initialPlace, entity, positionInfo);
    }

    @Override
    public Product harvest(int row, int column, Quality quality) throws UnableToInteractException {
        // Check the requested location is valid and has an entity.
        if (!farmGridManager.isValidCell(row, column)) {
            throw new UnableToInteractException("You can't harvest this location");
        }

        if (farmGridManager.isCellEmpty(row, column)) {
            throw new UnableToInteractException("You can't harvest an empty spot!");
        }

        FarmEntity entity = farmGridManager.getCell(row, column).getEntity();

        // Ensure the entity can be harvested
        entity.checkReadyForHarvest();

        // Harvest the entity and update its information on the grid
        List<String> positionInfo = entity.harvestEntity();
        boolean initialPlace = false;  // Updating the entity, not placing it
        farmGridManager.placeEntity(row, column, initialPlace, entity, positionInfo);

        // Return the harvested product
        return entity.getProduct(quality);
    }

    @Override
    public boolean feedEntity(int row, int column) throws UnableToInteractException {
        // Check the requested location is valid and has an entity
        if (!farmGridManager.isValidCell(row, column)) {
            throw new UnableToInteractException("Invalid location");
        }

        if (farmGridManager.isCellEmpty(row, column)) {
            throw new UnableToInteractException("You can't feed an empty spot!");
        }

        FarmEntity entity = farmGridManager.getCell(row, column).getEntity();

        // Feed the entity and update its position information on the grid
        List<String> positionInfo = entity.feed();
        farmGridManager.getCell(row, column).addEntity(entity, positionInfo);
        return true;
    }

    @Override
    public boolean endDay() {
        // Iterate through each cell on the grid
        for (int row = 0; row < farmGridManager.getRows(); row++) {
            for (int col = 0; col < farmGridManager.getColumns(); col++) {
                Cell cell = farmGridManager.getCell(row, col);
                if (!cell.isEmpty()) {  // i.e., there is an entity in that cell
                    FarmEntity entity = cell.getEntity();
                    // Reset the entity for the next day and update its information on the grid
                    List<String> positionInfo = entity.reset();
                    farmGridManager.getCell(row, col).addEntity(entity, positionInfo);
                }
            }
        }
        return true;
    }
}
