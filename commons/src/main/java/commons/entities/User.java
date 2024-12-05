package commons.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class User extends Entity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public User(String username, String password) {
        generateId(List.of(username, password));
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
