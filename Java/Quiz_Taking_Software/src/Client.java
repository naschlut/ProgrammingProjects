import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Client Class: Handles data between users and the server
 *
 * @author Aditya Menon
 * @version Dec 13, 2021
 */

public class Client {
    Socket socket = null;
    int courseNumber;
    int quizNumber;
    User user;
    int questionNumber;
    int submissionNumber;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    public static void main(String[] args) {
        Client client = new Client();
        View view = new View(client);

    }
    //tested

    public boolean connectToServer(String domainName, String inputPortNumber) {
        try {
            int portNumber = Integer.parseInt(inputPortNumber);
            socket = new Socket(domainName, portNumber);
            if (socket.isConnected()){
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    //looks good

    public boolean createAccount(String username, String password, String typeOfAccount){
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("create-account");
            objects.add(username);
            objects.add(password);
            objects.add(typeOfAccount);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            ArrayList<Object> result = (ArrayList<Object>)ois.readObject();
            String checker = (String)(result.get(0));
            if(checker.equals("success")){
                user = (User)(result.get(1));
                return true;
            }
        } catch (Exception e){
            throw new RuntimeException();
        }
        return false;
    }
    //looks good
    public boolean login(String username, String password, String typeOfAccount){

        try {
            ArrayList<Object> objects = new ArrayList<>();

            objects.add("login");
            objects.add(username);
            objects.add(password);
            objects.add(typeOfAccount);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            ArrayList<Object> result = (ArrayList<Object>)ois.readObject();
            String checker = (String)(result.get(0));
            if(checker.equals("success")){
                user = (User)(result.get(1));
                return true;
            }
        } catch (Exception e){
            throw new RuntimeException();
        }
        return false;

    }
    //looks good
    public ArrayList<Course> getAccountCourses(){
        try{
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("get-account-courses");
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            ArrayList<Course> courseList = (ArrayList<Course>) ois.readObject();
            return courseList;
        } catch(Exception e){
            throw new RuntimeException();
        }
    }
    public ArrayList<Course> getAllCourses() {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("get-all-courses");
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            ArrayList<Course> courseList = (ArrayList<Course>) ois.readObject();
            return courseList;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    //looks good
    public ArrayList<Quiz> getQuizzes() {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("get-quizzes");
            objects.add(courseNumber);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            ArrayList<Quiz> quizList = (ArrayList<Quiz>) (ois.readObject());
            return quizList;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    public Quiz getCurrentQuiz() {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("get-current-quiz");
            objects.add(courseNumber);
            objects.add(quizNumber);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            Quiz currentQuiz = (Quiz) ois.readObject();
            return currentQuiz;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public ArrayList<Question> getQuestions() {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("get-questions");
            objects.add(courseNumber);
            objects.add(quizNumber);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            ArrayList<Question> questionList = (ArrayList<Question>) ois.readObject();
            return questionList;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public ArrayList<Submission> getStudentSubmissions(){
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("get-student-submissions");
            objects.add(courseNumber);
            objects.add(quizNumber);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            ArrayList<Submission> submissionList = (ArrayList<Submission>) ois.readObject();
            return submissionList;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public ArrayList<Submission> getAllSubmissions() {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("get-all-submissions");
            objects.add(courseNumber);
            objects.add(quizNumber);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            ArrayList<Submission> submissionList = (ArrayList<Submission>) ois.readObject();
            return submissionList;
        } catch (Exception e) {
            throw new RuntimeException("client: getAllSubmissions not working");
        }
    }
    //looks good

    public boolean createCourse(String courseName, int courseNumber) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("create-course");
            objects.add(courseName);
            objects.add(courseNumber);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            String checker = (String)(((ArrayList<Object>) ois.readObject()).get(0));
            if(checker.equals("success")){
                this.courseNumber=courseNumber;
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return false;
    }

    public boolean addImportedQuiz(File f) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("create-imported-quiz-file");
            objects.add(courseNumber);
            objects.add(f);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            ArrayList<Object> result = (ArrayList<Object>)ois.readObject();
            String checker = (String) (result.get(0));
            if(checker.equals("success")){
                this.quizNumber = (Integer)(result.get(1));
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return false;
    }

    public void createQuiz(String quizName, boolean randomize) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("create-quiz");
            objects.add(courseNumber);
            objects.add(quizName);
            if(randomize) {
                objects.add("true");
            } else {
                objects.add("false");
            }
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            ArrayList<Object> result = (ArrayList<Object>)ois.readObject();
            int thisQuizNumber = (int)(result.get(0));
            this.quizNumber = thisQuizNumber;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void addStudentToCourse(int courseNumber){
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("add-student-to-course");
            objects.add(courseNumber);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            setActiveCourse(courseNumber);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void deleteQuiz() {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("delete-quiz");
            objects.add(courseNumber);
            objects.add(quizNumber);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            clearActiveQuestion();
            clearActiveQuiz();
        } catch (Exception e){
            throw new RuntimeException();
        }
    }

    public void createTrueFalseQuestion(String questionName, int pointValue, boolean trueOrFalse) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("create-true-false");
            objects.add(courseNumber);
            objects.add(quizNumber);
            objects.add(questionName);
            objects.add(pointValue);
            if (trueOrFalse) {
                objects.add("true");
            } else {
                objects.add("false");
            }
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
        } catch (Exception e) {
            throw new RuntimeException("client: createTrueFalseQuestion not working");
        }
    }

    public void createMultipleChoiceQuestion(String questionName, int pointValue, int numChoices, ArrayList<String>
                                     answerChoices, int correctAnswerIndex) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("create-multiple-choice");
            objects.add(courseNumber);
            objects.add(quizNumber);
            objects.add(questionName);
            objects.add(pointValue);
            objects.add(numChoices);
            objects.add(answerChoices);
            objects.add(correctAnswerIndex);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
        } catch (Exception e) {
            throw new RuntimeException("client: createMultipleChoice not working");
        }
    }

    public void createFillInTheBlankQuestion(String questionName, int pointValue, String answer) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("create-fill-in-the-blank");
            objects.add(courseNumber);
            objects.add(quizNumber);
            objects.add(questionName);
            objects.add(pointValue);
            objects.add(answer);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
        } catch (Exception e) {
            throw new RuntimeException("client: createFillInTheBlank not working");
        }
    }
//    public boolean deleteQuestion() {
//        try {
//
//        }
//    }
//
//        return false;
//    }

//    public void addQuestionToQuiz(Question question) {
//        try {
//            ArrayList<Object> objects = new ArrayList<>();
//            objects.add("add-question-to-quiz");
//            objects.add(question);
//            oos.writeObject(objects);
//            oos.flush();
//        } catch (Exception e) {
//            throw new RuntimeException();
//        }
//    }




