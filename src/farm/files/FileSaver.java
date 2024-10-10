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
        try (FileWriter writer = new FileWriter(filename)) {
            writeFarmInfo(writer, grid);
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
                List<String> cellStats = farmStats.get(row * rows + col);
                writer.write(String.join(",", cellStats));
                if (col < columns - 1) {
                    writer.write("|");
                }
            }
            writer.write("\n");
        }
    }
}
