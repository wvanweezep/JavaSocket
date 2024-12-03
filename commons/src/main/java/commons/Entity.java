package commons;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public abstract class Entity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private long id;

    public long getId() { return id; }

    protected void generateId(List<Object> objects) {
        StringBuilder concat = new StringBuilder();
        for (Object obj : objects) { concat.append(obj.toString()); }
        id = concat.toString().hashCode() & 0xFFFFFFFFFFFFFFFL;
    }
}
