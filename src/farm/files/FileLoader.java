package farm.files;

import farm.core.UnableToInteractException;
import farm.core.farmgrid.FarmGrid;
import farm.core.farmgrid.Grid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * @throws IOException
     */
    public Grid load(String filename) throws IOException {
        List<List<String>> farmStats = new ArrayList<>();
        int rows = 0;
        int cols = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String infoLine = reader.readLine();
            if (infoLine != null) {
                String[] farmInfo = infoLine.split(",");
                this.farmType = farmInfo[0];
                rows = Integer.parseInt(farmInfo[1]);
                cols = Integer.parseInt(farmInfo[2]);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] cells = line.split("\\|");
                List<String> row = new ArrayList<>(Arrays.asList(cells));
                farmStats.add(row);

                if (rows == 0) {
                    rows = row.size();
                }
            }
        }

        FarmGrid farmGrid = new FarmGrid(rows, cols, this.farmType);

        this.populateFarm(farmGrid, farmStats);

        System.out.println(farmGrid.getStats());
        return farmGrid;
    }


    private void populateFarm(FarmGrid farmGrid, List<List<String>> farmStats) {
        for (int row = 0; row < farmGrid.getRows(); row++) {
            List<String> rowInfo = farmStats.get(row);
            for (int col = 0; col < farmGrid.getColumns(); col++) {
                String cellInfo = rowInfo.get(col);
                List<String> entityParts = List.of(cellInfo.split(","));
                if (entityParts.size() > 2) {
                    char entitySymbol = entityParts.get(1).charAt(0);
                    try {
                        farmGrid.addToCell(row, col, entitySymbol, entityParts);
                    } catch (UnableToInteractException e) {
                        System.out.println("Unable to add entity to grid");
                    }
                }
            }
        }
    }
}
