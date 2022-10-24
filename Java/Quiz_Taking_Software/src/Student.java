import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Student Class: Responsible for creating Student Users
 *
 * @author Aditya Menon
 * @version Nov 13, 2021
 */

public class Student extends User {

    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Double> grades = new ArrayList<>();
    private ArrayList<Quiz> quizzesTaken = new ArrayList<>();
    private ArrayList<Quiz> quizzesTakenWithScores = new ArrayList<>();
    private ArrayList<Submission> submissions = new ArrayList<>();

    public Student(String username, String password) {
        super(username, password, false);
    }

    public ArrayList<Submission> getSubmissions() {
        return submissions;
    }

    public void addToCourse(Course course, String courseFilePath) {
        boolean exists = false;
        boolean alreadyThere = false;

        for (Course c : courses) {
            if (c.equals(course)) {
                alreadyThere = true;
            }
        }
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(courseFilePath));
            String check = "";
            while ((check = bfr.readLine()) != null) {
                if (check.equals(course)) {
                    exists = true;
                }
            }

            bfr.close();

            if (!exists && !alreadyThere) {
                courses.add(course);
                System.out.println("Course added!");
            } else {
                System.out.println("Course already being taken by student or does not exist!");
            }
        } catch (IOException io) {
            System.out.println("Error reading file!");
        }

    }

    public void addToCourse(Course c) {
        courses.add(c);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }

    public void addGradePlusQuiz(Quiz quiz, double grade) {
        quizzesTakenWithScores.add(quiz);
        grades.add(grade);
    }

    public String showTotalScores() {
        String s = "";
        if (quizzesTakenWithScores.size() > 0) {
            for (int i = 0; i < quizzesTakenWithScores.size(); i++) {
                s += quizzesTakenWithScores.get(i).getName() + ": " + grades.get(i) + "\n";
            }
            return s;
        } else {
            return "No scores assigned!";
        }
    }

    public String showQuizzesTaken() {
        String s = "";
        if (quizzesTaken.size() > 0) {
            for (int i = 0; i < quizzesTaken.size(); i++) {
                s += quizzesTaken.get(i).getName() + "\n";
            }
            return s;
        } else {
            return "No quizzes taken!";
        }
    }

    public void addQuizTaken(Quiz quiz) {
        quizzesTaken.add(quiz);
    }

    public String showStringCourses() {
        String s = "Courses: \n";
        for (int i = 0; i < courses.size(); i++) {
            s += courses.get(i).getCourseName() + "\n";
        }

        return s;
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

    public void addSubmission(Submission s) {
        submissions.add(s);
    }

    @Override
    public String toString() {
        return "Student{" +
                "courses=" + courses +
                ", grades=" + grades +
                ", quizzesTaken=" + quizzesTaken +
                ", quizzesTakenWithScores=" + quizzesTakenWithScores +
                ", submissions=" + submissions +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isTeacher=" + isTeacher +
                '}';
    }
}
