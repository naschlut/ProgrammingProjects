import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Quiz Class : Handles the making of the quiz by file input/ terminal and also handles editing of the quiz
 *
 * @author Nathan Schultz, Jay Mehta
 * @version Nov 13, 2021
 */
public class Quiz implements Serializable {


    private int numQuestions;
    private ArrayList<Question> quiz = new ArrayList<Question>();
    private String name;
    private Course course;
    private ArrayList<Submission> submissions = new ArrayList<>();
    private boolean randomized;
    private int courseNumber;



    public Quiz(Scanner scanner, Course course) { //create Quiz within the Quiz Class
        creatQuizByInputIndividually(scanner);
        this.course = course;
    }


    public Quiz(String fileName, Scanner scanner, Course course) {
        System.out.println("What do you want to name this quiz?");
        String line = scanner.nextLine();
        this.name = line;
        ArrayList<String> questionList = FileImports.readFile(fileName);
        createQuizFromFile(questionList, scanner);
        this.course = course;
    }
    public Quiz(Course course, File f){
        try(BufferedReader bfr=new BufferedReader(new FileReader(f))){

        } catch (Exception e) {

        }
    }

    public Quiz( Course course, String name, String randomized) {
        this.course = course;
        this.name = name;
        if (randomized.equals("true")) {
            this.randomized = true;
        } else {
            this.randomized = false;
        }
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public boolean isRandomized() {
        return randomized;
    }

    public ArrayList<Submission> getSubmission() {
        return submissions;
    }

    public void addSubmission(Submission s) {
        submissions.add(s);
    }

    public Course getCourse() {
        return course;
    }

    public void createQuizFromFile(ArrayList<String> questionList, Scanner scanner) {
        for (int i = 1; i < questionList.size(); i++) {
            System.out.println("Question: " + questionList.get(i));
            System.out.println("How many points do you want this question to be?");
            int points = Integer.parseInt(scanner.nextLine());
            System.out.println("What type of question is this?");
            System.out.println("Options: True False, Fill in the blank, Multiple Choice");
            String questionType;
            do {
                questionType = scanner.nextLine();
                if (!questionType.equalsIgnoreCase("True False") && !questionType.equalsIgnoreCase(
                        "Fill in the blank") &&
                        !questionType.equalsIgnoreCase("Multiple Choice")) {
                    System.out.println("Please enter a valid type of question");
                    continue;
                }
                break;
            } while (true);
            if (questionType.equalsIgnoreCase("True False")) {
                System.out.println("What is the correct answer?");
                String answer = scanner.nextLine();
                Question q = new TrueFalse(questionList.get(i), answer, points);
                quiz.add(q);

            } else if (questionType.equalsIgnoreCase("Fill in the blank")) {
                System.out.println("What is the correct answer?");
                String answer = scanner.nextLine();
                Question q = new FillInTheBlank(questionList.get(i), answer, points);
                quiz.add(q);
            } else if (questionType.equalsIgnoreCase("Multiple Choice")) {
                System.out.println("Enter the amount of answer choices for this question?");
                int answerChoices;
                do {
                    answerChoices = scanner.nextInt();
                    scanner.nextLine();
                    if (answerChoices < 1) {
                        System.out.println("Invalid input, enter amount of choices for this question again");
                        continue;
                    }
                    break;
                } while (true);
                ArrayList<String> choices = new ArrayList<String>();
                for (int j = 0; j < answerChoices; j++) {
                    System.out.println("Enter the Answer choice #" + (j + 1));
                    choices.add(scanner.nextLine());
                }
                System.out.println("What answer choice is correct?");
                int answerIndex;
                do {
                    answerIndex = (scanner.nextInt());
                    scanner.nextLine();
                    if (answerIndex <= 0 || answerIndex > answerChoices) {
                        System.out.println("Enter the correct answer index");
                        continue;
                    }
                    break;
                } while (true);
                Question q = new MultipleChoice(questionList.get(i), answerChoices, choices, answerIndex, points);
                quiz.add(q);
            }
        }
    }

    public void creatQuizByInputIndividually(Scanner scanner) {
        System.out.println("What do you want to name this quiz?");
        String title = scanner.nextLine();
        this.name = title;
        int questions;
        do {
            System.out.println("How many Questions do you want your Quiz to have?");
            questions = scanner.nextInt();
            scanner.nextLine();
            if (questions < 1) {
                continue;
            }
            break;
        } while (true);
        this.numQuestions = questions;
        do {
            System.out.println("Would you like this quiz to be randomized? (Answer Yes or No)");
            String random = scanner.nextLine();
            if (random.equalsIgnoreCase("N") || random.equalsIgnoreCase("NO")) {
                this.randomized = false;
                break;
            } else if (random.equalsIgnoreCase("Y") || random.equalsIgnoreCase("Yes")) {
                this.randomized = true;
                break;
            } else {
                continue;
            }
        } while (true);
        for (int i = 0; i < questions; i++) {
            //String type, String question, int numChoices, String[]answerChoices, int correctAnswerIndex
            System.out.println("What type of question would you like Question #" + (i + 1) + " to be?");
            System.out.println(("Options: True False, Fill in the blank, Multiple Choice"));
            String questionType;
            do {
                questionType = scanner.nextLine();
                if (!questionType.equalsIgnoreCase("True False") && !questionType.equalsIgnoreCase(
                        "Fill in the blank") && !questionType.equalsIgnoreCase(
                        "Multiple Choice")) {
                    System.out.println("Please enter a valid type of question");
                    continue;
                }
                break;
            } while (true);
            System.out.println("What do you want question " + (i + 1) + " to ask?");
            String question = scanner.nextLine();
            System.out.println("How many points do you want this question to be?");
            int points = scanner.nextInt();
            scanner.nextLine();
            if (questionType.equalsIgnoreCase("True False")) {
                System.out.println("What is the correct answer?");
                String answer = scanner.nextLine();
                Question q = new TrueFalse(question, answer, points);
                quiz.add(q);

            } else if (questionType.equalsIgnoreCase("Fill in the blank")) {
                System.out.println("What is the correct answer?");
                String answer = scanner.nextLine();
                Question q = new FillInTheBlank(question, answer, points);
                quiz.add(q);
            } else if (questionType.equalsIgnoreCase("Multiple Choice")) {
                System.out.println("Enter the amount of answer choices for this question?");
                int answerChoices;
                do {
                    answerChoices = scanner.nextInt();
                    scanner.nextLine();
                    if (answerChoices < 1) {
                        System.out.println("Invalid input, enter amount of choices for this question again");
                        continue;
                    }
                    break;
                } while (true);
                ArrayList<String> choices = new ArrayList<String>();
                for (int j = 0; j < answerChoices; j++) {
                    System.out.println("Enter the Answer choice #" + (j + 1));
                    choices.add(scanner.nextLine());
                }
                System.out.println("What answer choice is correct? (Enter the number of the correct answer)");
                int answerIndex;
                do {
                    answerIndex = (scanner.nextInt());
                    answerIndex -= 1;
                    scanner.nextLine();
                    if (answerIndex < 0 || answerIndex >= answerChoices) {
                        System.out.println("Enter the correct answer index");
                        continue;
                    }
                    break;
                } while (true);
                Question q = new MultipleChoice(question, answerChoices, choices, answerIndex, points);
                quiz.add(q);
            }
        }
        System.out.println("Congrats, you created the quiz titled " + title + ".");
    }

    public String getName() {
        return name;
    }

    public ArrayList<Question> getQuiz() {
        return quiz;
    }

    public void editQuiz(Scanner scanner) {
        boolean done = false;
        int selection = 0;
        System.out.println("What would you like to change in this quiz");
        do {
            do {
                System.out.println("1: Edit Quiz Name");
                System.out.println("2: Edit Quiz Question");
                System.out.println("3. Exit");
                selection = scanner.nextInt();
                scanner.nextLine();
                if (selection == 1 || selection == 2 || selection == 3) {
                    break;
                }
                System.out.println("Please enter a valid number");
            } while (true);
            if (selection == 1) {
                String title;
                System.out.println("Please insert the updated name for the quiz.");
                title = scanner.nextLine();
                this.name = title;
                System.out.println("Quiz name updated!");
            }
            if (selection == 2) {
                while (true) {
                    System.out.println("Select the Question: ");
                    for (int i = 0; i < quiz.size(); i++) {
                        System.out.println(i + ": " + quiz.get(i).getQuestion());
                    }
                    int questionNumber = scanner.nextInt();
                    scanner.nextLine();
                    getQuiz().get(questionNumber).editQuestion(scanner);
                    System.out.println("Do you want to edit another(yes/no)");
                    String choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("yes")) {
                        continue;
                    } else {
                        break;
                    }
                }
            }
            if (selection == 3) {
                done = false;
            }


        } while (done);
    }

    public void randomizeQuestions() {
        ArrayList<Question> temp = quiz;
        ArrayList<Question> temp2 = new ArrayList<>();
        int quizLength = quiz.size();
        while (quizLength > 0) {
            temp2.add(temp.remove((int) (Math.random() * quizLength)));
            quizLength--;

        }
        quiz = temp2;

    }
    public void addQuestion(Question question){
        quiz.add(question);
    }
    public void removeQuestion(Question question){
        quiz.remove(question);
    }
    public Question getQuestion(int i){
        return quiz.get(i);
    }



}




