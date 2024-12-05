package server.database;

import commons.Debugger;
import commons.databases.PersistentDatabase;
import commons.entities.User;

import java.util.Optional;

public class UserDatabase extends PersistentDatabase<User> {

    private static final String PATH = "server/src/main/resources/databases/users.ser";

    public UserDatabase(Debugger debugger) {
        super(debugger, PATH);
        log(getAll());
    }

    /**
     * Applies rules to test if a {@code User} can connect with the provided credentials.
     *
     * @param user The {@code User} trying to connect
     * @return {@code true} if the {@code User} passes all checks
     */
    public boolean authenticateUser(User user) {
        if (user == null) return false;
        Optional<User> u = getAll().stream()
                .filter(e -> e.getUsername().equals(user.getUsername()))
                .findFirst();

        //Accept when new user
        if (u.isEmpty()) {save(user); return true; }
        //Accept when correct credentials
        else if (u.get().getPassword().equals(user.getPassword())) {
            save(user);
            return true; }
        //Deny when credentials are incorrect
        else return false;
    }
}
