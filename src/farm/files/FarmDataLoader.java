package farm.files;

import farm.core.farmgrid.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class responsible for parsing farm data from a reader and transforming it into a usable FarmGrid.
 */
public class FarmDataLoader {
    private final BufferedReader reader;

    /**
     * Constructor for the FarmDataLoader
     */
    public FarmDataLoader(BufferedReader reader) {
        this.reader = reader;

    }

    /**
     * Loads the contents into a Grid.
     * @return a grid instance.
     * @throws IOException if there is an error reading the file
     */
    public Grid loadFarm() throws IOException {
        // Read header
        FarmGrid farmGrid = loadFarmInfo();
        // Read grid
        List<List<String>> grid = loadFarmGrid();
        // Update entity information
        populateFarm(farmGrid, grid);

        return farmGrid;
    }

    /**
     * Loads the farm type, number of rows and number of columns from the file header.
     * @return a FarmGrid object with the specified information
     * @throws IOException if the file is empty or missing the info line.
     */
    private FarmGrid loadFarmInfo() throws IOException {
        String infoLine = reader.readLine();
        if (infoLine == null) {
            throw new IOException("The file is empty or missing the farm information line");
        }

        String[] farmInfo = infoLine.split(",");
        if (farmInfo.length != 3) {
            throw new IOException("Invalid farm information line. "
                    + "Expected farmType, number of rows, number of columns.");
        }

        String farmType = farmInfo[0];
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

    /**
     * Loads the farm grid data from the file.
     * @return a 2D list representing the grid data.
     * @throws IOException if there is an error reading the file.
     */
    private List<List<String>> loadFarmGrid() throws IOException {
        List<List<String>> grid = new ArrayList<>();
        String line;
        // Get the information for each cell in the grid
        while ((line = reader.readLine()) != null) {
            String[] cells = line.split("\\|");
            grid.add(new ArrayList<>(Arrays.asList(cells)));
        }
        return grid;
    }

    /**
     * Updates the farm grid game based on the state read from the file.
     * @param farmGrid the grid to update.
     * @param farmStats the state read from the file.
     */
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
