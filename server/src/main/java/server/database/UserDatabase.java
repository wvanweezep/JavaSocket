package server.database;

import commons.Database;
import commons.User;

public class UserDatabase extends Database<User> {
    private static final String PATH = "server/src/main/resources/databases/users.ser";
    public UserDatabase() {
        super(PATH);
        save(new User("tesng", "name"));
        System.out.println(getAll());
    }
}
