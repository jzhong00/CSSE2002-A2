package farm.core.farmgrid;

import java.util.ArrayList;
import java.util.List;

public class GridManager {

    private final Cell[][] grid;
    private final int rows;
    private final int columns;

    public GridManager(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Rows and columns must be greater than 0");
        }

        this.rows = rows;
        this.columns = columns;
        this.grid = new Cell[rows][columns];

        initialiseGrid();
    }

    private void initialiseGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public boolean isValidCell(int row, int column) {
        return (row >= 0 && row < rows && column >= 0 && column < columns);
    }

    public boolean isCellEmpty(int row, int column) {
        Cell cell = grid[row][column];
        return cell.isEmpty();
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean placeEntity(int row, int column, FarmEntity entity, List<String> positionInfo) {
        if (!isValidCell(row, column)) {
            return false;
        }
        if (!isCellEmpty(row, column)) {
            return false;
        }

        Cell cell = grid[row][column];
        cell.addEntity(entity, positionInfo);

        return true;
    }

    public void removeEntity(int row, int column) {
        if (isValidCell(row, column)) {
            grid[row][column].removeEntity();
        }
    }

    public Cell getCell(int row, int column) {
        return grid[row][column];
    }

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


    /**
     * Method for retrieving the stats for the current farm.
     * @return the list describing the current farm state
     */
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
