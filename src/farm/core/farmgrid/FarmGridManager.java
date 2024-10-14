package farm.core.farmgrid;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that manages the grid for the farm.
 */
public class FarmGridManager implements GridManager {

    private final Cell[][] grid;
    private final int rows;
    private final int columns;

    /**
     * Constructs the FarmGridManager.
     * @param rows the number of rows in the grid
     * @param columns the number of columns in the grid
     * @throws IllegalArgumentException if the provided rows and columns are not valid
     * @requires rows > 0 and columns > 0
     */
    public FarmGridManager(int rows, int columns) {
        // Check the number of rows and columns are greater than 0
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Rows and columns must be greater than 0");
        }

        this.rows = rows;
        this.columns = columns;
        // Create a grid of cells with the size rows x columns
        this.grid = new Cell[rows][columns];

        initialiseGrid();
    }

    /**
     * Initialises each cell in the grid
     */
    private void initialiseGrid() {
        // Iterate through each position
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // Create a new cell in that location
                grid[i][j] = new Cell();
            }
        }
    }

    @Override
    public boolean isValidCell(int row, int column) {
        return (row >= 0 && row < rows && column >= 0 && column < columns);
    }

    @Override
    public boolean isCellEmpty(int row, int column) {
        Cell cell = grid[row][column];
        return cell.isEmpty();
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public boolean placeEntity(
            int row,
            int column,
            boolean initialPlace,
            FarmEntity entity,
            List<String> positionInfo
    ) {
        // Check if the position is valid
        if (!isValidCell(row, column)) {
            return false;
        }

        // Check the cell is empty if an entity is being placed for the first time
        if (initialPlace) {
            if (!isCellEmpty(row, column)) {
                throw new IllegalStateException("Something is already there!");
            }
        }

        // Add the entity to the cell
        Cell cell = grid[row][column];
        cell.addEntity(entity, positionInfo);

        return true;
    }

    @Override
    public boolean removeEntity(int row, int column) {
        if (isValidCell(row, column)) {
            grid[row][column].removeEntity();
            return true;
        }
        return false;
    }

    @Override
    public Cell getCell(int row, int column) {
        if (!isValidCell(row, column)) {
            throw new IllegalArgumentException("Not a valid location!");
        }
        return grid[row][column];
    }

    @Override
    public String getGridDisplay() {
        // create the fence at the top of the farm
        // two lines for each column of the farm, plus two for edges
        // and one for extra space
        StringBuilder sb = new StringBuilder();
        String horizontalFence = "-".repeat((getColumns() * 2) + 3);

        sb.append(horizontalFence).append(System.lineSeparator());

        // start each line with a "|" fence character
        // then display symbols with a space either side

        // note System.lineSeparator() is just \n but ensures it works
        // on all operating systems.

        for (int i = 0; i < getRows(); i++) {
            sb.append("| ");
            for (int j = 0; j < getColumns(); j++) {

                Cell cell = getCell(i, j);
                // Set the cell as ground
                char symbol = ' ';

                // Update the cell with the entity symbol if it is not empty
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


    /**
     * Method for retrieving the stats for the current farm.
     * @return the list describing the current farm state
     */
    @Override
    public List<List<String>> getTheFarmStatsList() {
        List<List<String>> farmStats = new ArrayList<>();

        // Iterate through each row and column of the farm grid
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                Cell cell = getCell(i, j);

                // If the cell is not empty, add its positionInfo to farmStats
                if (!cell.isEmpty()) {
                    farmStats.add(cell.getPositionInfo());
                } else {
                    // Add the representation for ground
                    List<String> spotOnGrid = new ArrayList<>();
                    spotOnGrid.add("ground");
                    spotOnGrid.add(" ");
                    farmStats.add(spotOnGrid);
                }
            }
        }
        return farmStats;
    }
}