    public void updateTrueFalseQuestion(String questionName, int pointValue, boolean trueOrFalse) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("update-true-false");
            objects.add(courseNumber);
            objects.add(quizNumber);
            objects.add(questionNumber);
            objects.add(questionName);
            objects.add(pointValue);
            if (trueOrFalse) {
                objects.add("true");
            } else {
                objects.add("false");
            }
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
        } catch (Exception e) {
            throw new RuntimeException("client: updateTrueFalseQuestion not working");
        }
    }

    public void updateMultipleChoiceQuestion(String questionName, int pointValue, int numChoices, ArrayList<String>
            answerChoices, int correctAnswerIndex) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("update-multiple-choice");
            objects.add(courseNumber);
            objects.add(quizNumber);
            objects.add(questionNumber);
            objects.add(questionName);
            objects.add(pointValue);
            objects.add(numChoices);
            objects.add(answerChoices);
            objects.add(correctAnswerIndex);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
        } catch (Exception e) {
            throw new RuntimeException("client: updateMultipleChoice not working");
        }
    }

    public void updateFillInTheBlankQuestion(String questionName, int pointValue, String answer) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("update-fill-in-the-blank");
            objects.add(courseNumber);
            objects.add(quizNumber);
            objects.add(questionNumber);
            objects.add(questionName);
            objects.add(pointValue);
            objects.add(answer);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
        } catch (Exception e) {
            throw new RuntimeException("client: updateFillInTheBlank not working");
        }
    }

    public void deleteQuestion(int questionNumber) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("delete-question");
            objects.add(courseNumber);
            objects.add(quizNumber);
            objects.add(questionNumber);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            clearActiveQuestion();
        } catch (Exception e) {
            throw new RuntimeException("client: deleteQuestion not working");
        }
    }

    public void submitSubmission(ArrayList<String> answers) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("submit-submission");
            objects.add(courseNumber);
            objects.add(quizNumber);
            objects.add(answers);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            int thisSubmissionNumber=(Integer) ois.readObject();
            setActiveSubmission(thisSubmissionNumber);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void lastQuestionAdded() {
        this.clearActiveQuiz();
        this.clearActiveQuestion();
    }

