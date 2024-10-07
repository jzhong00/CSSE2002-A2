package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.*;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.RandomQuality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FarmGrid implements Grid {

    private Cell[][] farmState;
    private final int rows;
    private final int columns;
    private final RandomQuality randomQuality;
    private final String farmType;


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


        this.rows = rows;
        this.columns = columns;
        this.randomQuality = new RandomQuality();
        this.farmState = new Cell[rows][columns];
        this.farmType = farmType;

        // Populate the initial farm with empty ground
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
        return farmType;
    }

    @Override
    public boolean place(int row, int column, char symbol) {

        if (!isValidCell(row, column)) {
            return false;
        }

        FarmEntity entity;
        try {
            entity = FarmEntity.createFarmEntity(symbol, this.getFarmType());
        } catch (IllegalArgumentException | UnableToInteractException e) {
            return false;
        }
        if (!this.getFarmType().equals(entity.getType())) {
            throw new IllegalArgumentException(
                    "You cannot place a " + entity.getType() + " on a " + this.getFarmType()
            );
        }

        Cell cell = farmState[row][column];
        if (!cell.isEmpty()) {
            throw new IllegalStateException("Something is already there!");
        }

        return this.placeEntity(row, column, entity);
    }

    protected boolean placeEntity(int row, int column, FarmEntity entity) {
        String[] additionalInfo;
        if (entity.getType().equals("plant")) {
            additionalInfo = new String[]{"Stage: 1"};
        } else {
            additionalInfo = new String[]{"Fed: false", "Collected: false"};
        }
        List<String> positionInfo = new ArrayList<>(List.of(
                entity.getName(),
                String.valueOf(entity.getSymbol())
        ));
        positionInfo.addAll(Arrays.asList(additionalInfo));
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

    // TODO: Should be in GridManager
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
                    symbol = (char) cell.getEntity().getSymbol();
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

    public Product harvest(int row, int column) throws UnableToInteractException {
        if (!isValidCell(row, column)) {
            throw new UnableToInteractException("You can't harvest this location");
        }

        Cell cell = farmState[row][column];
        FarmEntity entity = validateEntityForHarvest(cell);
        Quality quality = randomQuality.getRandomQuality();

        List<String> positionInfo = entity.harvestEntity();
        farmState[row][column].addEntity(entity, positionInfo);

        return entity.getProduct(quality);
    }

    private FarmEntity validateEntityForHarvest(Cell cell) throws UnableToInteractException {
        if (cell.isEmpty()) {
            throw new UnableToInteractException("You can't harvest an empty spot!");
        }

        FarmEntity entity = cell.getEntity();

        if (!this.getFarmType().equals(entity.getType())) {
            throw new IllegalArgumentException(
                    "You cannot place a " + entity.getType() + " on a " + this.getFarmType()
            );
        }
        entity.checkReadyForHarvest();

        return entity;
    }


    @Override
    public boolean interact(String command, int row, int column) throws UnableToInteractException {
        if (!isValidCell(row, column)) {
            return false;
        }
        switch (command) {
            case "feed":
                FarmEntity entity = farmState[row][column].getEntity();
                List<String> positionInfo = entity.feed();
                farmState[row][column].addEntity(entity, positionInfo);
                return true;
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



    public void endDay() {
        // Iterate through each cell in the farm grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Cell cell = farmState[row][col];
                if (!cell.isEmpty()) {
                    FarmEntity entity = cell.getEntity();
                    List<String> positionInfo = entity.reset();
                    farmState[row][col].addEntity(entity, positionInfo);
                }
            }
        }
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

    public void addToCell(int row, int col, char entitySymbol, List<String> positionInfo) throws UnableToInteractException {
        Cell cell = farmState[row][col];
        FarmEntity entity = FarmEntity.createFarmEntity(entitySymbol, this.getFarmType());
        if (this.getFarmType().equals("plant")) {
            int stage = positionInfo.get(2).charAt(positionInfo.get(2).length() - 1);
            ((Plant) entity).setGrowthStage(stage);
        } else {
            boolean fed = false;
            boolean collected = false;

            if (positionInfo.get(2).contains("true")) {
                fed = true;
            }
            if (positionInfo.get(3).contains("true")) {
                collected = true;
            }
            ((Animal) entity).setFed(fed);
            ((Animal) entity).setCollected(collected);
        }

        cell.addEntity(entity, positionInfo);
    }

}
