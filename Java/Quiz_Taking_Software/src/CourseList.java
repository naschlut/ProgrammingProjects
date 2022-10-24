import java.io.*;
import java.util.ArrayList;
/**
 * CourseList Class : Handles file reading, writing and use of the list of courses
 *
 * @author Jay Mehta
 * @version Nov 13, 2021
 */
public class CourseList implements Serializable {

    public static final String FILENAME = "courseList.ser";
    private ArrayList<Course> courses = new ArrayList<>();

    public static CourseList readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            CourseList courseList = (CourseList) ois.readObject();
            return courseList;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            return new CourseList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    public synchronized ArrayList<Course> getCourses() {
        return courses;
    }

    public synchronized Course getCourse(int i) {
        for (Course c : courses) {
            if (c.getCourseNumber() == i) {
                return c;
            }
        }
        return null;
    }

    public synchronized boolean exists(Course course) {
        return courses.contains(course);
    }

    public synchronized boolean add(Course course) {
        if (courses.contains(course)) {
            return false; //already contains this
        }
        courses.add(course);
        return true;
    }

    public synchronized boolean update(Course course) {
        if (!exists(course)) {
            return false;
        }
        for (int i = 0; i < courses.size(); i++) {
            if (course.getCourseNumber() == courses.get(i).getCourseNumber()) {
                courses.set(i, course);
                return true;
            }
        }
        return false;
    }

    public synchronized void saveToFile() {
        if (courses == null || courses.size() == 0) {
            return; // nothing to save
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(this);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
