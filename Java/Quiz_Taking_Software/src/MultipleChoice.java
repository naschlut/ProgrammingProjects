import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Multiple Choice Class : This class is in charge of storing a Multiple Choice type question and also has useful
 * functions for the rest of the program.
 *
 * @author Nathan Schlutz
 * @version Nov 15, 2021
 */

public class MultipleChoice extends Question {
    int numChoices;

    public ArrayList<String> getAnswerChoices() {
        return answerChoices;
    }

    public void setNumChoices(int numChoices) {
        this.numChoices = numChoices;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    private ArrayList<String> answerChoices;

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    private int correctAnswerIndex;


    public MultipleChoice(String question, int numChoices, ArrayList<String> answerChoices,
                          int answerIndex, int pointValue) {
        super(question, pointValue);
        this.numChoices = numChoices;
        this.answerChoices = answerChoices;
        this.correctAnswerIndex = answerIndex;
    }


    public void displayQuestion() {
        System.out.println(getQuestion());
        for (int i = 0; i < numChoices; i++) {
            System.out.printf("[%d] %s%n", i + 1, answerChoices.get(i));
        }
    }

    public void editQuestion(Scanner scanner) {
        int selection;
        do {

            do {
                System.out.println("What do you want to change about this question?");
                System.out.println("1: Change what the question is asking.");
                System.out.println("2:Change answer Choices.");
                System.out.println("3:Change correct answer.");
                System.out.println("4: Change point value");
                System.out.println("5.Exit");
                selection = scanner.nextInt();
                scanner.nextLine();
                if (selection > 0 && selection < 6) {
                    break;
                }

            } while (true);
            if (selection == 1) {
                System.out.println("Please enter the updated Question.");
                this.question = scanner.nextLine();
                System.out.println("Quiz question updated!");
            }
            if (selection == 2) {
                this.answerChoices = null;
                this.numChoices = 0;
                System.out.println("Current answer choices have been erased!");
                System.out.println("How many answer choices do you want for this question?");
                int nextInt = scanner.nextInt();
                this.numChoices = nextInt;
                scanner.nextLine();
                this.answerChoices = new ArrayList<String>();
                for (int i = 0; i < nextInt; i++) {
                    System.out.println("Enter you answer choice number #" + (i + 1));
                    answerChoices.set(i, scanner.nextLine());
                }
                System.out.println("What answer choice is correct? (Please insert a Integer)");
                this.correctAnswerIndex = scanner.nextInt();
                scanner.nextLine();
            }
            if (selection == 3) {
                int answerIndex;
                do {
                    for (int i = 0; i < answerChoices.size(); i++) {
                        System.out.printf("%d: %s%n", i, this.answerChoices.get(i));
                    }
                    System.out.println("What do you want the correct answer to be");
                    answerIndex = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("There was a problem editing the current answer choice!");
                } while (answerIndex > answerChoices.size() - 1);
                System.out.println("The correct answer has been updated.");

            }
            if (selection == 4) {
                int points = -1;
                do {
                    System.out.println("What do you want the points for the question to be?");
                    try {
                        points = scanner.nextInt();
                        scanner.nextLine();
                    } catch (NumberFormatException e) {
                        System.out.println("An error has occurred. Please insert an integer.");
                    }
                } while (points < 0);
                this.pointValue = points;
            }
        } while (selection != 5);
    }

    public void setAnswerChoices(ArrayList<String> answerChoices) {
        this.answerChoices = answerChoices;
    }
}

