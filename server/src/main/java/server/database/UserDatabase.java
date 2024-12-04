package server.database;

import commons.databases.PersistentDatabase;
import commons.entities.User;

import java.util.Optional;

public class UserDatabase extends PersistentDatabase<User> {
    private static final String PATH = "server/src/main/resources/databases/users.ser";
    public UserDatabase() {
        super(PATH);
        log(getAll());
    }

    public boolean authenticateUser(User user) {
        if (user == null) return false;
        Optional<User> u = getAll().stream()
                .filter(e -> e.getUsername().equals(user.getUsername()))
                .findFirst();
        if (u.isEmpty()) {save(user); return true; }
        else if (u.get().getPassword().equals(user.getPassword())) {
            save(user);
            return true;
        } else return false;
    }
}
