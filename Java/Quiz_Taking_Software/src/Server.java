import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Server Class: Handles storage of data as well as connection with client
 *
 * @author Jay Mehta, Nathan Reed, Anh V Nguyen
 * @version Dec 13, 2021
 */

public class Server {

    static TeacherList teacherList = TeacherList.readFromFile();
    static StudentList studentList = StudentList.readFromFile();
    static CourseList courseList = CourseList.readFromFile();

    public static void main(String[] args) {
        int port = 8818;
        try {
            ServerSocket socket = new ServerSocket(port);
            while (true) {
                Socket client = socket.accept();
                Thread thread = new Concurrency(client, teacherList, studentList, courseList);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

