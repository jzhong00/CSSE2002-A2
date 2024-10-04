package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;

import java.util.List;


public class PlantFarmGrid extends FarmGrid {

    public PlantFarmGrid(int rows, int columns) {
        super(rows, columns);
    }

    @Override
    public String getFarmType() {
        return "plant";
    }


    @Override
    public boolean placeEntity(int row, int column, FarmEntity entity) {
        if (!entity.getType().equals("plant")) {
            throw new IllegalArgumentException("You cannot place an animal on a plant farm!");
        }
        List<String> positionInfo = List.of(
                entity.getName(),
                String.valueOf(entity.getSymbol()),
                "Stage: 1"
        );
        farmState[row][column].addEntity(entity, positionInfo);
        return true;
    }

    @Override
    public Product harvest(int row, int column) throws UnableToInteractException {

        Cell cell = farmState[row][column];

        FarmEntity entity = validateEntityForHarvest(cell);

        Quality quality = randomQuality.getRandomQuality();
        resetPlantStage(row, column, entity);

        return entity.getProduct(quality);
    }

    @Override
    public boolean handleFeedCommand(int row, int column) throws UnableToInteractException {
        throw new UnableToInteractException("You cannot feed something that is not an animal!");
    }

    @Override
    public void resetCell(int row, int column, FarmEntity entity) throws UnableToInteractException {
        if (entity.getType().equals("plant")) {
            List<String> cellInfo = entity.grow();
            farmState[row][column].addEntity(entity, cellInfo);
        }
    }


    private FarmEntity validateEntityForHarvest(Cell cell) throws UnableToInteractException {
        if (cell.isEmpty()) {
            throw new UnableToInteractException("You can't harvest an empty spot!");
        }

        FarmEntity entity = cell.getEntity();

        if (!entity.getType().equals("plant")) {
            throw new UnableToInteractException("You have an animal on a plant farm!");
        }

        entity.checkReadyForHarvest();

        return entity;
    }



    private void resetPlantStage(int row, int column, FarmEntity entity) {
        String resetSymbol = switch (entity.getName()) {
            case "wheat" -> "\u1F34";
            case "coffee" -> ":";
            case "berry" -> ".";
            default -> throw new IllegalStateException("Unexpected entity: " + entity);
        };

        List<String> newPositionInfo = List.of(entity.getName(), resetSymbol, "Stage: 0");
        farmState[row][column].addEntity(entity, newPositionInfo);
    }
}