import java.io.Serializable;

/**
 * User Class: Creates User objects for login
 *
 * @author Aditya Menon, Jay Mehta
 * @version Nov 13, 2021
 */

public class User implements Serializable {
    String username;
    String password;
    boolean isTeacher;

    public User(String username, String password, boolean isTeacher) {
        this.username = username;
        this.password = password;
        this.isTeacher = isTeacher;
    }

    public User() {
        username = null;
        password = null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return (user.getUsername().equals(username) && user.isTeacher() == isTeacher);
    }

}
