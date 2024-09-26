package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.*;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.RandomQuality;

import java.util.ArrayList;
import java.util.List;


public class FarmGrid implements Grid {

    protected Cell[][] farmState;
    protected int rows;
    protected int columns;
    protected String farmType;
    protected RandomQuality randomQuality;


    /**
     * Constructor for the FarmGrid, creating a farm of specified type.
     * @param rows the number of rows on the grid
     * @param columns the number of columns on the grid
     * @requires rows > 0 && columns > 0
     */
    public FarmGrid(int rows, int columns, String farmType) {

        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Rows and columns must be greater than 0");
        }

        if (!farmType.equals("plant") && !farmType.equals("animal")) {
            throw new IllegalArgumentException("FarmType must be either plant or animal");
        }

        this.farmType = farmType;
        this.rows = rows;
        this.columns = columns;
        this.randomQuality = new RandomQuality();
        this.farmState = new Cell[rows][columns];

        // populate the initial farm with empty ground
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                farmState[i][j] = new Cell();
            }
        }
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
        return this.farmType;
    }

    @Override
    public boolean place(int row, int column, char symbol) {

        if (!isValidCell(row, column)) {
            return false;
        }

        String[] entityInfo = Entity.getEntityInfo(symbol);
        Entity entity = Entity.getBySymbol(symbol);

        if (entityInfo == null || entity == null) {
            return false;
        }

        Cell cell = farmState[row][column];
        if (!cell.isEmpty()) {
            throw new IllegalStateException("Something is already there!");
        }

        String entityType = entityInfo[2];

        if (this.farmType.equals("plant") && entityType.equals("plant")) {
            return placePlant(row, column, entity, entityInfo);
        } else if (this.farmType.equals("animal") && entityType.equals("animal")) {
            return placeAnimal(row, column, entity, entityInfo);
        } else {
            throw new IllegalArgumentException(
                    "You cannot place that on a " + this.farmType + " farm!"
            );
        }
    }

    private boolean placePlant(int row, int column, Entity entity, String[] entityInfo) {
        List<String> positionInfo = List.of(entityInfo[0], entityInfo[1], "Stage: 1");
        farmState[row][column].addEntity(entity, positionInfo);
        return true;
    }

    private boolean placeAnimal(int row, int column, Entity entity, String[] entityInfo) {
        List<String> positionInfo = List.of(entityInfo[0], entityInfo[1], "Fed: false", "Collected: false");
        farmState[row][column].addEntity(entity, positionInfo);
        return true;
    }

    @Override
    public int getRows() {
        return this.rows;

    }

    @Override
    public int getColumns() {
        return this.columns;
    }

    protected Boolean isValidCell(int row, int column) {
        return (row >= 0 && row < rows && column >= 0 && column < columns);
    }

    public void remove(int row, int column) {
        if (isValidCell(row, column)) {
            farmState[row][column].removeEntity();
        }
    }

    @Override
    public String farmDisplay() {
        // create the fence at the top of the farm
        // two lines for each column of the farm, plus two for edges
        // and one for extra space
        StringBuilder sb = new StringBuilder();
        String horizontalFence = "-".repeat((this.columns * 2) + 3);

        sb.append(horizontalFence).append(System.lineSeparator());

        // start each line with a "|" fence character
        // then display symbols with a space either side

        // note System.lineSeparator() is just \n but ensures it works
        // on all operating systems.

        for (int i = 0; i < this.rows; i++) {
            sb.append("| ");
            for (int j = 0; j < this.columns; j++) {

                Cell cell = farmState[i][j];
                char symbol = ' ';

                if (!cell.isEmpty()) {
                    symbol = cell.getEntity().getSymbol();
                }
                sb.append(symbol).append(" ");
            }
            sb.append("|").append(System.lineSeparator());
        }
        sb.append(horizontalFence).append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public List<List<String>> getStats() {
        return getTheFarmStatsList();
    }


    @Override
    public Product harvest(int row, int column) throws UnableToInteractException {

        if (!isValidCell(row, column)) {
            throw new UnableToInteractException("You can't harvest this location");
        }

        Cell cell = farmState[row][column];

        if (cell.isEmpty()) {
            throw new UnableToInteractException("You can't harvest an empty spot!");
        }

        Entity entity = cell.getEntity();
        List<String> positionInfo = cell.getPositionInfo();

        // Check if we're dealing with an animal or plant farm
        if (farmType.equals("animal") && entity.getType().equals("animal")) {
            return handleAnimalHarvest(entity, positionInfo, row, column);
        } else if (farmType.equals("plant") && entity.getType().equals("plant")) {
            return handlePlantHarvest(entity, positionInfo, row, column);
        } else {
            throw new UnableToInteractException("You have a " + entity.getType() + " on a " + farmType + " farm!");
        }
    }

    @Override
    public boolean interact(String command, int row, int column) throws UnableToInteractException {
        switch (command) {
            case "feed":
                return handleFeedCommand(row, column);
            case "end-day":
                this.endDay();
                return true;
            case "remove":
                this.remove(row, column);
                return true;
            default:
                throw new UnableToInteractException("Unknown command: " + command);
        }
    }

    private boolean handleFeedCommand(int row, int column) throws UnableToInteractException {
        if (this.farmType.equals("animal")) {
            return feed(row, column);
        } else {
            throw new UnableToInteractException("You cannot feed something that is not an animal!");
        }
    }

        /**
     * Feed an animal at the specified location.
     * @param row the row coordinate
     * @param col the column coordinate
     * @return true iff the animal was fed, else false.
     */
    public boolean feed(int row, int col) {

       if (!isValidCell(row, col)) {
           return false;
       }

       Cell cell = farmState[row][col];

       if (!cell.isEmpty() && cell.getEntity().getType().equals("animal")) {
           List<String> positionInfo = cell.getPositionInfo();

           List<String> updatedInfo = List.of(positionInfo.get(0), positionInfo.get(1), "Fed: true", positionInfo.get(3));
           farmState[row][col].addEntity(cell.getEntity(), updatedInfo);
           return true;
       }
       return false;
    }


    public void endDay() {
        // Iterate through each cell in the farm grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Cell cell = farmState[row][col];
                if (!cell.isEmpty()) {
                    Entity entity = cell.getEntity();
                    List<String> positionInfo = cell.getPositionInfo();

                    if (this.farmType.equals("plant") && entity.getType().equals("plant")) {
                        handlePlantGrowth(entity, positionInfo, row, col);
                    } else if (this.farmType.equals("animal") && entity.getType().equals("animal")) {
                        resetAnimalState(entity, row, col);
                    }
                }
            }
        }
    }

    private void handlePlantGrowth(Entity entity, List<String> positionInfo, int row, int col) {
        String stage = positionInfo.get(2); // Get the current stage (e.g., "Stage: 0")

        switch (entity) {
            case BERRY:
                handleGrowthStages(row, col, "berry", stage, new String[]{".", "o", "@"}, 3);
                break;
            case WHEAT:
                handleGrowthStages(row, col, "wheat", stage, new String[]{"\u1F34", "#"}, 2);
                break;
            case COFFEE:
                handleGrowthStages(row, col, "coffee", stage, new String[]{":", ";", "*", "%"}, 4);
                break;
            default:
                // Unknown plant type, do nothing
                break;
        }
    }

    private void handleGrowthStages(int row, int col, String plantName, String stage, String[] growthSymbols, int maxStage) {
        int currentStage = Integer.parseInt(stage.split(": ")[1]);

        if (currentStage < maxStage) {
            farmState[row][col].addEntity(
                farmState[row][col].getEntity(),
                List.of(plantName, growthSymbols[currentStage], "Stage: " + (currentStage + 1))
            );
        }
    }

    private void resetAnimalState(Entity entity, int row, int col) {
        farmState[row][col].addEntity(
            entity,
            List.of(entity.getName(), Character.toString(entity.getSymbol()), "Fed: false", "Collected: false")
        );
    }

    /**
     * Method for retrieving the stats for the current farm.
     * @return the list describing the current farm state
     */
    public List<List<String>> getTheFarmStatsList() {
        List<List<String>> farmStats = new ArrayList<>();

        // Iterate through each row and column of the farm grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = farmState[i][j];

                // If the cell is not empty, add its positionInfo to farmStats
                if (!cell.isEmpty()) {
                    farmStats.add(cell.getPositionInfo());
                } else {
                    List<String> spotOnGrid = new ArrayList<>();
                    spotOnGrid.add("ground");
                    spotOnGrid.add(" ");
                    farmStats.add(spotOnGrid);
                }
            }
        }
        return farmStats;
    }


    private Product handleAnimalHarvest(
            Entity entity, List<String> positionInfo, int row, int column
    ) throws UnableToInteractException {

        if (positionInfo.get(2).equals("Fed: false")) {
            throw new UnableToInteractException("You have not fed this animal today!");
        }
        if (positionInfo.get(2).equals("Collected: true")) {
            throw new UnableToInteractException("This animal has produced an item already today!");
        }

        Quality quality = randomQuality.getRandomQuality();
        updateCollectedStatus(row, column, positionInfo);

        return entity.getProduct(quality);
    }

    private boolean isPlantFullyGrown(List<String> positionInfo) {
        return positionInfo.get(1).equals("#") || positionInfo.get(1).equals("%") || positionInfo.get(1).equals("@");
    }

    private Product handlePlantHarvest(Entity entity, List<String> positionInfo, int row, int column) throws UnableToInteractException {
        if (!isPlantFullyGrown(positionInfo)) {
            throw new UnableToInteractException("The crop is not fully grown!");
        }

        Quality quality = randomQuality.getRandomQuality();
        resetPlantStage(row, column, entity);

        // Use entity's barcode to get the corresponding product
        return entity.getProduct(quality);
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

    private void updateCollectedStatus(int row, int column, List<String> positionInfo) {
        List<String> updatedInfo = List.of(positionInfo.get(0), positionInfo.get(1), positionInfo.get(2), "Collected: true");
        farmState[row][column].addEntity(farmState[row][column].getEntity(), updatedInfo);
    }



}