//
//    public void close() {
//
//    }
//

    public void setActiveQuestion(int questionNumber){
        this.questionNumber = questionNumber;
    }
    public void setActiveSubmission(int submissionNumber){
        this.submissionNumber = submissionNumber;
    }
    public void setActiveCourse(int courseNumber) {
        this.courseNumber = courseNumber;
    }
    public void setActiveQuiz(int quizNumber){
        this.quizNumber = quizNumber;
    }
    public void clearActiveCourse() {
        this.courseNumber = -1;
    }
    public void clearActiveSubmission() {
        this.submissionNumber = -1;
    }
    public void clearActiveQuiz(){
        this.quizNumber = -1;
    }
    public void clearActiveQuestion() {
        this.questionNumber = -1;
    }
    public Question getActiveQuestion() {
        try {
           ArrayList<Object> objects = new ArrayList<>();
           objects.add("get-active-question");
           objects.add(courseNumber);
           objects.add(quizNumber);
           objects.add(questionNumber);
           oos.reset();
           oos.writeObject(objects);
           oos.flush();
           Question question = (Question) ois.readObject();
           return question;
        } catch (Exception e) {
            throw new RuntimeException("client: getActiveQuestion not working");
        }
    }
    public ArrayList<String> getAnswersFromSubmission(String studentName) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("get-answers-from-submission");
            objects.add(courseNumber);
            objects.add(quizNumber);
            objects.add(submissionNumber);
            if(user.isTeacher()){
                objects.add(studentName);
            }
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            ArrayList<String> studentAnswers = (ArrayList<String>) ois.readObject();
            return studentAnswers;
        } catch (Exception e) {
            throw new RuntimeException("client: getAnswersFromSubmission not working");
        }
    }
    public boolean isRandom(){
        try{
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("check-for-random");
            objects.add(courseNumber);
            objects.add(quizNumber);
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
            String isOrNot = (String) ois.readObject();
            return isOrNot.equals("true");
        } catch (Exception e) {
            throw new RuntimeException("client: isRandom not working");
        }
    }
    public void close() {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add("close");
            oos.reset();
            oos.writeObject(objects);
            oos.flush();
        } catch (Exception e) {
            throw new RuntimeException("client: close not working");
        }
    }
    //            OLD ONE

    //    public ArrayList<Quiz> getQuizzes(){
//        ArrayList<Quiz> quizzes = course.getQuizzes();
//        return quizzes;
//    }

    //         OLD ONE
//    public void setActiveQuiz(int quizNumber) {
//        try {
//            ArrayList<Object> objects = new ArrayList<>();
//            objects.add("set-active-quiz");
//            objects.add(quizNumber);
//            oos.writeObject(objects);
//            oos.flush();
//            quiz=(Quiz)ois.readObject();
//        } catch (Exception e) {
//            throw new RuntimeException();
//        }
//    }

    // OLD ONE
//    public void setActiveSubmission(int submissionNumber) {
//        try {
//            ArrayList<Object> objects = new ArrayList<>();
//            objects.add("set-active-submission");
//            objects.add(submissionNumber);
//            oos.writeObject(objects);
//            oos.flush();
//        } catch (Exception e) {
//            throw new RuntimeException();
//        }
//    }

    //     OLD ONE
//    public void setActiveQuestion(int questionNumber) {
//        try {
//            ArrayList<Object> objects = new ArrayList<>();
//            objects.add("set-active-question");
//            objects.add(questionNumber);
//            oos.writeObject(objects);
//            oos.flush();
//        } catch (Exception e) {
//            throw new RuntimeException();
//        }
//    }
}
