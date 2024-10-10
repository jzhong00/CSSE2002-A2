package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;

import java.util.List;

public class EntityInteractionManager implements InteractionManager {

    private final GridManager farmGridManager;

    public EntityInteractionManager(GridManager farmGridManager) {
        this.farmGridManager = farmGridManager;
    }

    @Override
    public boolean place(int row, int column, FarmEntity entity, FarmType farmType) {
        if (!farmType.getName().equals(entity.getType())) {
            throw new IllegalArgumentException(
                    "You cannot place a " + entity.getType() + " on a " + farmType
            );
        }
        List<String> positionInfo = entity.getPositionInfo();
        return farmGridManager.placeEntity(row, column, entity, positionInfo);
    }

    @Override
    public Product harvest(int row, int column, Quality quality) throws UnableToInteractException {
        if (!farmGridManager.isValidCell(row, column)) {
            throw new UnableToInteractException("You can't harvest this location");
        }

        if (farmGridManager.isCellEmpty(row, column)) {
            throw new UnableToInteractException("You can't harvest an empty spot!");
        }

        Cell cell = farmGridManager.getCell(row, column);
        FarmEntity entity = cell.getEntity();

        entity.checkReadyForHarvest();

        List<String> positionInfo = entity.harvestEntity();
        farmGridManager.placeEntity(row, column, entity, positionInfo);

        return entity.getProduct(quality);
    }

    @Override
    public void feedEntity(int row, int column) throws UnableToInteractException {
        if (!farmGridManager.isValidCell(row, column)) {
            throw new UnableToInteractException("Invalid location");
        }

        FarmEntity entity = farmGridManager.getCell(row, column).getEntity();
        List<String> positionInfo = entity.feed();
        farmGridManager.getCell(row, column).addEntity(entity, positionInfo);
    }

    @Override
    public void endDay() {
        for (int row = 0; row < farmGridManager.getRows(); row++) {
            for (int col = 0; col < farmGridManager.getColumns(); col++) {
                Cell cell = farmGridManager.getCell(row, col);
                if (!cell.isEmpty()) {
                    FarmEntity entity = cell.getEntity();
                    List<String> positionInfo = entity.reset();
                    farmGridManager.getCell(row, col).addEntity(entity, positionInfo);
                }
            }
        }
    }
}
