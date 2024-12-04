package commons.databases;

import commons.entities.Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class PersistentDatabase<T extends Entity> extends Database<T>{

    private final String filePath;

    public PersistentDatabase(String filePath) {
        this.filePath = filePath;
    }

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
