package farm.core.farmgrid;

public class GridManager {
    private Cell[][] grid;
    private int rows;
    private int columns;

    public GridManager(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new Cell[rows][columns];

       // populate the initial farm with empty ground
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public boolean isValidCell(int row, int column) {
       return (row >= 0 && row < rows && column >= 0 && column < columns);
    }

    public Cell getCell(int row, int column) {
        if (isValidCell(row, column)) {
            return grid[row][column];
        }
        throw new IllegalArgumentException("Invalid cell coordinates");
    }

    public void removeEntity(int row, int column) {
        if (isValidCell(row, column)) {
            grid[row][column] = null;
        }
        throw new IllegalArgumentException("Invalid cell coordinates");
    }

}
