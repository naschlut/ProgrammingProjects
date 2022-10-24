import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Random;
/**
 * Submission Class : handles the process of student taking the quiz, the grading of that submission, file writing,
 * score assignment, and viewing previous submissions
 *
 * @author Jay Mehta
 * @version Nov 13, 2021
 */
public class Submission implements Serializable {

    Student student;
    Quiz quizz;
    Timestamp timestamp;
    int totalScore;
    String filename;
    ArrayList<String> answers =new ArrayList<String>();

    public Submission(Student student, Quiz quizz) {
        this.student = student;
        this.quizz = quizz;
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        this.timestamp = ts;
    }

    public Student getStudent() {
        return student;
    }

    public String getFilename() {
        return this.filename;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Course getCourse() {
        return this.quizz.getCourse();
    }

    public Quiz getQuiz() {
        return quizz;
    }

    public ArrayList<String> getAnswers(){
        return answers;
    }

    public boolean takeQuiz(Scanner scanner) {
        ArrayList<String> answers = new ArrayList<>();
        ArrayList<Question> questions = new ArrayList<>();
        String studentAnswer;
        for (int i = 0; i < quizz.getQuiz().size(); i++) {
            Question temp = quizz.getQuiz().get(i);
            if (quizz.getQuiz().get(i).getClass() == FillInTheBlank.class) {
                FillInTheBlank temp2 = (FillInTheBlank) temp;
                temp2.displayQuestion();
                String fileOrType = "1. Do you want to attach a file for the answer?\n2. Do you want to type the " +
                        "answer?";
                int option = Menu.getIntegerFromScanner(scanner, fileOrType, 1, 2, true);
                if (option == 1) {
                    studentAnswer = readingTheAnswerFromFile(scanner);
                    if (studentAnswer == null) {
                        System.out.println("You submitted an invalid file for the answer");
                        return false;
                    }
                } else {
                    studentAnswer = Menu.getStringFromScanner(scanner, "Your answer: ", false);
                }
                answers.add(studentAnswer);
                questions.add(temp);
            } else if (quizz.getQuiz().get(i).getClass() == TrueFalse.class) {
                TrueFalse temp2 = (TrueFalse) temp;
                temp2.displayQuestion();
                String fileOrType = "1. Do you want to attach a file for the answer?\n2. Do you want to type the " +
                        "answer?";
                int option = Menu.getIntegerFromScanner(scanner, fileOrType, 1, 2, true);
                if (option == 1) {
                    studentAnswer = readingTheAnswerFromFile(scanner);
                    if (studentAnswer == null) {
                        System.out.println("You submitted an invalid file for the answer");
                        return false;
                    }
                } else {
                    studentAnswer = Menu.getStringFromScanner(scanner, "Your answer: ", false);
                }
                answers.add(studentAnswer);
                questions.add(temp);
            } else if (quizz.getQuiz().get(i).getClass() == MultipleChoice.class) {
                MultipleChoice temp2 = (MultipleChoice) temp;
                temp2.displayQuestion();
                String fileOrType = "1. Do you want to attach a file for the correct answer index?\n2. Do you want to "
                        + "type the correct answer index?";
                int option = Menu.getIntegerFromScanner(scanner, fileOrType, 1, 2, true);
                if (option == 1) {
                    studentAnswer = readingTheAnswerFromFile(scanner);
                    if (studentAnswer == null) {
                        System.out.println("You submitted an invalid file for the answer");
                    }
                } else {
                    do {
                        try {
                            System.out.print("Your answer:");
                            studentAnswer = scanner.nextLine();
                            int number = Integer.parseInt(studentAnswer);
                            number--;
                            studentAnswer = String.valueOf(number);
                            break;
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    } while (true);
                }
                answers.add(studentAnswer);
                questions.add(temp);
            }

        }
        submissionReport(answers, questions);
        return true;
    }

    public void submissionReport(ArrayList<String> answers, ArrayList<Question> questions) {
        this.answers=answers;
        ArrayList<String> tempAnswers=new ArrayList<>();
        String filepath = createsNewFile();
        try (PrintWriter pw = new PrintWriter(new FileWriter(filepath))) {
            pw.println(student.getUsername() + ": " + quizz.getName());
            for (int i = 0; i < answers.size(); i++) {
                if (questions.get(i).getClass() == FillInTheBlank.class) {
                    tempAnswers.add(answers.get(i));
                    FillInTheBlank temp = (FillInTheBlank) questions.get(i);
                    pw.print("Question " + (i + 1) + ". ");
                    pw.println(temp.getQuestion());
                    pw.print("Answer " + (i + 1) + ". ");
                    pw.println(temp.getAnswer());
                    pw.println("Your answer: " + (answers.get(i)));
                    if (temp.getAnswer().equalsIgnoreCase(answers.get(i))) {
                        pw.println("Points got: " + temp.getPointValue());
                        totalScore += temp.getPointValue();
                    } else {
                        pw.println("Points got: " + 0);
                    }
                } else if (questions.get(i).getClass() == TrueFalse.class) {
                    tempAnswers.add(answers.get(i));
                    TrueFalse temp = (TrueFalse) questions.get(i);
                    pw.print("Question " + (i + 1) + ". ");
                    pw.println(temp.getQuestion());
                    pw.print("Answer " + (i + 1) + ". ");
                    pw.println(temp.getAnswer());
                    pw.println("Your answer: " + ((answers.get(i))));
                    if (temp.getAnswer().equalsIgnoreCase(answers.get(i))) {
                        pw.println("Points got: " + temp.getPointValue());
                        totalScore += temp.getPointValue();
                    } else {
                        pw.println("Points got: " + 0);
                    }
                } else if (questions.get(i).getClass() == MultipleChoice.class) {
                    MultipleChoice temp = (MultipleChoice) questions.get(i);
                    tempAnswers.add(temp.getAnswerChoices().get(Integer.parseInt(answers.get(i))));
                    pw.print("Question " + (i + 1) + ". ");
                    pw.println(temp.getQuestion());
                    pw.print("Answer " + (i + 1) + ". ");
                    pw.println(temp.getCorrectAnswerIndex());
                    pw.println("Your answer: " + (temp.getAnswerChoices().get(Integer.parseInt(answers.get(i)))));
                    if (temp.getCorrectAnswerIndex() == Integer.parseInt((String) (answers.get(i)))) {
                        pw.println("Points got: " + temp.getPointValue());
                        totalScore += temp.getPointValue();
                    } else {
                        pw.println("Points got: " + 0);
                    }
                }
            }
            pw.println("Total score: " + totalScore);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.answers=tempAnswers;
        student.addSubmission(this);
        quizz.addSubmission(this);
    }

    public String createsNewFile() {
        Random a = new Random();
        String filepath = "Submission" + student.getUsername() + a.nextInt(10000);
        this.filename = filepath;
        return filepath;
    }

    public String readingTheAnswerFromFile(Scanner scanner) {
        String studentAnswer1 = "";
        try {
            System.out.println("Enter the file path:");
            String filepath = scanner.nextLine();
            File f = new File(filepath);
            BufferedReader bfr = new BufferedReader(new FileReader(f));
            String line = bfr.readLine();
            while (line != null) {
                studentAnswer1 = studentAnswer1 + line;
                line = bfr.readLine();
            }
        } catch (IOException e) {
            System.out.println("There was an error accessing your file.");
            return null;
        }
        return studentAnswer1;
    }

    public void view(int submissionNumber) {
        ArrayList<Submission> allThePreviousSubmissions = student.getSubmissions();
        for (int i = 0; i < allThePreviousSubmissions.size(); i++) {
            if (i == submissionNumber) {
                try (BufferedReader bfr = new BufferedReader(new FileReader(allThePreviousSubmissions.get(i)
                        .getFilename())
                )) {
                    String line = bfr.readLine();
                    while (line != null) {
                        System.out.println(line);
                        line = bfr.readLine();
                    }
                    break;
                } catch (IOException e) {
                    System.out.println("There was an error reading from the file.");
                    throw new RuntimeException();
                }
            }
        }
    }
    public String toString(){
        return String.format("This submission %s belonging to %s",quizz.getName(),student.getUsername());
    }

}
