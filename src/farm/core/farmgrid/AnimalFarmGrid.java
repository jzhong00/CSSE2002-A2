package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;

import java.util.List;

public class AnimalFarmGrid extends FarmGrid {

    public AnimalFarmGrid(int rows, int columns) {
        super(rows, columns);
    }

    @Override
    public String getFarmType() {
        return "animal";
    }

    @Override
    protected boolean placeEntity(int row, int column, FarmEntity entity) {
        if (!entity.getType().equals("animal")) {
            throw new IllegalArgumentException("You cannot place a plant on an animal farm!");
        }

        List<String> positionInfo = List.of(
                entity.getName(),
                String.valueOf(entity.getSymbol()),
                "Fed: false", "collected: false"
        );
        farmState[row][column].addEntity(entity, positionInfo);
        return true;
    }

    @Override
    public Product harvest(int row, int column) throws UnableToInteractException {
        Cell cell = farmState[row][column];

        FarmEntity entity = validateEntityForHarvest(cell);
        Quality quality = randomQuality.getRandomQuality();

        entity.harvestEntity();

        return entity.getProduct(quality);
    }

    @Override
    public boolean handleFeedCommand(int row, int column) throws UnableToInteractException {
        return false;
    }

    @Override
    public void resetCell(int row, int column, FarmEntity entity) throws UnableToInteractException {

    }

    private FarmEntity validateEntityForHarvest(Cell cell) throws UnableToInteractException {
               if (cell.isEmpty()) {
            throw new UnableToInteractException("You can't harvest an empty spot!");
        }

        FarmEntity entity = cell.getEntity();

        if (!entity.getType().equals("animal")) {
            throw new UnableToInteractException("You have a plant on an animal farm!");
        }

        entity.checkReadyForHarvest();
        return entity;
    }
}

