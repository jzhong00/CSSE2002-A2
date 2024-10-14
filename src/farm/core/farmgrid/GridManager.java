package farm.core.farmgrid;

import java.util.List;

/**
 * Interface describing the required methods for a class that manages interacting with the grid.
 */
public interface GridManager {

    /**
     * Retrieves the number of rows in the grid.
     * @return the number of rows.
     */
    int getRows();

    /**
     * Retrieves the number of columns in the grid
     * @return the number of columns
     */
    int getColumns();

    /**
     * Determines whether the provided coordinates are a valid position on the grid.
     * @param row the row to check
     * @param column the column to check
     * @return true iff the row and column are valid.
     */
    boolean isValidCell(int row, int column);

    /**
     * Returns whether the cell is empty (i.e., no entity currently occupying the cell)
     * @param row the row to check
     * @param column the column to check
     * @return true iff the cell is empty.
     */
    boolean isCellEmpty(int row, int column);

    /**
     * Place an entity at the provided position.
     * @param row the row to place the entity at
     * @param column the column to place the entity at
     * @param initialPlace true if the entity is being placed for the first time,
     *                     or false if it is being updated (e.g., harvested, fed, etc.).
     * @param entity the FarmEntity to place
     * @param positionInfo the entity's associated information (i.e., stage, name, etc.)
     * @return true iff the entity was successfully placed.
     * @requires cell location is valid
     * @requires if the entity is being placed for the first time, that the cell is empty.
     */
    boolean placeEntity(
            int row,
            int column,
            boolean initialPlace,
            FarmEntity entity,
            List<String> positionInfo)
    ;

    /**
     * Removes an entity from the specified position.
     * @param row the row where the entity is located.
     * @param column the column where the entity is located.
     * @requires the location to be a valid position on the grid.
     */
    boolean removeEntity(int row, int column);

    /**
     * Returns the Cell at the given location
     * @param row the row the cell is at.
     * @param column the column the cell is at.
     * @return the cell of the grid.
     */
    Cell getCell(int row, int column);

    /**
     * Creates and returns the representation of the grid for display.
     * @return a string representing the farm
     */
    String getGridDisplay();

    /**
     * Retrieves the grid and all associated information for each cell.
     * @return a list of lists representing each cell.
     */
    List<List<String>> getTheFarmStatsList();
}
