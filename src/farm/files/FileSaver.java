package farm.files;

import farm.core.farmgrid.FarmGrid;
import farm.core.farmgrid.Grid;

import java.io.*;
import java.util.List;

/**
 * A class that manages saving the current state of a farm into a txt format.
 */
public class FileSaver {

    /**
     * Constructor for the FileSaver class.
     */
    public FileSaver() {
    }

    /**
     * Saves the contents of the provided grid and farmtype to a file with specified name.
     * @param filename the name of the file to be saved
     * @param grid the grid instance to save to a file
     * @throws IOException if an IO error occurs during saving
     */
    public void save(String filename, Grid grid) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            // Write header (e.g., "Plant,3,4")
            writeFarmInfo(writer, grid);

            // Write the grid with the entities
            writeFarmGrid(writer, grid);
        }
    }

    private void writeFarmInfo(FileWriter writer, Grid grid) throws IOException {
        FarmGrid farmGrid = (FarmGrid) grid;
        writer.write(
                farmGrid.getFarmType()
                    + "," + farmGrid.getRows()
                    + "," + farmGrid.getColumns()
                    + "\n"
        );
    }

    private void writeFarmGrid(FileWriter writer, Grid grid) throws IOException {
        List<List<String>> farmStats = grid.getStats();
        int rows = grid.getRows();
        int columns = grid.getColumns();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Get cell at that position
                List<String> cellStats = farmStats.get(getPositionIndex(row, col, columns));

                // Write that cell with each item in the array joined using a comma
                writer.write(String.join(",", cellStats));

                // Add a separator if it is not the final column
                if (!isFinalColumn(col, columns)) {
                    writer.write("|");
                }
            }
            writer.write("\n");
        }
    }

    private int getPositionIndex(int row, int col, int totalColumns) {
        return (row * totalColumns) + col;
    }

    private boolean isFinalColumn(int col, int columns) {
        return col >= columns - 1;
    }
}
