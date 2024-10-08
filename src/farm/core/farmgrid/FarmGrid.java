package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.*;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.RandomQuality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FarmGrid implements Grid {

    private final GridManager gridManager;
    private final EntityInteractionManager interactionManager;
    private final RandomQuality randomQuality;
    private final String farmType;


    /**
     * Constructor for the FarmGrid, creating a farm of specified type.
     * @param rows the number of rows on the grid
     * @param columns the number of columns on the grid
     * @requires rows > 0 && columns > 0
     */
    public FarmGrid(int rows, int columns, String farmType) {

        this.gridManager = new GridManager(rows, columns);
        this.interactionManager = new EntityInteractionManager(gridManager);
        this.randomQuality = new RandomQuality();
        this.farmType = farmType;
    }


    /**
     * Default constructor for the FarmGrid, creating a plant farm.
     *
     * NOTE: whatever class you implement that extends Grid *must* have a constructor
     * with this signature for testing purposes.
     *
     * @param rows the number of rows on the grid
     * @param columns the number of columns on the grid
     * @requires rows > 0 && columns > 0
     */
    public FarmGrid(int rows, int columns) {
        this(rows, columns, "plant");
    }

    public String getFarmType() {
        return farmType;
    }

    @Override
    public boolean place(int row, int column, char symbol) {

        FarmEntity entity;
        try {
            entity = FarmEntity.createFarmEntity(symbol, this.getFarmType());
        } catch (IllegalArgumentException | UnableToInteractException e) {
            return false;
        }
        return interactionManager.place(row, column, entity, farmType);
    }


    @Override
    public int getRows() {
        return gridManager.getRows();
    }

    @Override
    public int getColumns() {
        return gridManager.getColumns();
    }


    public void remove(int row, int column) {
        gridManager.removeEntity(row, column);
    }

    @Override
    public String farmDisplay() {
        return gridManager.getGridDisplay();
    }

    @Override
    public List<List<String>> getStats() {
        return gridManager.getTheFarmStatsList();
    }

    public Product harvest(int row, int column) throws UnableToInteractException {
        return interactionManager.harvest(row, column, randomQuality.getRandomQuality());
    }


    @Override
    public boolean interact(String command, int row, int column) throws UnableToInteractException {
        switch (command) {
            case "feed":
                interactionManager.feedEntity(row, column);
                return true;
            case "end-day":
                interactionManager.endDay();
                return true;
            case "remove":
                this.remove(row, column);
                return true;
            default:
                throw new UnableToInteractException("Unknown command: " + command);
        }
    }
}
