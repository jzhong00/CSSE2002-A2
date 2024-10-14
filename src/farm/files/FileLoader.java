package farm.files;

import farm.core.farmgrid.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A class that handles loading a farm game from a text representation
 */
public class FileLoader {

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
            FarmDataLoader loader = new FarmDataLoader(reader);
            return loader.loadFarm();
        }
    }

}
