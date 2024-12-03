package commons;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
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
            return (ArrayList<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
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
        if (!data.stream().anyMatch(entity -> entity.equals(obj))) data.add(obj);
        else data.stream()
                .filter(entity -> entity.equals(obj))
                .forEach(entity -> { data.set(data.indexOf(entity), obj); });
        return data;
    }

    public T remove(T obj) {
        data.removeIf(entity -> entity.equals(obj));
        return obj;
    }

    public void clear(){
        data.clear();
    }
}
