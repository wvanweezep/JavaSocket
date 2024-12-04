package commons.databases;

import commons.entities.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class for holding objects of type {@code Entity}, allowing the usage of util methods.
 *
 * @param <T> The type of the {@code Entity}
 */
public abstract class Database<T extends Entity> {

    /**
     * An {@code ArrayList} containing all saved entities.
     */
    private final List<T> data;
    /**
     * The type of {@code Entity} in the database.
     */
    private final Class<T> type;

    /**
     * Constructor initializing the data array.
     *
     * @param type The type for the {@code Entity}
     */
    public Database(Class<T> type) {
        this.data = load();
        this.type = type;
    }

    /**
     * Placeholder load method returning an empty {@code ArrayList}.
     *
     * @return An empty {@code ArrayList}
     */
    protected List<T> load() {
        return new ArrayList<>();
    }

    /**
     * Getter for the database {@code Entity} type.
     *
     * @return The type of the {@code Entity} in the database
     */
    public Class<T> getType(){
        return type;
    }

    /**
     * Getter for the data array.
     *
     * @return The {@code ArrayList} containing the data
     */
    public List<T> getAll() {
        return data;
    }

    /**
     * Getter for an {@code Enity} in the database with a certain id
     *
     * @param id The id of the target object
     * @return An {@code Optional<T>} of type {@code T} with the {@code id}
     */
    public Optional<T> getById(long id) {
        return data.stream()
                .filter(entity -> entity.getId() == id)
                .findFirst();
    }

    /**
     * Save an entity to the database, updating if present or adding if it's not.
     * After saving the database is updated ({@code PersistentDatabase}).
     *
     * @param obj The entity of type {@code T} to save
     */
    public void save(T obj) {
        if (data.stream().noneMatch(entity -> entity.equals(obj))) data.add(obj);
        else data.stream()
                .filter(entity -> entity.equals(obj))
                .forEach(entity -> { data.set(data.indexOf(entity), obj); });
        write();
    }

    /**
     * Remove an {@code Entity} from the database.
     *
     * @param obj The {@code Entity} to remove
     * @return The removed {@code Entity}
     */
    public T remove(T obj) {
        data.removeIf(entity -> entity.equals(obj));
        return obj;
    }

    /**
     * Remove an {@code Entity} from the database based on an id.
     *
     * @param id The id of the {@code Entity} to remove
     * @return The removed {@code Entity}
     */
    public T removeById(long id) {
        Optional<T> obj =  data.stream().filter(entity -> entity.getId() == id).findFirst();
        if (obj.isPresent()) {
            data.removeIf(entity -> entity.getId() == id);
            return obj.get();
        }
        return null;
    }

    /**
     * Clears the entire database, leaving an empty {@code ArrayList}.
     */
    public void clear(){
        data.clear();
    }

    /**
     * Writes the data to the file ({@code PersistentDatabase}).
     */
    abstract protected void write();

    /**
     * Documents an event in the terminal.
     *
     * @param msg The message that describes the event
     * @return The object given for the logging
     * @param <T> The type for the message, any object is allowed
     */
    protected <T> T log(T msg){
        System.out.println("[Database:" + getType() + "] " + msg.toString());
        return msg;
    }
}
