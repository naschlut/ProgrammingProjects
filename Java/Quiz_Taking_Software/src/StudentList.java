import java.io.*;
import java.util.ArrayList;

/**
 * StudentList Class : Handles file reading, writing and use of the list of students
 *
 * @author Jay Mehta
 * @version Nov 13, 2021
 */

public class StudentList implements Serializable {

    public static final String FILENAME = "studentList.ser";
    private ArrayList<Student> students = new ArrayList<>();

    public static StudentList readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            StudentList studentList = (StudentList) ois.readObject();
            return studentList;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            return new StudentList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    public synchronized boolean exists(Student student) {
        return students.contains(student);
    }

    public synchronized boolean add(Student student) {
        if (students.contains(student)) {
            return false; //already contains this
        }
        students.add(student);
        return true;
    }

    public synchronized boolean removes(Student student) {
        if (students.contains(student)) {
            students.remove(student);
            return true;
        }
        return false; // does not contain it
    }

    public synchronized void saveToFile() {
        if (students == null || students.size() == 0) {
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

    public synchronized Student findStudent(String username, String password) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getUsername().equals(username) && students.get(i).getPassword().equals(password)) {
                return students.get(i);
            }
        }
        return null;
    }

    public synchronized Student findStudent(String username) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getUsername().equals(username)) {
                return students.get(i);
            }
        }
        return null;
    }
}
