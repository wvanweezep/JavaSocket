package commons.databases;

import commons.entities.Entity;

import java.io.*;
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
     * @param type The type for the {@code Entity}
     * @param filePath The file path of the database .ser file
     */
    public PersistentDatabase(Class<T> type, String filePath) {
        super(type);
        this.filePath = filePath;
    }

    /**
     * Initializes and returns the data from the .ser database file.
     *
     * @return An initialized {@code ArrayList} containing data from the .ser database
     */
    @SuppressWarnings("unchecked")
    @Override
    protected List<T> load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            ArrayList<T> objects = (ArrayList<T>) ois.readObject();
            if (objects == null) {
                return new ArrayList<>();
            }
            return objects;
        } catch (FileNotFoundException e) {
            try {
                new File(filePath).createNewFile();
            } catch (IOException ioException) {
                System.out.println("Error creating the file: " + ioException.getMessage());
            }
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
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
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
