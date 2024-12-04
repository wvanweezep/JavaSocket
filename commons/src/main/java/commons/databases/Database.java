package commons.databases;

import commons.entities.Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Database<T extends Entity> {

    private final List<T> data;

    public Database() {
        data = load();
    }

    protected List<T> load() {
        return new ArrayList<>();
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

    abstract protected void write();
}
