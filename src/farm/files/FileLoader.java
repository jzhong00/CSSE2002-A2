package farm.files;

import farm.core.farmgrid.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class that handles loading a farm game from a text representation
 */
public class FileLoader {
    private String farmType;

    /**
     * Constructor for the FileLoader
     */
    public FileLoader() {
    }

    /**
     * Loads contents of the specified file into a Grid.
     * @param filename the String filename to read contents from.
     * @return a grid instance.
     * @throws IOException if there is an error reading the file
     */
    public Grid load(String filename) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Read header
            FarmGrid farmGrid = loadFarmInfo(reader);
            // Read grid
            List<List<String>> grid = loadFarmGrid(reader);
            // Update entity information
            populateFarm(farmGrid, grid);
            return farmGrid;
        }
    }

    private FarmGrid loadFarmInfo(BufferedReader reader) throws IOException {
        String infoLine = reader.readLine();
        if (infoLine == null) {
            throw new IOException("The file is empty or missing the farm information line");
        }

        String[] farmInfo = infoLine.split(",");
        if (farmInfo.length != 3) {
            throw new IOException("Invalid farm information line. "
                    + "Expected farmType, number of rows, number of columns.");
        }

        this.farmType = farmInfo[0];
        int rows = Integer.parseInt(farmInfo[1]);
        int cols = Integer.parseInt(farmInfo[2]);

        GridManager farmGridManager = new FarmGridManager(rows, cols);
        InteractionManager entityInteractionManager = new EntityInteractionManager(farmGridManager);
        // Create and return the instantiated FarmGrid
        return new FarmGrid(
                farmGridManager,
                entityInteractionManager,
                FarmType.fromString(farmType)
        );
    }

    private List<List<String>> loadFarmGrid(BufferedReader reader) throws IOException {
        List<List<String>> grid = new ArrayList<>();
        String line;
        // Get the information for each cell in the grid
        while ((line = reader.readLine()) != null) {
            String[] cells = line.split("\\|");
            grid.add(new ArrayList<>(Arrays.asList(cells)));
        }
        return grid;
    }


    private void populateFarm(FarmGrid farmGrid, List<List<String>> farmStats) {
        // Iterate through each cell
        for (int row = 0; row < farmGrid.getRows(); row++) {
            List<String> rowInfo = farmStats.get(row);
            for (int col = 0; col < farmGrid.getColumns(); col++) {

                // Get cell information
                String cellInfo = rowInfo.get(col);
                List<String> entityParts = List.of(cellInfo.split(","));

                if (entityParts.size() > 2) {
                    // The cell is not empty, update the entity information
                    String name = entityParts.getFirst();
                    farmGrid.addToCell(row, col, name, entityParts);
                }
            }
        }
    }
}
