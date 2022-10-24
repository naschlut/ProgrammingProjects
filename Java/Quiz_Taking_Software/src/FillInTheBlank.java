import java.io.Serializable;
import java.util.Scanner;

/**
 * Fill InThe Blank Class : This class is in charge of storing a Fill in the blank type question and also has useful
 * functions for the rest of the program.
 *
 * @author Nathan Schlutz
 * @version Nov 13, 2021
 */

public class FillInTheBlank extends Question {
    String answer;

    public FillInTheBlank(String question, String answer, int pointValue) {
        super(question, pointValue);
        this.answer = answer;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public void displayQuestion() {
        System.out.println(question);
    }

    public void editQuestion(Scanner scanner) {
        int selection = 0;
        do {
            do {
                try {
                    System.out.println("What do you want to change?");
                    System.out.println("1: Change what the question is asking.");
                    System.out.println("2: Change the correct answer.");
                    System.out.println("3. Change point value.");
                    System.out.println("4: Exit");
                    selection = scanner.nextInt();
                    scanner.nextLine();
                    if (selection > 0 && selection < 5) {
                        break;
                    }
                    System.out.println("Please enter a valid input!");
                } catch (NumberFormatException e) {
                    continue;
                }
            } while (true);
            if (selection == 1) {
                System.out.println("Please enter the updated Question.");
                this.question = scanner.nextLine();
                System.out.println("Quiz question updated!");
            }
            if (selection == 2) {
                String response;
                System.out.println("What do you want the answer to be?");
                response = scanner.nextLine();
                response.toLowerCase();
                this.answer = response;
            }
            if (selection == 3) {
                int points = -1;
                do {
                    System.out.println("What do you want the points for the question to be?");
                    try {
                        points = scanner.nextInt();
                        scanner.nextLine();
                        if (points > -1) {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("An error has occurred. Please insert an integer.");
                    }
                } while (true); // positive points including 0;
                this.pointValue = points;
            }
        } while (selection != 4);
    }
}
