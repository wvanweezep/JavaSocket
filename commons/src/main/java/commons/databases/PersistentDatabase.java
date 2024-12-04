package commons.databases;

import commons.entities.Entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for holding objects of type {@code Entity}, allowing the usage of util methods.
 * Data is stored on a given location in a .ser file.
 *
 * @param <T> The type of the {@code Entity}
 */
public abstract class PersistentDatabase<T extends Entity> extends Database<T>{

    /**
     * The file path of the database .ser file.
     */
    private final String filePath;

    /**
     * Constructor loading the data from the .ser database file.
     *
     * @param filePath The file path of the database .ser file
     */
    public PersistentDatabase(String filePath) {
        super(filePath);
        this.filePath = filePath;
    }

    /**
     * Initializes and returns the data from the .ser database file.
     *
     * @param path The path to find the .ser file
     * @return An initialized {@code ArrayList} containing data from the .ser database
     */
    @SuppressWarnings("unchecked")
    @Override
    protected List<T> load(String path) {
        log("Attempting to load file: " + path);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            ArrayList<T> objects = (ArrayList<T>) ois.readObject();
            if (objects == null) {
                log("No objects found");
                return new ArrayList<>();
            }
            log("Loaded objects from file");
            return objects;
        } catch (FileNotFoundException e) {
            try {
                log("No file found, creating a new file at " + path);
                new File(path).createNewFile();
            } catch (IOException ioException) {
                log("Failed to create file: " + ioException.getMessage());
            }
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            log("Critical failure loading database: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Writes the data to the file ({@code PersistentDatabase}).
     */
    @Override
    public void write() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(super.getAll());
            log("Successfully saved data");
        } catch (IOException e) {
            log("Error saving data: " + e.getMessage());
        }
    }
}
