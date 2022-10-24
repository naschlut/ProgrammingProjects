import java.util.Scanner;

/**
 * True False Class : This class is in charge of storing a True False  type question and also has useful
 * functions for the rest of the program.
 *
 * @author Nathan Schlutz
 * @version Nov 13, 2021
 */

public class TrueFalse extends Question {
    String answer;

    public TrueFalse(String question, String answer, int pointValue) {
        super(question, pointValue);
        this.answer = answer;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void displayQuestion() {
        System.out.println(this.getQuestion());
        System.out.println("True");
        System.out.println("False");
    }
    public void setAnswer(String answer){
        this.answer = answer;
    }

    public void editQuestion(Scanner scanner) {
        int selection;
        do {
            do {
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
            } while (selection != 1 || selection != 2 || selection != 3);
            if (selection == 1) {
                System.out.println("Please enter the updated Question.");
                this.question = scanner.nextLine();
                System.out.println("Quiz question updated!");
            }
            if (selection == 2) {
                String response;
                do {
                    System.out.println("What do you want the answer to be? (Please enter True or False)");
                    response = scanner.nextLine();
                    if (response.equalsIgnoreCase("True") || response.equals("False")) {
                        break;
                    }
                } while (true);
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
                        if (selection > -1) {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("An error has occurred. Please insert an integer.");
                    }
                } while (points < 0); // positive points including 0;
                this.pointValue = points;
            }
        } while (selection != 4);
    }
}
