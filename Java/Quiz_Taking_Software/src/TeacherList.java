import java.io.*;
import java.util.ArrayList;

/**
 * TeacherList Class : Handles file reading, writing and use of the list of teachers
 *
 * @author Jay Mehta
 * @version Nov 13, 2021
 */

public class TeacherList implements Serializable {
    public static final String FILENAME = "teacherList.ser";
    private ArrayList<Teacher> teachers = new ArrayList<>();

    public static TeacherList readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            TeacherList teacherList = (TeacherList) ois.readObject();
            return teacherList;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            return new TeacherList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    public synchronized boolean exists(Teacher teacher) {
        return teachers.contains(teacher);
    }

    public synchronized boolean add(Teacher teacher) {
        if (teachers.contains(teacher)) {
            return false; //already contains this
        }
        teachers.add(teacher);
        return true;
    }

    public synchronized void saveToFile() {
        if (teachers == null || teachers.size() == 0) {
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

    public synchronized Teacher findTeacher(String username, String password) {
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getUsername().equals(username) && teachers.get(i).getPassword().equals(password)) {
                return teachers.get(i);
            }
        }
        return null;
    }

}
