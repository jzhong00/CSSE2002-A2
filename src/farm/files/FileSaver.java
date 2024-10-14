package farm.files;

import farm.core.farmgrid.FarmGrid;
import farm.core.farmgrid.Grid;

import java.io.*;

/**
 * A class that manages saving the current state of a farm into a txt format.
 */
public class FileSaver {

    private final FarmDataFormatter dataFormatter;


    /**
     * Constructor for the FileSaver class.
     */
    public FileSaver(FarmDataFormatter dataFormatter) {
        this.dataFormatter = dataFormatter;
    }


    /**
     * Saves the contents of the provided grid and farmtype to a file with specified name.
     *
     * @param filename the name of the file to be saved
     * @param grid the grid instance to save to a file
     * @throws IOException if an IO error occurs during saving
     */
    public void save(String filename, Grid grid) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            // Write header and grid data
            writer.write(dataFormatter.formatFarmInfo((FarmGrid) grid));
            writer.write(dataFormatter.formatFarmGrid(grid));
        }
    }
}
