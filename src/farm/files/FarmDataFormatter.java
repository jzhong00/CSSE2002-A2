package farm.files;

import farm.core.farmgrid.FarmGrid;
import farm.core.farmgrid.Grid;

import java.util.List;
import java.util.StringJoiner;

/**
 * A class responsible for formatting farm data into a string representation.
 */
public class FarmDataFormatter {

    /**
     * Formats the farm's general information, such as type, rows, and columns.
     *
     * @param grid the grid to retrieve the farm's information from
     * @return a formatted string representing the farm info
     */
    public String formatFarmInfo(FarmGrid grid) {
        return grid.getFarmType() + "," + grid.getRows() + "," + grid.getColumns() + "\n";
    }

    /**
     * Formats the grid's entities into a string representation.
     *
     * @param grid the grid to retrieve stats from
     * @return a formatted string representing the grid
     */
    public String formatFarmGrid(Grid grid) {
        StringBuilder builder = new StringBuilder();
        List<List<String>> farmStats = grid.getStats();
        int rows = grid.getRows();
        int columns = grid.getColumns();

        for (int row = 0; row < rows; row++) {
            StringJoiner rowJoiner = new StringJoiner("|");
            for (int col = 0; col < columns; col++) {
                List<String> cellStats = farmStats.get(getPositionIndex(row, col, columns));
                rowJoiner.add(String.join(",", cellStats));
            }
            builder.append(rowJoiner).append("\n");
        }

        return builder.toString();
    }

    private int getPositionIndex(int row, int col, int totalColumns) {
        return (row * totalColumns) + col;
    }
}