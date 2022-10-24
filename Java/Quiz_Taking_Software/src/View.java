import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Online Quiz Navigator v2 - View
 *
 * Handles the GUI for the Online Quiz Navigator.
 * Listens for user updates, then sends them to Client for handling.
 * Listens for updates from Client, then refreshes the board as need be.
 *
 * @author Nathan Reed, lab sec L24
 * @version December 8, 2021
 */
public class View {
    int submissionChosenChange=0;
    ArrayList<ArrayList<String>> forActiveTeacherSubmission= new ArrayList<>();
    String studentName="";
    private Client client;
    private JFrame frame;
    private JPanel mainPanel;
    private ArrayList<Object> activeComponents = new ArrayList<Object>();

    private int accountType;
    private static final int STUDENT_OPTION = 0;
    private static final int TEACHER_OPTION = 1;

    private int menuLocation = -1;
    private static final int NO_UPDATE_NEEDED = -1;
    private static final int MAIN_MENU = 0;
    private static final int ADD_COURSE_SCREEN = 1;
    private static final int COURSE_MENU = 2;
    private static final int EDIT_QUIZ_MENU = 3;
    private static final int TAKE_QUIZ_INTRO_SCREEN = 4;
    private static final int STUDENT_SUBMISSION_MENU = 5;
    private static final int TEACHER_SUBMISSION_MENU = 6;

