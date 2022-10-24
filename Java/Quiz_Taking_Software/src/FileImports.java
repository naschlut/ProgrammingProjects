import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * FileImports Class : Handles file imports
 *
 * @author Anh V Nguyen
 * @version Nov 15, 2021
 */

public class FileImports {
    public static String prompt() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the file path to the Quiz!");
        String userAnswer = scanner.nextLine();
        return userAnswer;
    }

    public static ArrayList<String> readFile(String filePath) {
        ArrayList<String> userInput = new ArrayList<>();
        try {
            File quiz = new File(filePath);
            String quizQuestions = "";
            BufferedReader bfr = new BufferedReader(new FileReader(filePath));
            quizQuestions = bfr.readLine();
            do {
                userInput.add(quizQuestions);
                quizQuestions = bfr.readLine();
            } while (quizQuestions != null);
        } catch (IOException e) {
            System.out.println("Please input the right file path!");
        }
        return userInput;
    }
}
