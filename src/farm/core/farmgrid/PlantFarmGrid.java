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
    public boolean placeEntity(int row, int column, Entity entity) {
        if (!entity.getType().equals("plant")) {
            throw new IllegalArgumentException("You cannot place an animal on a plant farm!")
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

        Entity entity = validateEntityForHarvest(cell);

        Quality quality = randomQuality.getRandomQuality();
        resetPlantStage(row, column, entity);

        return entity.getProduct(quality);
    }

    @Override
    public boolean handleFeedCommand(int row, int column) throws UnableToInteractException {
        throw new UnableToInteractException("You cannot feed something that is not an animal!");
    }

    @Override
    public void resetCell(int row, int column, Entity entity) {

        int stageNum = entity.getStage();
        String stage = "Stage: " + String.valueOf(stageNum);

        if (entity.getType().equals("plant")) {
            String name = entity.getName();
            String[] growthStages;
            switch (entity) {
                case BERRY:
                    growthStages = new String[]{".", "o", "@"};
                case WHEAT:
                    growthStages = new String[]{".", "#"};
                case COFFEE:
                    growthStages = new String[]{".", "*"};
                default:
                    break;
            }
        }

    }


    private Entity validateEntityForHarvest(Cell cell) throws UnableToInteractException {
        if (cell.isEmpty()) {
            throw new UnableToInteractException("You can't harvest an empty spot!");
        }

        Entity entity = cell.getEntity();

        if (!entity.getType().equals("plant")) {
            throw new UnableToInteractException("You have an animal on a plant farm!");
        }

        if (!isPlantFullyGrown(entity)) {
            throw new UnableToInteractException("The crop is not fully grown!");
        }

        return entity;
    }

    private boolean isPlantFullyGrown(Entity plant) {
        String symbol = String.valueOf(plant.getSymbol());
        return symbol.equals("#") || symbol.equals("%") || symbol.equals("@");

    }

    private void resetPlantStage(int row, int column, Entity entity) {
        String resetSymbol = switch (entity) {
            case WHEAT -> "\u1F34";
            case COFFEE -> ":";
            case BERRY -> ".";
            default -> throw new IllegalStateException("Unexpected entity: " + entity);
        };

        List<String> newPositionInfo = List.of(entity.getName(), resetSymbol, "Stage: 0");
        farmState[row][column].addEntity(entity, newPositionInfo);
    }
}