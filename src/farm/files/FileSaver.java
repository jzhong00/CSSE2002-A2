package farm.files;

import farm.core.farmgrid.FarmGrid;
import farm.core.farmgrid.Grid;

import java.io.*;
import java.util.List;

public class FileSaver {

    /**
     * Constructor for the FileSaver class.
     */
    public FileSaver() {
    }

    /**
     * Saves the contents of the provided grid and farmtype to a file with specified name.
     * @param filename
     * @param grid the grid instance to save to a file
     * @throws IOException
     */
    public void save(String filename, Grid grid) throws IOException {
        List<List<String>> farmStats = grid.getStats();
        int numRows = grid.getRows();
        int numCols = grid.getColumns();
        FarmGrid farmGrid = (FarmGrid) grid;
        String farmType = farmGrid.getFarmType();

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(farmType + "," + numRows + "," + numCols + "\n");
            for (int i = 0; i < numRows; i++) {
                // Create a row by extracting the stats for each cell in the current row
                for (int j = 0; j < numCols; j++) {
                    List<String> cellStats = farmStats.get(i * numCols + j);  // Access cell in row-major order
                    writer.write(String.join(",", cellStats));  // Write cell contents
                    if (j < numCols - 1) {
                        writer.write("|");  // Add a comma between cells, except after the last one in the row
                    }
                }
                writer.write("\n");  // Move to the next line after writing a full row
            }
        }
    }
}
