package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;

import java.util.List;

public class EntityInteractionManager {

    private final GridManager gridManager;

    public EntityInteractionManager(GridManager gridManager) {
        this.gridManager = gridManager;
    }

    public boolean place(int row, int column, FarmEntity entity, String farmType) {
        if (!farmType.equals(entity.getType())) {
            throw new IllegalArgumentException(
                    "You cannot place a " + entity.getType() + " on a " + farmType
            );
        }
        List<String> positionInfo = entity.getPositionInfo();
        return gridManager.placeEntity(row, column, entity, positionInfo);
    }

    public Product harvest(int row, int column, Quality quality) throws UnableToInteractException {
        if (!gridManager.isValidCell(row, column)) {
            throw new UnableToInteractException("You can't harvest this location");
        }

        if (gridManager.isCellEmpty(row, column)) {
            throw new UnableToInteractException("You can't harvest an empty spot!");
        }

        Cell cell = gridManager.getCell(row, column);
        FarmEntity entity = cell.getEntity();

        entity.checkReadyForHarvest();

        List<String> positionInfo = entity.harvestEntity();
        gridManager.placeEntity(row, column, entity, positionInfo);

        return entity.getProduct(quality);
    }

    public void feedEntity(int row, int column) throws UnableToInteractException {
        if (!gridManager.isValidCell(row, column)) {
            throw new UnableToInteractException("Invalid location");
        }

        FarmEntity entity = gridManager.getCell(row, column).getEntity();
        List<String> positionInfo = entity.feed();
        gridManager.getCell(row, column).addEntity(entity, positionInfo);
    }

    public void endDay() {
        for (int row = 0; row < gridManager.getRows(); row++) {
            for (int col = 0; col < gridManager.getColumns(); col++) {
                Cell cell = gridManager.getCell(row, col);
                if (!cell.isEmpty()) {
                    FarmEntity entity = cell.getEntity();
                    List<String> positionInfo = entity.reset();
                    gridManager.getCell(row, col).addEntity(entity, positionInfo);
                }
            }
        }
    }

}
