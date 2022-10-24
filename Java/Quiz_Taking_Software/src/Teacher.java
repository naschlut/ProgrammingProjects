import java.util.ArrayList;

/**
 * Teacher Class: Responsible for creating Teacher Users
 *
 * @author Aditya Menon
 * @version Nov 13, 2021
 */

public class Teacher extends User {

    private ArrayList<Course> courses = new ArrayList<>(); //list of courses created by teacher

    public Teacher(String username, String password) {

        super(username, password, true);
    }

    public void addCourse(Course c) {
        boolean alreadyThere = false;
        for (Course cs : courses) {
            if (c.equals(cs)) {
                alreadyThere = true;
            }
        }

        if (!alreadyThere) {
            courses.add(c);
        }
    }


    public void removeCourse(int courseNumber) {
        for (Course c : courses) {
            if (c.getCourseNumber() == courseNumber) {
                courses.remove(c);
            }
        }
    }

    public void editCourseName(String oldCourse, String newCourse) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseName().equals(oldCourse)) { //ensures course exists before editing
                courses.get(i).setCourseName(newCourse);
            }
        }
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public Course getCourse(int i) {
        for (Course c : courses) {
            if (c.getCourseNumber() == i) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "courses=" + courses +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isTeacher=" + isTeacher +
                '}';
    }

}
