package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.*;
import farm.inventory.product.data.RandomQuality;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * A grid-based farm that handles executing user interactions, and manages classes to
 * update the grid display and logic.
 */
public class FarmGrid implements Grid {

    private final GridManager gridManager;
    private final InteractionManager interactionManager;
    private final RandomQuality randomQuality;
    private final FarmType farmType;


    /**
     * Constructs a type of8 FarmGrid which manages classes such as the grid manager.
     * @param gridManager a class that handles grid logic.
     * @param interactionManager a class that handles the interactions between the entities on the
     *                           farm and the grid
     * @param farmType a FarmType that indicates what type of farm is being instantiated.
     * @requires grid to have rows > 0 and columns > 0
     * @requires gridManager and interactionManager to be valid implimentations
     * of the interfaces GridManager and InteractionManager respectively.
     * @requires farmType to be a valid FarmType.
     */
    public FarmGrid(
            GridManager gridManager, InteractionManager interactionManager, FarmType farmType
    ) {

        this.gridManager = gridManager;
        this.interactionManager = interactionManager;
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
        // Create a FarmGrid with a FarmGridManager and EntityInteractionManager
        this(
                new FarmGridManager(rows, columns),
                new EntityInteractionManager(new FarmGridManager(rows, columns)),
                FarmType.PLANT
        );
    }

    /**
     * Retrieves the type of the farm
     * @return a string representing the farm type
     */
    public String getFarmType() {
        return farmType.getName();
    }

    @Override
    public boolean place(int row, int column, char symbol) {

        FarmEntity entity;
        try {
            entity = FarmEntity.createFarmEntity(symbol, this.getFarmType());
        } catch (NoSuchElementException e) {
            // Invalid symbol provided
            return false;
        }

        // Place the entity using the interaction manager
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

    @Override
    public String farmDisplay() {
        return gridManager.getGridDisplay();
    }

    @Override
    public List<List<String>> getStats() {
        return gridManager.getTheFarmStatsList();
    }

    @Override
    public Product harvest(int row, int column) throws UnableToInteractException {
        return interactionManager.harvest(row, column, randomQuality.getRandomQuality());
    }

    @Override
    public boolean interact(String command, int row, int column) throws UnableToInteractException {
        switch (command) {
            case "feed":
                return interactionManager.feedEntity(row, column);
            case "end-day":
                return interactionManager.endDay();
            case "remove":
                return gridManager.removeEntity(row, column);
            default:
                throw new UnableToInteractException("Unknown command: " + command);
        }
    }

    /**
     * Creates and adds an entity to the farm grid based on its provided information
     * (e.g., stage, fed status, etc.)
     * @param row the row to place the entity at
     * @param column the column to place the entity at
     * @param name the name of the entity (e.g., cow)
     * @param entityInfo a list of information regarding the entity's status
     */
    public void addToCell(int row, int column, String name, List<String> entityInfo) {
        // Create an entity based on its corresponding symbol
        FarmEntity entity = FarmEntity.createFarmEntity(
                Entity.getSymbolByName(name), farmType.getName()
        );
        // Update the entity's information from the provided stats
        entity.initialiseFromPositionInfo(entityInfo);
        // Place the entity onto the grid along with its information
        boolean initialPlace = true;
        gridManager.placeEntity(row, column, initialPlace, entity, entity.getPositionInfo());
    }
}
