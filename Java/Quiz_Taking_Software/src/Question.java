import java.io.Serializable;
import java.util.Scanner;
/**
 * Question Class : Used for creating quiz objects
 *
 * @author Nathan S.
 * @version Nov 13, 2021
 */
public class Question implements Serializable {
    String question;
    int pointValue;

    public Question(String question, int pointValue) {
        this.question = question;
        this.pointValue = pointValue;
    }

    public Question() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public void editQuestion(Scanner scanner) {
    }

}
