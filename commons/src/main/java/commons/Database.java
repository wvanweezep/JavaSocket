package commons;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Database<T extends Entity> {

    private final String filePath;
    private final List<T> data;

    public Database(String filePath) {
        this.filePath = filePath;
        data = load();
    }

    private List<T> load() {
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

    public List<T> getAll(){
        return data;
    }

    public Optional<T> getById(long id) {
        return data.stream()
                .filter(entity -> entity.getId() == id)
                .findFirst();
    }

    public List<T> save(T obj) {
        if (data.stream().noneMatch(entity -> entity.equals(obj))) data.add(obj);
        else data.stream()
                .filter(entity -> entity.equals(obj))
                .forEach(entity -> { data.set(data.indexOf(entity), obj); });
        write();
        return data;
    }

    public T remove(T obj) {
        data.removeIf(entity -> entity.equals(obj));
        return obj;
    }

    public T removeById(long id) {
        Optional<T> obj =  data.stream().filter(entity -> entity.getId() == id).findFirst();
        if (obj.isPresent()) {
            data.removeIf(entity -> entity.getId() == id);
            return obj.get();
        }
        return null;
    }

    public void clear(){
        data.clear();
    }

    public void write() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