    private boolean running = true;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            if (actionCommand.equals("send connection info")) {
                JTextField domainNameTxt = (JTextField) activeComponents.get(0);
                JTextField portNumberTxt = (JTextField) activeComponents.get(1);
                if (client.connectToServer(domainNameTxt.getText(), portNumberTxt.getText())) {
                    createLoginScreen();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Unable to connect to the server with the given domain name and " +
                            "port number. Please try again.", "Unable to Connect", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("send login info")) {
                JTextField usernameTxt = (JTextField) activeComponents.get(0);
                JTextField passwordTxt = (JTextField) activeComponents.get(1);
                ButtonGroup studentOrTeacher = (ButtonGroup) activeComponents.get(2);
                if (studentOrTeacher.getSelection().getActionCommand().equals("student")) {
                    accountType = STUDENT_OPTION;
                } else if (studentOrTeacher.getSelection().getActionCommand().equals("teacher")) {
                    accountType = TEACHER_OPTION;
                }
                if (client.login(usernameTxt.getText(), passwordTxt.getText(),
                        studentOrTeacher.getSelection().getActionCommand())) {
                    createMainMenu();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Unable to log in to the account with the given username or password. " +
                            "Please try again.", "Unable to login", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("send account creation info")) {
                JTextField usernameTxt = (JTextField) activeComponents.get(0);
                JTextField passwordTxt = (JTextField) activeComponents.get(1);
                ButtonGroup studentOrTeacher = (ButtonGroup) activeComponents.get(2);
                if (studentOrTeacher.getSelection().getActionCommand().equals("student")) {
                    accountType = STUDENT_OPTION;
                } else if (studentOrTeacher.getSelection().getActionCommand().equals("teacher")) {
                    accountType = TEACHER_OPTION;
                }
                if (client.createAccount(usernameTxt.getText(), passwordTxt.getText(),
                        studentOrTeacher.getSelection().getActionCommand())) {
                    createMainMenu();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "The account with the given username already exists. " +
                                    "Please try again.", "Unable to create account", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("choose course")) {
                ButtonGroup courseList = (ButtonGroup) activeComponents.get(0);
                String courseChosen = courseList.getSelection().getActionCommand();
                if (courseChosen.equals("add course")) {
                    if (accountType == STUDENT_OPTION) {
                        createAddCourseScreen();
                    } else if (accountType == TEACHER_OPTION) {
                        createCreateCourseScreen();
                    }
                } else {
                    client.setActiveCourse(Integer.parseInt(courseChosen));
                    createCourseMenu();
                }
            } else if (actionCommand.equals("add student to course")) {
                ButtonGroup courseList = (ButtonGroup) activeComponents.get(0);
                String courseChosen = courseList.getSelection().getActionCommand();
                client.addStudentToCourse(Integer.parseInt(courseChosen));
                createCourseMenu();
            } else if (actionCommand.equals("create course")) {
                JTextField courseNumberTxt = (JTextField) activeComponents.get(0);
                JTextField courseNameTxt = (JTextField) activeComponents.get(1);
                try {
                    int courseNumber = Integer.parseInt(courseNumberTxt.getText());
                    String courseName = courseNameTxt.getText();
                    if (client.createCourse(courseName, courseNumber)) {
                        createCourseMenu();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "A course with the given number already exists. " +
                                        "Please try again.", "Unable to create course", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter an integer for the " +
                            "course number.", "Enter an integer", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("choose quiz")) {
                ButtonGroup quizList = (ButtonGroup) activeComponents.get(0);
                String quizChosen = quizList.getSelection().getActionCommand();
                if (quizChosen.equals("add quiz")) {
                    createCreateQuizIntroScreen();
                } else {
                    client.setActiveQuiz(Integer.parseInt(quizChosen));
                    if (accountType == STUDENT_OPTION) {
                        createStudentQuizOptionsMenu();
                    } else if (accountType == TEACHER_OPTION) {
                        createTeacherQuizOptionsMenu();
                    }
                }
            } else if (actionCommand.equals("choose student option")) {
                ButtonGroup optionsGroup = (ButtonGroup) activeComponents.get(0);
                String optionChosen = optionsGroup.getSelection().getActionCommand();
                if (optionChosen.equals("take quiz")) {
                    createTakeQuizIntroScreen();
                } else if (optionChosen.equals("view previous submissions from student")) {
                    createStudentSubmissionMenu();
                }
            } else if (actionCommand.equals("confirm take quiz")) {
                createActiveQuizScreen();
            } else if (actionCommand.equals("submit quiz")) {
                int confirmSubmit = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to submit? You cannot change your answers after you submit.",
                        "Submit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirmSubmit == JOptionPane.YES_OPTION) {
                    ArrayList<String> answers = new ArrayList<String>();
                    for (int i = 0; i < activeComponents.size(); i++) {
                        Object currentAnswerCollector = activeComponents.get(i);
                        if (currentAnswerCollector instanceof ButtonGroup) {
                            ButtonGroup answerGroup = (ButtonGroup) currentAnswerCollector;
                            ButtonModel buttonSelected = answerGroup.getSelection();
                            if (buttonSelected == null) {
                                answers.add("");
                            } else {
                                answers.add(buttonSelected.getActionCommand());
                            }
                        } else if (currentAnswerCollector instanceof JTextField) {
                            JTextField answerTxt = (JTextField) currentAnswerCollector;
                            answers.add(answerTxt.getText());
                        }
                    }
                    client.submitSubmission(answers);
                    createCourseMenu();
                }
            } else if (actionCommand.equals("view submission")) {
                ButtonGroup submissionsGroup = (ButtonGroup) activeComponents.get(0);
                int submissionChosen = Integer.parseInt(submissionsGroup.getSelection().getActionCommand());
                client.setActiveSubmission(submissionChosen);
                if(accountType==TEACHER_OPTION){
                    submissionChosenChange=Integer.parseInt(forActiveTeacherSubmission.get(submissionChosen).get(1));
                    client.setActiveSubmission(submissionChosenChange);
                }
                try {
                    studentName=forActiveTeacherSubmission.get(submissionChosen).get(0);
                } catch (Exception ignored) {

                }
                createSubmissionViewer();
            } else if (actionCommand.equals("choose teacher option")) {
                ButtonGroup optionsGroup = (ButtonGroup) activeComponents.get(0);
                String optionChosen = optionsGroup.getSelection().getActionCommand();
                if (optionChosen.equals("edit quiz")) {
                    createEditQuizMenu();
                } else if (optionChosen.equals("view all previous submissions")) {
                    createTeacherSubmissionMenu();
                } else if (optionChosen.equals("delete quiz")) {
                    int deleteChoice = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete this quiz?", "Delete quiz?",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (deleteChoice == JOptionPane.YES_OPTION) {
                        client.deleteQuiz();
                        createCourseMenu();
                    }
                }
            } else if (actionCommand.equals("choose quiz input option")) {
                ButtonGroup optionsGroup = (ButtonGroup) activeComponents.get(0);
                String optionChosen = optionsGroup.getSelection().getActionCommand();
                if (optionChosen.equals("import from file")) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Choose Quiz");
                    int selectOrCancel = fileChooser.showOpenDialog(null);
                    if (selectOrCancel == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        if (client.addImportedQuiz(selectedFile)) {
                            JOptionPane.showMessageDialog(null, "Quiz imported successfully!",
                                    "Success!", JOptionPane.INFORMATION_MESSAGE);
                            createCourseMenu();
                        } else {
                            JOptionPane.showMessageDialog(null, "Quiz import unsuccessful. " +
                                    "Please try again.", "Error importing quiz", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else if (optionChosen.equals("enter manually")) {
                    createCreateQuizTitleScreen();
                }
            } else if (actionCommand.equals("create quiz")) {
                JTextField quizNameTxt = (JTextField) activeComponents.get(0);
                ButtonGroup randomizeGroup = (ButtonGroup) activeComponents.get(1);
                String quizName = quizNameTxt.getText();
                String randomizeChoice = randomizeGroup.getSelection().getActionCommand();
                if (randomizeChoice.equals("yes")) {
                    client.createQuiz(quizName, true);
                } else if (randomizeChoice.equals("no")) {
                    client.createQuiz(quizName, false);
                }
                createCreateQuestionScreen();
            } else if (actionCommand.equals("true or false")) {
                createCreateTrueFalseQuestion();
            } else if (actionCommand.equals("add true or false question")) {
                JTextField questionNameTxt = (JTextField) activeComponents.get(2);
                JTextField pointValueTxt = (JTextField) activeComponents.get(3);
                ButtonGroup trueOrFalseGroup = (ButtonGroup) activeComponents.get(4);
                String trueOrFalseChoice = trueOrFalseGroup.getSelection().getActionCommand();
                try {
                    String questionName = questionNameTxt.getText();
                    int pointValue = Integer.parseInt(pointValueTxt.getText());
                    boolean trueOrFalse = trueOrFalseChoice.equals("true");
                    client.createTrueFalseQuestion(questionName, pointValue, trueOrFalse);
                    createCreateQuestionScreen();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter an integer for point " +
                            "value.", "Enter an integer", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("multiple choice")) {
                createSelectNumAnswerChoices();
            } else if (actionCommand.equals("set num answer choices")) {
                try {
                    JTextField numAnswerChoicesTxt = (JTextField) activeComponents.get(4);
                    int numAnswerChoices = Integer.parseInt(numAnswerChoicesTxt.getText());
                    createCreateMultipleChoiceQuestion(numAnswerChoices);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter an integer for number of " +
                            "answer choices.", "Enter an integer", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("add multiple choice question")) {
                JTextField questionNameTxt = (JTextField) activeComponents.get(2);
                JTextField pointValueTxt = (JTextField) activeComponents.get(3);
                JTextField numAnswerChoicesTxt = (JTextField) activeComponents.get(4);
                ButtonGroup correctAnswerIndGroup = (ButtonGroup) activeComponents.get(5);

                ArrayList<String> answerChoices = new ArrayList<String>();
                for (int i = 6; i < activeComponents.size(); i++) {
                    JTextField choiceTxt = (JTextField) activeComponents.get(i);
                    answerChoices.add(choiceTxt.getText());
                }

                try {
                    String questionName = questionNameTxt.getText();
                    int pointValue = Integer.parseInt(pointValueTxt.getText());
                    int numAnswerChoices = Integer.parseInt(numAnswerChoicesTxt.getText());
                    int correctAnswerIndex = Integer.parseInt(correctAnswerIndGroup.getSelection().getActionCommand());
                    client.createMultipleChoiceQuestion(questionName, pointValue, numAnswerChoices, answerChoices,
                            correctAnswerIndex);
                    createCreateQuestionScreen();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter an integer for point " +
                            "value.", "Enter an integer", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("fill in the blank")) {
                createCreateFillInTheBlankQuestion();
            } else if (actionCommand.equals("add fill in the blank question")) {
                JTextField questionNameTxt = (JTextField) activeComponents.get(2);
                JTextField pointValueTxt = (JTextField) activeComponents.get(3);
                JTextField answerTxt = (JTextField) activeComponents.get(4);
                try {
                    String questionName = questionNameTxt.getText();
                    int pointValue = Integer.parseInt(pointValueTxt.getText());
                    String answer = answerTxt.getText();
                    client.createFillInTheBlankQuestion(questionName, pointValue, answer);
                    createCreateQuestionScreen();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter an integer for point " +
                            "value.", "Enter an integer", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("finish with true or false question")) {
                JTextField questionNameTxt = (JTextField) activeComponents.get(2);
                JTextField pointValueTxt = (JTextField) activeComponents.get(3);
                ButtonGroup trueOrFalseGroup = (ButtonGroup) activeComponents.get(4);
                String trueOrFalseChoice = trueOrFalseGroup.getSelection().getActionCommand();
                try {
                    String questionName = questionNameTxt.getText();
                    int pointValue = Integer.parseInt(pointValueTxt.getText());
                    boolean trueOrFalse = trueOrFalseChoice.equals("true");
                    client.createTrueFalseQuestion(questionName, pointValue, trueOrFalse);
                    client.lastQuestionAdded();
                    createCourseMenu();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter an integer for point " +
                            "value.", "Enter an integer", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("finish with multiple choice question")) {
                JTextField questionNameTxt = (JTextField) activeComponents.get(2);
                JTextField pointValueTxt = (JTextField) activeComponents.get(3);
                JTextField numAnswerChoicesTxt = (JTextField) activeComponents.get(4);
                ButtonGroup correctAnswerIndGroup = (ButtonGroup) activeComponents.get(5);

                ArrayList<String> answerChoices = new ArrayList<String>();
                for (int i = 6; i < activeComponents.size(); i++) {
                    JTextField choiceTxt = (JTextField) activeComponents.get(i);
                    answerChoices.add(choiceTxt.getText());
                }

                try {
                    String questionName = questionNameTxt.getText();
                    int pointValue = Integer.parseInt(pointValueTxt.getText());
                    int numAnswerChoices = Integer.parseInt(numAnswerChoicesTxt.getText());
                    int correctAnswerIndex = Integer.parseInt(correctAnswerIndGroup.getSelection().getActionCommand());
                    client.createMultipleChoiceQuestion(questionName, pointValue, numAnswerChoices, answerChoices,
                            correctAnswerIndex);
                    client.lastQuestionAdded();
                    createCourseMenu();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter an integer for point " +
                            "value.", "Enter an integer", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("finish with fill in the blank question")) {
                JTextField questionNameTxt = (JTextField) activeComponents.get(2);
                JTextField pointValueTxt = (JTextField) activeComponents.get(3);
                JTextField answerTxt = (JTextField) activeComponents.get(4);
                try {
                    String questionName = questionNameTxt.getText();
                    int pointValue = Integer.parseInt(pointValueTxt.getText());
                    String answer = answerTxt.getText();
                    client.createFillInTheBlankQuestion(questionName, pointValue, answer);
                    client.lastQuestionAdded();
                    createCourseMenu();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter an integer for point " +
                            "value.", "Enter an integer", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("done with adding questions")) {
                client.lastQuestionAdded(); //this method just tells Client that there are no more questions
                createCourseMenu();
            } else if (actionCommand.equals("edit question")) {
                ButtonGroup questionsGroup = (ButtonGroup) activeComponents.get(0);
                String questionChoice = questionsGroup.getSelection().getActionCommand();
                if (questionChoice.equals("add questions")) {
                    createCreateQuestionScreen();
                } else {
                    int questionNumber = Integer.parseInt(questionChoice);
                    client.setActiveQuestion(questionNumber);
                    createEditQuestionScreen();
                }
            } else if (actionCommand.equals("update true or false question")) {
                JTextField questionNameTxt = (JTextField) activeComponents.get(0);
                JTextField pointValueTxt = (JTextField) activeComponents.get(1);
                ButtonGroup trueOrFalseGroup = (ButtonGroup) activeComponents.get(2);
                String trueOrFalseChoice = trueOrFalseGroup.getSelection().getActionCommand();
                try {
                    String questionName = questionNameTxt.getText();
                    int pointValue = Integer.parseInt(pointValueTxt.getText());
                    boolean trueOrFalse = trueOrFalseChoice.equals("true");
                    client.updateTrueFalseQuestion(questionName, pointValue, trueOrFalse);
                    createEditQuizMenu();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter an integer for point " +
                            "value.", "Enter an integer", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("update multiple choice question")) {
                JTextField questionNameTxt = (JTextField) activeComponents.get(0);
                JTextField pointValueTxt = (JTextField) activeComponents.get(1);
                ButtonGroup correctAnswerIndGroup = (ButtonGroup) activeComponents.get(2);

                ArrayList<String> answerChoices = new ArrayList<String>();
                for (int i = 3; i < activeComponents.size(); i++) {
                    JTextField choiceTxt = (JTextField) activeComponents.get(i);
                    answerChoices.add(choiceTxt.getText());
                }

                try {
                    String questionName = questionNameTxt.getText();
                    int pointValue = Integer.parseInt(pointValueTxt.getText());
                    int numAnswerChoices = answerChoices.size();
                    int correctAnswerIndex = Integer.parseInt(correctAnswerIndGroup.getSelection().getActionCommand());
                    client.updateMultipleChoiceQuestion(questionName, pointValue, numAnswerChoices, answerChoices,
                            correctAnswerIndex);
                    createEditQuizMenu();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter an integer for point " +
                            "value.", "Enter an integer", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("update fill in the blank question")) {
                JTextField questionNameTxt = (JTextField) activeComponents.get(0);
                JTextField pointValueTxt = (JTextField) activeComponents.get(1);
                JTextField answerTxt = (JTextField) activeComponents.get(2);
                try {
                    String questionName = questionNameTxt.getText();
                    int pointValue = Integer.parseInt(pointValueTxt.getText());
                    String answer = answerTxt.getText();
                    client.updateFillInTheBlankQuestion(questionName, pointValue, answer);
                    createEditQuizMenu();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter an integer for point " +
                            "value.", "Enter an integer", JOptionPane.ERROR_MESSAGE);
                }
            } else if (actionCommand.equals("delete question")) {
                ButtonGroup questionsGroup = (ButtonGroup) activeComponents.get(0);
                String questionChoice = questionsGroup.getSelection().getActionCommand();
                if (!questionChoice.equals("add questions")) {
                    int checkDeletion = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete this question?", "Delete question?",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (checkDeletion == JOptionPane.YES_OPTION) {
                        int questionNumber = Integer.parseInt(questionChoice);
                        client.deleteQuestion(questionNumber);
                        createEditQuizMenu();
                    }
                }
            } else if (actionCommand.equals("back to teacher quiz options menu")) {
                createTeacherQuizOptionsMenu();
            } else if (actionCommand.equals("back out of quiz")) {
                int confirmBack = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to go back? All your work will be lost.",
                        "Back?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirmBack == JOptionPane.YES_OPTION) {
                    createStudentQuizOptionsMenu();
                }
            } else if (actionCommand.equals("back to student quiz options menu")) {
                createStudentQuizOptionsMenu();
            } else if (actionCommand.equals("back to submission menu")) {
                client.clearActiveSubmission();
                if (accountType == STUDENT_OPTION) {
                    createStudentSubmissionMenu();
                } else if (accountType == TEACHER_OPTION) {
                    createTeacherSubmissionMenu();
                }
            } else if (actionCommand.equals("back to course menu")) {
                client.clearActiveQuiz();
                createCourseMenu();
            } else if (actionCommand.equals("back to main menu")) {
                client.clearActiveCourse();
                createMainMenu();
            } else if (actionCommand.equals("quit")) {
                int closeConfirmation = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to quit?", "Quit?",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (closeConfirmation == JOptionPane.YES_OPTION) {
                    running = false;
                    client.close();
                    frame.dispose();
                }
            }
        }
    };

    /**
     * Sets up View object and Event Dispatch Thread when called by Client
     * Structure borrowed from Week 11 Wednesday lecture slide 75
     *
     * @param client the Client object used for this session
     */
    public View(Client client) {
        this.client = client;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });
        while (running) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                ;
            }
            if (menuLocation != NO_UPDATE_NEEDED) {
                this.update();
            }
        }
    }

    /**
     * Updates view being displayed to be consistent with new server updates
     */
    public void update() {
        if (menuLocation == MAIN_MENU) {
            createMainMenu();
        } else if (menuLocation == ADD_COURSE_SCREEN) {
            createAddCourseScreen();
        } else if (menuLocation == COURSE_MENU) {
            createCourseMenu();
        } else if (menuLocation == EDIT_QUIZ_MENU) {
            createEditQuizMenu();
        } else if (menuLocation == TAKE_QUIZ_INTRO_SCREEN) {
            createTakeQuizIntroScreen();
        } else if (menuLocation == STUDENT_SUBMISSION_MENU) {
            createStudentSubmissionMenu();
        } else if (menuLocation == TEACHER_SUBMISSION_MENU) {
            createTeacherSubmissionMenu();
        }
    }

    /**
     * Sets up initial components of GUI
     */
    private void createGUI() {
        frame = new JFrame("Online Quiz Navigator v2");
        mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        createConnectionScreen();

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Displays text fields for domain name and port number
     * as well as a submit button
     */
    private void createConnectionScreen() {
        menuLocation = NO_UPDATE_NEEDED;

        JLabel domainNameLabel = new JLabel("Domain Name:");
        JTextField domainNameTxt = new JTextField(30);
        activeComponents.add(domainNameTxt);
        JLabel portNumberLabel = new JLabel("Port Number:");
        JTextField portNumberTxt = new JTextField(30);
        activeComponents.add(portNumberTxt);

        JPanel domainNamePanel = new JPanel(new FlowLayout());
        JPanel portNumberPanel = new JPanel(new FlowLayout());
        domainNamePanel.add(domainNameLabel);
        domainNamePanel.add(domainNameTxt);
        portNumberPanel.add(portNumberLabel);
        portNumberPanel.add(portNumberTxt);

        JButton submitButton = new JButton("Submit");
        submitButton.setActionCommand("send connection info");
        submitButton.addActionListener(actionListener);

        mainPanel.add(domainNamePanel, BorderLayout.NORTH);
        mainPanel.add(portNumberPanel, BorderLayout.CENTER);
        mainPanel.add(submitButton, BorderLayout.SOUTH);
    }

    /**
     * Displays text fields for username and password,
     * a set of radio buttons for selecting a student or teacher account,
     * as well as a login button and a create account button
     */
    private void createLoginScreen() {
        menuLocation = NO_UPDATE_NEEDED;

        mainPanel.removeAll();
        activeComponents.clear();

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameTxt = new JTextField(30);
        activeComponents.add(usernameTxt);
        JLabel passwordLabel = new JLabel("Password:");
        JTextField passwordTxt = new JTextField(30);
        activeComponents.add(passwordTxt);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setActionCommand("send account creation info");
        createAccountButton.addActionListener(actionListener);
        JButton loginButton = new JButton("Login");
        loginButton.setActionCommand("send login info");
        loginButton.addActionListener(actionListener);

        JRadioButton studentButton = new JRadioButton("Student", true);
        studentButton.setActionCommand("student");
        JRadioButton teacherButton = new JRadioButton("Teacher");
        teacherButton.setActionCommand("teacher");
        ButtonGroup teacherOrStudentGroup = new ButtonGroup();
        teacherOrStudentGroup.add(studentButton);
        teacherOrStudentGroup.add(teacherButton);
        activeComponents.add(teacherOrStudentGroup);

        JPanel usernamePanel = new JPanel(new FlowLayout());
        JPanel passwordPanel = new JPanel(new FlowLayout());
        JPanel teacherOrStudentPanel = new JPanel(new FlowLayout());
        JPanel loginPanel = new JPanel(new BorderLayout());
        JPanel submitPanel = new JPanel(new FlowLayout());
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTxt);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordTxt);
        teacherOrStudentPanel.add(studentButton);
        teacherOrStudentPanel.add(teacherButton);
        loginPanel.add(usernamePanel, BorderLayout.NORTH);
        loginPanel.add(passwordPanel, BorderLayout.CENTER);
        loginPanel.add(teacherOrStudentPanel, BorderLayout.SOUTH);
        submitPanel.add(createAccountButton);
        submitPanel.add(loginButton);

        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.add(submitPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays a list of courses the user currently has and an "add course" option,
     * as well as a submission button
     */
    private void createMainMenu() {
        menuLocation = MAIN_MENU;

        mainPanel.removeAll();
        activeComponents.clear();

        ArrayList<Course> courseList = client.getAccountCourses();
        JPanel coursePanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup courseGroup = new ButtonGroup();

        for (Course currentCourse : courseList) {
            int courseNumber = currentCourse.getCourseNumber();
            String courseName = currentCourse.getCourseName();
            String displayCourse = courseNumber + ": " + courseName;

            JRadioButton courseButton = new JRadioButton(displayCourse);
            courseButton.setActionCommand(Integer.toString(courseNumber));
            courseGroup.add(courseButton);
            coursePanel.add(courseButton);
        }

        JRadioButton addCourseButton = new JRadioButton("Add course", true);
        addCourseButton.setActionCommand("add course");
        courseGroup.add(addCourseButton);
        coursePanel.add(addCourseButton);

        activeComponents.add(courseGroup);

        JButton selectButton = new JButton("Select");
        selectButton.setActionCommand("choose course");
        selectButton.addActionListener(actionListener);
        JButton quitButton = new JButton("Save and Quit");
        quitButton.setActionCommand("quit");
        quitButton.addActionListener(actionListener);
        JPanel selectOrQuitPanel = new JPanel(new FlowLayout());
        selectOrQuitPanel.add(selectButton);
        selectOrQuitPanel.add(quitButton);

        JScrollPane scrollPane = new JScrollPane(coursePanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(selectOrQuitPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays all available courses to student, allowing them to add any course.
     */
    private void createAddCourseScreen() {
        menuLocation = ADD_COURSE_SCREEN;

        mainPanel.removeAll();
        activeComponents.clear();

        ArrayList<Course> courseList = client.getAllCourses();
        JPanel coursePanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup courseGroup = new ButtonGroup();

        for (Course currentCourse : courseList) {
            int courseNumber = currentCourse.getCourseNumber();
            String courseName = currentCourse.getCourseName();
            String displayCourse = courseNumber + ": " + courseName;

            JRadioButton courseButton = new JRadioButton(displayCourse);
            courseButton.setActionCommand(Integer.toString(courseNumber));
            courseGroup.add(courseButton);
            coursePanel.add(courseButton);
        }

        activeComponents.add(courseGroup);

        JButton selectButton = new JButton("Select");
        selectButton.setActionCommand("add student to course");
        selectButton.addActionListener(actionListener);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back to main menu");
        backButton.addActionListener(actionListener);
        JPanel selectOrBackPanel = new JPanel(new FlowLayout());
        selectOrBackPanel.add(selectButton);
        selectOrBackPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(coursePanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(selectOrBackPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays text fields for course creation to teacher, allowing them to create a course.
     */
    private void createCreateCourseScreen() {
        menuLocation = NO_UPDATE_NEEDED;

        mainPanel.removeAll();
        activeComponents.clear();

        JLabel courseNumberLabel = new JLabel("Course Number:");
        JTextField courseNumberTxt = new JTextField(30);
        activeComponents.add(courseNumberTxt);
        JLabel courseNameLabel = new JLabel("Course Name:");
        JTextField courseNameTxt = new JTextField(30);
        activeComponents.add(courseNameTxt);

        JButton createCourseButton = new JButton("Create Course");
        createCourseButton.setActionCommand("create course");
        createCourseButton.addActionListener(actionListener);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back to main menu");
        backButton.addActionListener(actionListener);

        JPanel courseNumberPanel = new JPanel(new FlowLayout());
        JPanel courseNamePanel = new JPanel(new FlowLayout());
        JPanel createOrBackPanel = new JPanel(new FlowLayout());
        courseNumberPanel.add(courseNumberLabel);
        courseNumberPanel.add(courseNumberTxt);
        courseNamePanel.add(courseNameLabel);
        courseNamePanel.add(courseNameTxt);
        createOrBackPanel.add(createCourseButton);
        createOrBackPanel.add(backButton);
        mainPanel.add(courseNumberPanel, BorderLayout.NORTH);
        mainPanel.add(courseNamePanel, BorderLayout.CENTER);
        mainPanel.add(createOrBackPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays the menu for a particular course, allowing the user to select which quiz they want to access, as well as
     * create a new quiz if the user is a teacher.
     */
    private void createCourseMenu() {
        menuLocation = COURSE_MENU;

        mainPanel.removeAll();
        activeComponents.clear();

        ArrayList<Quiz> quizList = client.getQuizzes();
        JPanel quizPanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup quizGroup = new ButtonGroup();

        for (int i = 0; i < quizList.size(); i++) {
            String currentQuizName = quizList.get(i).getName();
            JRadioButton currentQuizButton = new JRadioButton(currentQuizName);
            currentQuizButton.setActionCommand(Integer.toString(i));
            quizGroup.add(currentQuizButton);
            quizPanel.add(currentQuizButton);
        }

        if (accountType == TEACHER_OPTION) {
            JRadioButton addQuizButton = new JRadioButton("Add quiz", true);
            addQuizButton.setActionCommand("add quiz");
            quizGroup.add(addQuizButton);
            quizPanel.add(addQuizButton);
        }

        activeComponents.add(quizGroup);

        JButton selectButton = new JButton("Select");
        selectButton.setActionCommand("choose quiz");
        selectButton.addActionListener(actionListener);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back to main menu");
        backButton.addActionListener(actionListener);
        JPanel selectOrBackPanel = new JPanel(new FlowLayout());
        selectOrBackPanel.add(selectButton);
        selectOrBackPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(quizPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(selectOrBackPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays options for a particular quiz (take quiz or view submissions) to a student
     */
    private void createStudentQuizOptionsMenu() {
        menuLocation = NO_UPDATE_NEEDED;

        mainPanel.removeAll();
        activeComponents.clear();

        JRadioButton takeButton = new JRadioButton("Take quiz");
        takeButton.setActionCommand("take quiz");
        JRadioButton viewButton = new JRadioButton("View previous submissions");
        viewButton.setActionCommand("view previous submissions from student");
        ButtonGroup optionsGroup = new ButtonGroup();
        optionsGroup.add(takeButton);
        optionsGroup.add(viewButton);
        activeComponents.add(optionsGroup);
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1));
        optionsPanel.add(takeButton);
        optionsPanel.add(viewButton);

        JButton selectButton = new JButton("Select");
        selectButton.setActionCommand("choose student option");
        selectButton.addActionListener(actionListener);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back to course menu");
        backButton.addActionListener(actionListener);
        JPanel selectOrBackPanel = new JPanel(new FlowLayout());
        selectOrBackPanel.add(selectButton);
        selectOrBackPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(optionsPanel);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(selectOrBackPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays options for a particular quiz (edit quiz, view submissions, or delete quiz) to teacher
     */
    private void createTeacherQuizOptionsMenu() {
        menuLocation = NO_UPDATE_NEEDED;

        mainPanel.removeAll();
        activeComponents.clear();

        JRadioButton editButton = new JRadioButton("Edit quiz");
        editButton.setActionCommand("edit quiz");
        JRadioButton viewButton = new JRadioButton("View submissions");
        viewButton.setActionCommand("view all previous submissions");
        JRadioButton deleteButton = new JRadioButton("Delete quiz");
        deleteButton.setActionCommand("delete quiz");
        ButtonGroup optionsGroup = new ButtonGroup();
        optionsGroup.add(editButton);
        optionsGroup.add(viewButton);
        optionsGroup.add(deleteButton);
        activeComponents.add(optionsGroup);
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1));
        optionsPanel.add(editButton);
        optionsPanel.add(viewButton);
        optionsPanel.add(deleteButton);

        JButton selectButton = new JButton("Select");
        selectButton.setActionCommand("choose teacher option");
        selectButton.addActionListener(actionListener);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back to course menu");
        backButton.addActionListener(actionListener);
        JPanel selectOrBackPanel = new JPanel(new FlowLayout());
        selectOrBackPanel.add(selectButton);
        selectOrBackPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(optionsPanel);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(selectOrBackPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays choice of quiz input method to teacher
     */
    private void createCreateQuizIntroScreen() {
        menuLocation = NO_UPDATE_NEEDED;

        mainPanel.removeAll();
        activeComponents.clear();

        JLabel creationMethodLabel = new JLabel("Would you like to import the quiz as a file, or " +
                "enter it manually?");

        JRadioButton importButton = new JRadioButton("Import as file");
        importButton.setActionCommand("import from file");
        JRadioButton enterButton = new JRadioButton("Enter manually");
        enterButton.setActionCommand("enter manually");
        ButtonGroup optionsGroup = new ButtonGroup();
        optionsGroup.add(importButton);
        optionsGroup.add(enterButton);
        activeComponents.add(optionsGroup);
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1));
        optionsPanel.add(importButton);
        optionsPanel.add(enterButton);

        JButton selectButton = new JButton("Select");
        selectButton.setActionCommand("choose quiz input option");
        selectButton.addActionListener(actionListener);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back to course menu");
        backButton.addActionListener(actionListener);
        JPanel selectOrBackPanel = new JPanel(new FlowLayout());
        selectOrBackPanel.add(selectButton);
        selectOrBackPanel.add(backButton);

        mainPanel.add(creationMethodLabel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(optionsPanel);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(selectOrBackPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays screen to set quiz title and randomization
     */
    private void createCreateQuizTitleScreen() {
        menuLocation = NO_UPDATE_NEEDED;

        mainPanel.removeAll();
        activeComponents.clear();

        JLabel quizNameLabel = new JLabel("Quiz Name:");
        JTextField quizNameTxt = new JTextField(30);
        activeComponents.add(quizNameTxt);
        JPanel quizNamePanel = new JPanel(new FlowLayout());
        quizNamePanel.add(quizNameLabel);
        quizNamePanel.add(quizNameTxt);

        JLabel randomizeLabel = new JLabel("Randomize?");
        JRadioButton yesButton = new JRadioButton("Yes");
        yesButton.setActionCommand("yes");
        JRadioButton noButton = new JRadioButton("No");
        noButton.setActionCommand("no");
        ButtonGroup randomizeGroup = new ButtonGroup();
        randomizeGroup.add(yesButton);
        randomizeGroup.add(noButton);
        activeComponents.add(randomizeGroup);
        JPanel randomizePanel = new JPanel(new FlowLayout());
        randomizePanel.add(randomizeLabel);
        randomizePanel.add(yesButton);
        randomizePanel.add(noButton);

        JButton createQuizButton = new JButton("Create Quiz");
        createQuizButton.setActionCommand("create quiz");
        createQuizButton.addActionListener(actionListener);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back to teacher quiz options menu");
        backButton.addActionListener(actionListener);
        JPanel createOrBackPanel = new JPanel(new FlowLayout());
        createOrBackPanel.add(createQuizButton);
        createOrBackPanel.add(backButton);

        mainPanel.add(quizNamePanel, BorderLayout.NORTH);
        mainPanel.add(randomizePanel, BorderLayout.CENTER);
        mainPanel.add(createOrBackPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays generic fields for question creation and allows the user to select whether the question should be
     * true or false, multiple choice, or fill in the blank
     */
    private void createCreateQuestionScreen() {
        menuLocation = NO_UPDATE_NEEDED;

        mainPanel.removeAll();
        activeComponents.clear();

        JPanel preSpecificPanel = new JPanel(new BorderLayout());
        activeComponents.add(preSpecificPanel);
        JPanel addDoneFinishPanel = new JPanel(new FlowLayout());
        activeComponents.add(addDoneFinishPanel);

        JLabel questionNameLabel = new JLabel("Question:");
        JTextField questionNameTxt = new JTextField(30);
        activeComponents.add(questionNameTxt);
        JPanel questionNamePanel = new JPanel(new FlowLayout());
        questionNamePanel.add(questionNameLabel);
        questionNamePanel.add(questionNameTxt);
        JLabel pointValueLabel = new JLabel("Point Value:");
        JTextField pointValueTxt = new JTextField(5);
        activeComponents.add(pointValueTxt);
        JPanel pointValuePanel = new JPanel(new FlowLayout());
        pointValuePanel.add(pointValueLabel);
        pointValuePanel.add(pointValueTxt);
        JPanel genericFieldsPanel = new JPanel(new BorderLayout());
        genericFieldsPanel.add(questionNamePanel, BorderLayout.NORTH);
        genericFieldsPanel.add(pointValuePanel, BorderLayout.CENTER);
        preSpecificPanel.add(genericFieldsPanel, BorderLayout.NORTH);

        JLabel questionTypeLabel = new JLabel("Question Type:");
        JRadioButton trueOrFalseButton = new JRadioButton("True or false");
        trueOrFalseButton.setActionCommand("true or false");
        trueOrFalseButton.addActionListener(actionListener);
        JRadioButton multipleChoiceButton = new JRadioButton("Multiple choice");
        multipleChoiceButton.setActionCommand("multiple choice");
        multipleChoiceButton.addActionListener(actionListener);
        JRadioButton fillInTheBlankButton = new JRadioButton("Fill in the blank");
        fillInTheBlankButton.setActionCommand("fill in the blank");
        fillInTheBlankButton.addActionListener(actionListener);
        ButtonGroup questionTypeGroup = new ButtonGroup();
        questionTypeGroup.add(trueOrFalseButton);
        questionTypeGroup.add(multipleChoiceButton);
        questionTypeGroup.add(fillInTheBlankButton);
        JPanel questionTypePanel = new JPanel(new FlowLayout());
        questionTypePanel.add(questionTypeLabel);
        questionTypePanel.add(trueOrFalseButton);
        questionTypePanel.add(multipleChoiceButton);
        questionTypePanel.add(fillInTheBlankButton);
        preSpecificPanel.add(questionTypePanel, BorderLayout.CENTER);

        JButton doneButton = new JButton("Done");
        doneButton.setActionCommand("done with adding questions");
        doneButton.addActionListener(actionListener);
        addDoneFinishPanel.add(doneButton);

        mainPanel.add(preSpecificPanel, BorderLayout.NORTH);
        mainPanel.add(addDoneFinishPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays possible answer options for true or false questions, allows teacher to choose which should be correct
     */
    private void createCreateTrueFalseQuestion() {
        JPanel preSpecificPanel = (JPanel) activeComponents.get(0);
        if (activeComponents.size() > 4) {
            for (int i = 4; i < activeComponents.size(); i++) {
                activeComponents.remove(i);
            }

            BorderLayout mainPanelLayout = (BorderLayout) mainPanel.getLayout();
            mainPanel.remove(mainPanelLayout.getLayoutComponent(BorderLayout.CENTER));

            BorderLayout preSpecificLayout = (BorderLayout) preSpecificPanel.getLayout();
            preSpecificPanel.remove(preSpecificLayout.getLayoutComponent(BorderLayout.SOUTH));
        }

        JPanel addDoneFinishPanel = (JPanel) activeComponents.get(1);
        addDoneFinishPanel.removeAll();
        JButton addButton = new JButton("Add another question");
        addButton.setActionCommand("add true or false question");
        addButton.addActionListener(actionListener);
        JButton finishButton = new JButton("Finish");
        finishButton.setActionCommand("finish with true or false question");
        finishButton.addActionListener(actionListener);
        addDoneFinishPanel.add(addButton);
        addDoneFinishPanel.add(finishButton);

        JLabel trueOrFalseLabel = new JLabel("Is the correct answer 'true' or 'false'?");
        JRadioButton trueButton = new JRadioButton("True");
        trueButton.setActionCommand("true");
        JRadioButton falseButton = new JRadioButton("False");
        falseButton.setActionCommand("false");
        ButtonGroup trueOrFalseGroup = new ButtonGroup();
        trueOrFalseGroup.add(trueButton);
        trueOrFalseGroup.add(falseButton);
        activeComponents.add(trueOrFalseGroup);
        JPanel trueOrFalsePanel = new JPanel(new BorderLayout());
        trueOrFalsePanel.add(trueOrFalseLabel, BorderLayout.NORTH);
        trueOrFalsePanel.add(trueButton, BorderLayout.CENTER);
        trueOrFalsePanel.add(falseButton, BorderLayout.SOUTH);

        mainPanel.add(trueOrFalsePanel, BorderLayout.CENTER);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays a field that allows the teacher to set the number of answer choices
     */
    private void createSelectNumAnswerChoices() {
        JPanel preSpecificPanel = (JPanel) activeComponents.get(0);
        if (activeComponents.size() > 4) {
            for (int i = 4; i < activeComponents.size(); i++) {
                activeComponents.remove(i);
            }

            BorderLayout mainPanelLayout = (BorderLayout) mainPanel.getLayout();
            mainPanel.remove(mainPanelLayout.getLayoutComponent(BorderLayout.CENTER));

            BorderLayout preSpecificLayout = (BorderLayout) preSpecificPanel.getLayout();
            preSpecificPanel.remove(preSpecificLayout.getLayoutComponent(BorderLayout.SOUTH));
        }

        JPanel addDoneFinishPanel = (JPanel) activeComponents.get(1);
        if (addDoneFinishPanel.getComponentCount() != 1) {
            addDoneFinishPanel.removeAll();

            JButton doneButton = new JButton("Done");
            doneButton.setActionCommand("done with adding questions");
            doneButton.addActionListener(actionListener);
            addDoneFinishPanel.add(doneButton);
        }

        JLabel numAnswerChoicesLabel = new JLabel("Number of answer choices");
        JTextField numAnswerChoicesTxt = new JTextField(5);
        activeComponents.add(numAnswerChoicesTxt);
        JButton numAnswerChoicesButton = new JButton("Set");
        numAnswerChoicesButton.setActionCommand("set num answer choices");
        numAnswerChoicesButton.addActionListener(actionListener);
        JPanel numAnswerChoicesPanel = new JPanel(new FlowLayout());
        numAnswerChoicesPanel.add(numAnswerChoicesLabel);
        numAnswerChoicesPanel.add(numAnswerChoicesTxt);
        numAnswerChoicesPanel.add(numAnswerChoicesButton);

        preSpecificPanel.add(numAnswerChoicesPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays text fields for teacher to specify answer choices
     *
     * @param numAnswerChoices the number of answer choices to display
     */
    private void createCreateMultipleChoiceQuestion(int numAnswerChoices) {
        JPanel preSpecificPanel = (JPanel) activeComponents.get(0);
        if (activeComponents.size() > 5) {
            for (int i = 5; i < activeComponents.size(); i++) {
                activeComponents.remove(i);
            }

            BorderLayout mainPanelLayout = (BorderLayout) mainPanel.getLayout();
            mainPanel.remove(mainPanelLayout.getLayoutComponent(BorderLayout.CENTER));
        }

        JPanel addDoneFinishPanel = (JPanel) activeComponents.get(1);
        addDoneFinishPanel.removeAll();
        JButton addButton = new JButton("Add another question");
        addButton.setActionCommand("add multiple choice question");
        addButton.addActionListener(actionListener);
        JButton finishButton = new JButton("Finish");
        finishButton.setActionCommand("finish with multiple choice question");
        finishButton.addActionListener(actionListener);
        addDoneFinishPanel.add(addButton);
        addDoneFinishPanel.add(finishButton);

        JPanel answerChoicesPanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup answerChoicesGroup = new ButtonGroup();
        activeComponents.add(answerChoicesGroup);

        for (int i = 0; i < numAnswerChoices; i++) {
            JRadioButton choiceButton = new JRadioButton("Choice " + (i + 1) + ":");
            choiceButton.setActionCommand(Integer.toString(i));
            answerChoicesGroup.add(choiceButton);
            JTextField choiceTxt = new JTextField(30);
            activeComponents.add(choiceTxt);
            JPanel choicePanel = new JPanel(new FlowLayout());
            choicePanel.add(choiceButton);
            choicePanel.add(choiceTxt);
            answerChoicesPanel.add(choicePanel);
        }

        JScrollPane scrollPane = new JScrollPane(answerChoicesPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays text field for teacher to specify answer to fill in the blank question
     */
    private void createCreateFillInTheBlankQuestion() {
        JPanel preSpecificPanel = (JPanel) activeComponents.get(0);
        if (activeComponents.size() > 4) {
            for (int i = 4; i < activeComponents.size(); i++) {
                activeComponents.remove(i);
            }

            BorderLayout mainPanelLayout = (BorderLayout) mainPanel.getLayout();
            mainPanel.remove(mainPanelLayout.getLayoutComponent(BorderLayout.CENTER));

            BorderLayout preSpecificLayout = (BorderLayout) preSpecificPanel.getLayout();
            preSpecificPanel.remove(preSpecificLayout.getLayoutComponent(BorderLayout.SOUTH));
        }

        JPanel addDoneFinishPanel = (JPanel) activeComponents.get(1);
        addDoneFinishPanel.removeAll();
        JButton addButton = new JButton("Add another question");
        addButton.setActionCommand("add fill in the blank question");
        addButton.addActionListener(actionListener);
        JButton finishButton = new JButton("Finish");
        finishButton.setActionCommand("finish with fill in the blank question");
        finishButton.addActionListener(actionListener);
        addDoneFinishPanel.add(addButton);
        addDoneFinishPanel.add(finishButton);

        JLabel answerLabel = new JLabel("Answer:");
        JTextField answerTxt = new JTextField(30);
        activeComponents.add(answerTxt);
        JPanel answerPanel = new JPanel(new FlowLayout());
        answerPanel.add(answerLabel);
        answerPanel.add(answerTxt);

        mainPanel.add(answerPanel, BorderLayout.CENTER);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays a list of questions that the teacher can choose to edit
     */
    private void createEditQuizMenu() {
        menuLocation = EDIT_QUIZ_MENU;

        mainPanel.removeAll();
        activeComponents.clear();

        ArrayList<Question> questions = client.getQuestions();
        JPanel questionsPanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup questionsGroup = new ButtonGroup();

        for (int i = 0; i < questions.size(); i++) {
            String questionName = questions.get(i).getQuestion();
            JRadioButton questionButton = new JRadioButton(questionName);
            questionButton.setActionCommand(Integer.toString(i));
            questionsGroup.add(questionButton);
            questionsPanel.add(questionButton);
        }

        JRadioButton addQuestionsButton = new JRadioButton("Add questions");
        addQuestionsButton.setActionCommand("add questions");
        questionsGroup.add(addQuestionsButton);
        questionsPanel.add(addQuestionsButton);

        activeComponents.add(questionsGroup);

        JButton editButton = new JButton("Edit");
        editButton.setActionCommand("edit question");
        editButton.addActionListener(actionListener);
        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand("delete question");
        deleteButton.addActionListener(actionListener);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back to teacher quiz options menu");
        backButton.addActionListener(actionListener);
        JPanel editDeleteOrBackPanel = new JPanel(new FlowLayout());
        editDeleteOrBackPanel.add(editButton);
        editDeleteOrBackPanel.add(deleteButton);
        editDeleteOrBackPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(editDeleteOrBackPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Determines type of question to be edited, then calls relevant edit screen creation method
     */
    private void createEditQuestionScreen() {
        menuLocation = NO_UPDATE_NEEDED;

        mainPanel.removeAll();
        activeComponents.clear();

        Question question = client.getActiveQuestion();
        if (question instanceof TrueFalse) {
            TrueFalse trueFalse = (TrueFalse) question;
            createEditTrueFalseScreen(trueFalse);
        } else if (question instanceof MultipleChoice) {
            MultipleChoice multipleChoice = (MultipleChoice) question;
            createEditMultipleChoiceScreen(multipleChoice);
        } else if (question instanceof FillInTheBlank) {
            FillInTheBlank fillInTheBlank = (FillInTheBlank) question;
            createEditFillInTheBlankScreen(fillInTheBlank);
        }

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays fields of true or false question for editing
     *
     * @param trueFalse the question to be displayed
     */
    private void createEditTrueFalseScreen(TrueFalse trueFalse) {
        JLabel questionNameLabel = new JLabel("Question:");
        JTextField questionNameTxt = new JTextField(30);
        questionNameTxt.setText(trueFalse.getQuestion());
        activeComponents.add(questionNameTxt);
        JPanel questionNamePanel = new JPanel(new FlowLayout());
        questionNamePanel.add(questionNameLabel);
        questionNamePanel.add(questionNameTxt);

        JLabel pointValueLabel = new JLabel("Point Value:");
        JTextField pointValueTxt = new JTextField(5);
        pointValueTxt.setText(Integer.toString(trueFalse.getPointValue()));
        activeComponents.add(pointValueTxt);
        JPanel pointValuePanel = new JPanel(new FlowLayout());
        pointValuePanel.add(pointValueLabel);
        pointValuePanel.add(pointValueTxt);

        JPanel genericFieldsPanel = new JPanel(new BorderLayout());
        genericFieldsPanel.add(questionNamePanel, BorderLayout.NORTH);
        genericFieldsPanel.add(pointValuePanel, BorderLayout.CENTER);

        JLabel trueOrFalseLabel = new JLabel("Is the correct answer 'true' or 'false'?");
        JRadioButton trueButton;
        JRadioButton falseButton;
        boolean answer = trueFalse.getAnswer().equalsIgnoreCase("true");
        if (answer) {
            trueButton = new JRadioButton("True", true);
            falseButton = new JRadioButton("False");
        } else {
            trueButton = new JRadioButton("True");
            falseButton = new JRadioButton("False", true);
        }
        trueButton.setActionCommand("true");
        falseButton.setActionCommand("false");
        ButtonGroup trueOrFalseGroup = new ButtonGroup();
        trueOrFalseGroup.add(trueButton);
        trueOrFalseGroup.add(falseButton);
        activeComponents.add(trueOrFalseGroup);
        JPanel trueOrFalsePanel = new JPanel(new BorderLayout());
        trueOrFalsePanel.add(trueOrFalseLabel, BorderLayout.NORTH);
        trueOrFalsePanel.add(trueButton, BorderLayout.CENTER);
        trueOrFalsePanel.add(falseButton, BorderLayout.SOUTH);

        JButton updateButton = new JButton("Update");
        updateButton.setActionCommand("update true or false question");
        updateButton.addActionListener(actionListener);

        mainPanel.add(genericFieldsPanel, BorderLayout.NORTH);
        mainPanel.add(trueOrFalsePanel, BorderLayout.CENTER);
        mainPanel.add(updateButton, BorderLayout.SOUTH);
    }

    /**
     * Displays fields of multiple choice question for editing
     *
     * @param multipleChoice the question to be displayed
     */
    private void createEditMultipleChoiceScreen(MultipleChoice multipleChoice) {
        JLabel questionNameLabel = new JLabel("Question:");
        JTextField questionNameTxt = new JTextField(30);
        questionNameTxt.setText(multipleChoice.getQuestion());
        activeComponents.add(questionNameTxt);
        JPanel questionNamePanel = new JPanel(new FlowLayout());
        questionNamePanel.add(questionNameLabel);
        questionNamePanel.add(questionNameTxt);

        JLabel pointValueLabel = new JLabel("Point Value:");
        JTextField pointValueTxt = new JTextField(5);
        pointValueTxt.setText(Integer.toString(multipleChoice.getPointValue()));
        activeComponents.add(pointValueTxt);
        JPanel pointValuePanel = new JPanel(new FlowLayout());
        pointValuePanel.add(pointValueLabel);
        pointValuePanel.add(pointValueTxt);

        JPanel genericFieldsPanel = new JPanel(new BorderLayout());
        genericFieldsPanel.add(questionNamePanel, BorderLayout.NORTH);
        genericFieldsPanel.add(pointValuePanel, BorderLayout.CENTER);

        ArrayList<String> answerChoices = multipleChoice.getAnswerChoices();
        ButtonGroup answerChoicesGroup = new ButtonGroup();
        activeComponents.add(answerChoicesGroup);
        JPanel answerChoicesPanel = new JPanel(new GridLayout(0, 1));

        for (int i = 0; i < answerChoices.size(); i++) {
            JRadioButton choiceButton;
            if (multipleChoice.getCorrectAnswerIndex() == i) {
                choiceButton = new JRadioButton("Choice " + (i + 1) + ":", true);
            } else {
                choiceButton = new JRadioButton("Choice " + (i + 1) + ":", false);
            }
            choiceButton.setActionCommand(Integer.toString(i));
            answerChoicesGroup.add(choiceButton);
            JTextField choiceTxt = new JTextField(30);
            choiceTxt.setText(answerChoices.get(i));
            activeComponents.add(choiceTxt);
            JPanel choicePanel = new JPanel(new FlowLayout());
            choicePanel.add(choiceButton);
            choicePanel.add(choiceTxt);
            answerChoicesPanel.add(choicePanel);
        }

        JButton updateButton = new JButton("Update");
        updateButton.setActionCommand("update multiple choice question");
        updateButton.addActionListener(actionListener);

        mainPanel.add(genericFieldsPanel, BorderLayout.NORTH);
        mainPanel.add(answerChoicesPanel, BorderLayout.CENTER);
        mainPanel.add(updateButton, BorderLayout.SOUTH);
    }

    /**
     * Displays fields of fill in the blank question for editing
     *
     * @param fillInTheBlank the question to be displayed
     */
    private void createEditFillInTheBlankScreen(FillInTheBlank fillInTheBlank) {
        JLabel questionNameLabel = new JLabel("Question:");
        JTextField questionNameTxt = new JTextField(30);
        questionNameTxt.setText(fillInTheBlank.getQuestion());
        activeComponents.add(questionNameTxt);
        JPanel questionNamePanel = new JPanel(new FlowLayout());
        questionNamePanel.add(questionNameLabel);
        questionNamePanel.add(questionNameTxt);

        JLabel pointValueLabel = new JLabel("Point Value:");
        JTextField pointValueTxt = new JTextField(5);
        pointValueTxt.setText(Integer.toString(fillInTheBlank.getPointValue()));
        activeComponents.add(pointValueTxt);
        JPanel pointValuePanel = new JPanel(new FlowLayout());
        pointValuePanel.add(pointValueLabel);
        pointValuePanel.add(pointValueTxt);

        JPanel genericFieldsPanel = new JPanel(new BorderLayout());
        genericFieldsPanel.add(questionNamePanel, BorderLayout.NORTH);
        genericFieldsPanel.add(pointValuePanel, BorderLayout.CENTER);

        JLabel answerLabel = new JLabel("Answer:");
        JTextField answerTxt = new JTextField(30);
        answerTxt.setText(fillInTheBlank.getAnswer());
        activeComponents.add(answerTxt);
        JPanel answerPanel = new JPanel(new FlowLayout());
        answerPanel.add(answerLabel);
        answerPanel.add(answerTxt);

        JButton updateButton = new JButton("Update");
        updateButton.setActionCommand("update fill in the blank question");
        updateButton.addActionListener(actionListener);

        mainPanel.add(genericFieldsPanel, BorderLayout.NORTH);
        mainPanel.add(answerPanel, BorderLayout.CENTER);
        mainPanel.add(updateButton, BorderLayout.SOUTH);
    }

    /**
     * Displays a confirmation message to make sure the student is taking the right quiz
     */
    private void createTakeQuizIntroScreen() {
        menuLocation = TAKE_QUIZ_INTRO_SCREEN;

        mainPanel.removeAll();
        activeComponents.clear();

        Quiz quiz = client.getCurrentQuiz();
        String quizName = quiz.getName();
        int numQuestions = quiz.getQuiz().size();

        JLabel takeLabel = new JLabel("You are about to take the quiz " + quizName +
                " with " + numQuestions + " questions. Are you sure you want to proceed?");

        JButton takeButton = new JButton("Take");
        takeButton.setActionCommand("confirm take quiz");
        takeButton.addActionListener(actionListener);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back to student quiz options menu");
        backButton.addActionListener(actionListener);
        JPanel takeOrBackPanel = new JPanel(new FlowLayout());
        takeOrBackPanel.add(takeButton);
        takeOrBackPanel.add(backButton);

        mainPanel.add(takeLabel, BorderLayout.CENTER);
        mainPanel.add(takeOrBackPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays quiz to student
     */
    private void createActiveQuizScreen() {
        menuLocation = NO_UPDATE_NEEDED;

        mainPanel.removeAll();
        activeComponents.clear();

        ArrayList<Question> questions = client.getQuestions();
        if(client.isRandom()){
            Collections.shuffle(questions, new Random(1));
        }
        JPanel questionsPanel = new JPanel(new GridLayout(0, 1));
        for (int i = 0; i < questions.size(); i++) {
            Question currentQuestion = questions.get(i);
            JPanel currentQuestionPanel = null;
            if (currentQuestion instanceof TrueFalse) {
                TrueFalse trueFalse = (TrueFalse) currentQuestion;
                currentQuestionPanel = assembleTrueFalseQuestion(trueFalse, i+1);
            } else if (currentQuestion instanceof MultipleChoice) {
                MultipleChoice multipleChoice = (MultipleChoice) currentQuestion;
                currentQuestionPanel = assembleMultipleChoiceQuestion(multipleChoice, i+1);
            } else if (currentQuestion instanceof FillInTheBlank) {
                FillInTheBlank fillInTheBlank = (FillInTheBlank) currentQuestion;
                currentQuestionPanel = assembleFillInTheBlankQuestion(fillInTheBlank, i+1);
            }
            questionsPanel.add(currentQuestionPanel);
        }

        JButton submitButton = new JButton("Submit");
        submitButton.setActionCommand("submit quiz");
        submitButton.addActionListener(actionListener);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back out of quiz");
        backButton.addActionListener(actionListener);
        JPanel submitOrBackPanel = new JPanel(new FlowLayout());
        submitOrBackPanel.add(submitButton);
        submitOrBackPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(submitOrBackPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Assembles a panel of a true or false question for a student taking a quiz
     *
     * @param trueFalse the question being assembled
     * @param index the index of the question
     * @return JPanel containing the question and answer options for display
     */
    private JPanel assembleTrueFalseQuestion(TrueFalse trueFalse, int index) {
        String questionName = trueFalse.getQuestion();
        int pointValue = trueFalse.getPointValue();
        String questionTitle = index + ". " + questionName + " (" + pointValue + " point";
        if (pointValue != 1) questionTitle += "s";
        questionTitle += ")";
        JLabel questionLabel = new JLabel(questionTitle);

        JRadioButton trueButton = new JRadioButton("True");
        trueButton.setActionCommand("true");
        JRadioButton falseButton = new JRadioButton("False");
        falseButton.setActionCommand("false");
        ButtonGroup trueOrFalseGroup = new ButtonGroup();
        trueOrFalseGroup.add(trueButton);
        trueOrFalseGroup.add(falseButton);
        activeComponents.add(trueOrFalseGroup);
        JPanel trueOrFalsePanel = new JPanel(new BorderLayout());
        trueOrFalsePanel.add(trueButton, BorderLayout.NORTH);
        trueOrFalsePanel.add(falseButton, BorderLayout.CENTER);

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        questionPanel.add(trueOrFalsePanel, BorderLayout.CENTER);
        return questionPanel;
    }

    /**
     * Assembles a panel of a multiple choice question for a student taking a quiz
     *
     * @param multipleChoice the question being assembled
     * @param index the index of the question
     * @return JPanel containing the question and answer options for display
     */
    private JPanel assembleMultipleChoiceQuestion(MultipleChoice multipleChoice, int index) {
        String questionName = multipleChoice.getQuestion();
        int pointValue = multipleChoice.getPointValue();
        String questionTitle = index + ". " + questionName + " (" + pointValue + " point";
        if (pointValue != 1) questionTitle += "s";
        questionTitle += ")";
        JLabel questionLabel = new JLabel(questionTitle);

        ArrayList<String> answerChoices = multipleChoice.getAnswerChoices();
        ButtonGroup choicesGroup = new ButtonGroup();
        JPanel choicesPanel = new JPanel(new GridLayout(0, 1));

        for (int i = 0; i < answerChoices.size(); i++) {
            String answer = answerChoices.get(i);
            JRadioButton answerButton = new JRadioButton(answer);
            answerButton.setActionCommand(Integer.toString(i));
            choicesGroup.add(answerButton);
            choicesPanel.add(answerButton);
        }

        activeComponents.add(choicesGroup);

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(choicesPanel);
        questionPanel.add(scrollPane, BorderLayout.CENTER);
        return questionPanel;
    }

    /**
     * Assembles a panel of a multiple choice question for a student taking a quiz
     *
     * @param fillInTheBlank the question being assembled
     * @param index the index of the question
     * @return JPanel containing the question and answer field for display
     */
    private JPanel assembleFillInTheBlankQuestion(FillInTheBlank fillInTheBlank, int index) {
        String questionName = fillInTheBlank.getQuestion();
        int pointValue = fillInTheBlank.getPointValue();
        String questionTitle = index + ". " + questionName + " (" + pointValue + " point";
        if (pointValue != 1) questionTitle += "s";
        questionTitle += ")";
        JLabel questionLabel = new JLabel(questionTitle);

        JLabel answerLabel = new JLabel("Your Answer:");
        JTextField answerTxt = new JTextField(30);
        activeComponents.add(answerTxt);
        JPanel answerPanel = new JPanel(new FlowLayout());
        answerPanel.add(answerLabel);
        answerPanel.add(answerTxt);

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        questionPanel.add(answerPanel, BorderLayout.CENTER);
        return questionPanel;
    }

    /**
     * Displays a list of the student's submissions for later viewing
     */
    private void createStudentSubmissionMenu() {
        menuLocation = STUDENT_SUBMISSION_MENU;

        mainPanel.removeAll();
        activeComponents.clear();

        ArrayList<Submission> submissions = client.getStudentSubmissions();
        JPanel submissionsPanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup submissionsGroup = new ButtonGroup();

        for (int i = 0; i < submissions.size(); i++) {
            Submission currentSubmission = submissions.get(i);
            String submissionTime = currentSubmission.getTimestamp().toString();
            String displaySubmission = "Submission " + (i+1) + ": " + submissionTime;
            JRadioButton submissionButton = new JRadioButton(displaySubmission);
            submissionButton.setActionCommand(Integer.toString(i));
            submissionsGroup.add(submissionButton);
            submissionsPanel.add(submissionButton);
        }

        activeComponents.add(submissionsGroup);

        JButton selectButton = new JButton("Select");
        selectButton.setActionCommand("view submission");
        selectButton.addActionListener(actionListener);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back to student quiz options menu");
        backButton.addActionListener(actionListener);
        JPanel selectOrBackPanel = new JPanel(new FlowLayout());
        selectOrBackPanel.add(selectButton);
        selectOrBackPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(submissionsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(selectOrBackPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays a list of all submissions to a quiz for later viewing
     */
    private void createTeacherSubmissionMenu() {
        menuLocation = TEACHER_SUBMISSION_MENU;

        mainPanel.removeAll();
        activeComponents.clear();

        ArrayList<Submission> submissions = client.getAllSubmissions();
        JPanel submissionsPanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup submissionsGroup = new ButtonGroup();

        for (int i = 0; i < submissions.size(); i++) {
            Submission currentSubmission = submissions.get(i);
            String  studentName = currentSubmission.getStudent().getUsername();
            String submissionTime = currentSubmission.getTimestamp().toString();
            String displaySubmission = "Submission " + (i+1) + ": " + studentName + " - " + submissionTime;
            JRadioButton submissionButton = new JRadioButton(displaySubmission);
            submissionButton.setActionCommand(Integer.toString(i));
            submissionsGroup.add(submissionButton);
            submissionsPanel.add(submissionButton);
            ArrayList<String> tempArray=new ArrayList<>();
            tempArray.add(studentName);
            int sum=0;
            for(ArrayList<String> temp2 : forActiveTeacherSubmission){
                if(temp2.get(0).equals(studentName)){
                    sum=sum+1;
                }
            }
            tempArray.add(String.valueOf(sum));
            forActiveTeacherSubmission.add(tempArray);
        }

        activeComponents.add(submissionsGroup);

        JButton selectButton = new JButton("Select");
        selectButton.setActionCommand("view submission");
        selectButton.addActionListener(actionListener);
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back to teacher quiz options menu");
        backButton.addActionListener(actionListener);
        JPanel selectOrBackPanel = new JPanel(new FlowLayout());
        selectOrBackPanel.add(selectButton);
        selectOrBackPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(submissionsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(selectOrBackPanel, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays a selected submission
     */
    private void createSubmissionViewer() {
        menuLocation = NO_UPDATE_NEEDED;

        mainPanel.removeAll();
        activeComponents.clear();

        ArrayList<Question> questions = client.getQuestions();

        ArrayList<String> studentAnswers = client.getAnswersFromSubmission(studentName);
        JPanel submissionPanel = new JPanel(new GridLayout(0, 1));

        for (int i = 0; i < questions.size(); i++) {
            Question currentQuestion = questions.get(i);
            String currentStudentAnswer="";
            currentStudentAnswer = studentAnswers.get(i);

            JPanel currentQuestionPanel = new JPanel(new BorderLayout());

            String questionName = currentQuestion.getQuestion();
            int pointValue = currentQuestion.getPointValue();
            String questionTitle = (i+1) + ". " + questionName + " (" + pointValue + " point";
            if (pointValue != 1) questionTitle += "s";
            questionTitle += ")";
            JLabel questionLabel = new JLabel(questionTitle);
            currentQuestionPanel.add(questionLabel, BorderLayout.NORTH);

            String studentAnswerText = "";
            if (accountType == STUDENT_OPTION) {
                studentAnswerText = "Your answer: " + currentStudentAnswer;
            } else if (accountType == TEACHER_OPTION) {
                studentAnswerText = "Their answer: " + currentStudentAnswer;
            }
            JLabel studentAnswerLabel = new JLabel(studentAnswerText);
            currentQuestionPanel.add(studentAnswerLabel, BorderLayout.CENTER);

            String correctAnswerText = "Correct answer: ";
            if (currentQuestion instanceof TrueFalse) {
                TrueFalse trueFalse = (TrueFalse) currentQuestion;
                correctAnswerText += trueFalse.getAnswer();
            } else if (currentQuestion instanceof MultipleChoice) {
                MultipleChoice multipleChoice = (MultipleChoice) currentQuestion;
                int correctAnswerIndex = multipleChoice.getCorrectAnswerIndex();
                String correctAnswerChoice = multipleChoice.getAnswerChoices().get(correctAnswerIndex);
                correctAnswerText += correctAnswerChoice;
            } else if (currentQuestion instanceof FillInTheBlank) {
                FillInTheBlank fillInTheBlank = (FillInTheBlank) currentQuestion;
                correctAnswerText += fillInTheBlank.getAnswer();
            }
            JLabel correctAnswerLabel = new JLabel(correctAnswerText);
            currentQuestionPanel.add(correctAnswerLabel, BorderLayout.SOUTH);

            submissionPanel.add(currentQuestionPanel);
        }

        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back to submission menu");
        backButton.addActionListener(actionListener);

        JScrollPane scrollPane = new JScrollPane(submissionPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        mainPanel.validate();
        mainPanel.repaint();
    }
}
