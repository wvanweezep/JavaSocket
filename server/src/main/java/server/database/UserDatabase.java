package server.database;

import commons.databases.PersistentDatabase;
import commons.entities.User;

public class UserDatabase extends PersistentDatabase<User> {
    private static final String PATH = "server/src/main/resources/databases/users.ser";
    public UserDatabase() {
        super(PATH);
    }
}
