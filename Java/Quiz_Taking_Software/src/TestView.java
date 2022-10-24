import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

//Explanations for Client methods:
//I probably haven't explained this well. Please ask me if you need clarification on anything, or if something in this
//plan needs to be edited because it won't work with the rest of the program.
//*
//*
//*
//Program Location (details for almost all methods):
//Client should use fields to track the current program location, which includes the following:
// - the active user
// - the active course
// - the active quiz
// - the active question
// - the active submission
//Not all of these will be necessary at any given time (eg in the main menu, the user has not selected a course,
//so there is no active course).
//If a particular component is inactive, it should be set to null.
//I have included a few clear methods that will indicate to client when one of the active objects is being exited and
//should be set to null.
//However, other methods (eg deleteQuiz()) will also need to clear the active object.
//(Implementation details can be different from this; I just need the Client to know what to access at any given time.)
//Any getter methods should take the program location into account and get only the lists from that particular location.
//(eg if a particular quiz is active, the Client should get the questions from that particular quiz.)
//Getter methods use numbers because I can't transfer objects from the screen creator to the action listener.
//If absolutely necessary, I can try to determine how to do this, but it will likely take several hours of work.
//*
//*
//*
//Difference between getAccountCourses() and getAllCourses():
//getAccountCourses() is used to access the courses an account already has - that is, that account's ArrayList of courses
//getAllCourses() is used to let students add courses, so it should get every course that has been created and allow user selection
//*
//*
//*
//What close() does:
//close() indicates that the user has quit the program, and that the Client should ensure any remaining data is stored,
//then also terminate.
//*
//*
//*
//Again, if I haven't explained this well or have planned something that won't work, please let me know so we can
//discuss further.

//TODO: implement the rest of the menu logic
//parts of the menu logic remaining:
//create quiz - long series of methods allowing teacher to create quiz
//edit quiz - displays list of questions, then allows teacher to edit any particular question
//take quiz - long series of methods allowing student to take quiz
//view submission - displays submissions to quiz, then allows viewing of any submission
//TODO: make sure client gets notified when view closes! (might be better implemented in client)

/**
 * Online Quiz Navigator v2 - View
 *
 * Handles the GUI for the Online Quiz Navigator.
 * Listens for user updates, then sends them to Client for handling.
 * Listens for updates from Client, then refreshes the board as need be.
 *
 * @author Nathan Reed, lab sec L24
 * @version December 6, 2021
 */
public class TestView extends JComponent {
    private Client client;
    private JFrame frame;
    private JPanel mainPanel;
    private ArrayList<Object> activeComponents = new ArrayList<Object>();

    private int accountType;
    private static final int STUDENT_OPTION = 0;
    private static final int TEACHER_OPTION = 1;

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
            } //else if (actionCommand.equals("choose student option")) {
//                ButtonGroup optionsGroup = (ButtonGroup) activeComponents.get(0);
//                String optionChosen = optionsGroup.getSelection().getActionCommand();
//                if (optionChosen.equals("take quiz")) {
//                    createTakeQuizIntroScreen();
//                } else if (optionChosen.equals("view previous submissions from student")) {
//                    createStudentSubmissionMenu();
//                }
//            } else if (actionCommand.equals("choose teacher option")) {
//                ButtonGroup optionsGroup = (ButtonGroup) activeComponents.get(0);
//                String optionChosen = optionsGroup.getSelection().getActionCommand();
//                if (optionChosen.equals("edit quiz")) {
//                    createEditQuizMenu();
//                } else if (optionChosen.equals("view all previous submissions")) {
//                    createTeacherSubmissionMenu();
//                } else if (optionChosen.equals("delete quiz")) {
//                    int deleteChoice = JOptionPane.showConfirmDialog(null,
//                            "Are you sure you want to delete this quiz?", "Delete quiz?",
//                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                    if (deleteChoice == JOptionPane.YES_OPTION) {
//                        client.deleteQuiz();
//                        createCourseMenu();
//                    }
//                }
//            } else if (actionCommand.equals("choose quiz input option")) {
//                ButtonGroup optionsGroup = (ButtonGroup) activeComponents.get(0);
//                String optionChosen = optionsGroup.getSelection().getActionCommand();
//                if (optionChosen.equals("import from file")) {
//                    JFileChooser fileChooser = new JFileChooser();
//                    fileChooser.setDialogTitle("Choose Quiz");
//                    int selectOrCancel = fileChooser.showOpenDialog(null);
//                    if (selectOrCancel == JFileChooser.APPROVE_OPTION) {
//                        File selectedFile = fileChooser.getSelectedFile();
//                        if (client.addImportedQuiz(selectedFile)) {
//                            JOptionPane.showMessageDialog(null, "Quiz imported successfully!",
//                                    "Success!", JOptionPane.INFORMATION_MESSAGE);
//                            createCourseMenu();
//                        } else {
//                            JOptionPane.showMessageDialog(null, "Quiz import unsuccessful. " +
//                                    "Please try again.", "Error importing quiz", JOptionPane.ERROR_MESSAGE);
//                        }
//                    }
//                } else if (optionChosen.equals("enter manually")) {
//                    createCreateQuizTitleScreen();
//                }
//            } else if (actionCommand.equals("create quiz")) {
//                JTextField quizNameTxt = (JTextField) activeComponents.get(0);
//                ButtonGroup randomizeGroup = (ButtonGroup) activeComponents.get(1);
//                String quizName = quizNameTxt.getText();
//                String randomizeChoice = randomizeGroup.getSelection().getActionCommand();
//                if (randomizeChoice.equals("yes")) {
//                    client.createQuiz(quizName, true);
//                } else if (randomizeChoice.equals("no")) {
//                    client.createQuiz(quizName, false);
//                }
//                createCreateQuestionScreen();
//            } else if (actionCommand.equals("true or false")) {
//                createCreateTrueFalseQuestion();
//            } else if (actionCommand.equals("multiple choice")) {
//                createSelectNumAnswerChoices();
//            } else if (actionCommand.equals("fill in the blank")) {
//                createCreateFillInTheBlankQuestion();
//            } else if (actionCommand.equals("done with adding questions")) {
//                client.lastQuestionAdded(); //this method just tells Client that there are no more questions
//                createCourseMenu();
//            } else if (actionCommand.equals("back to teacher quiz options menu")) {
//                createTeacherQuizOptionsMenu();
//            } else if (actionCommand.equals("back to course menu")) {
//                client.clearActiveQuiz();
//                createCourseMenu();
//            } else if (actionCommand.equals("back to main menu")) {
//                client.clearActiveCourse();
//                createMainMenu();
//            } else if (actionCommand.equals("quit")) {
//                int closeConfirmation = JOptionPane.showConfirmDialog(null,
//                        "Are you sure you want to quit?", "Quit?",
//                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                if (closeConfirmation == JOptionPane.YES_OPTION) {
//                    client.close();
//                    frame.dispose();
//                }
//            }
        }
    };

    //TODO: remove this method before submission
    /**
     * Constructor for testing without access to client
     */
//    private View() {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                createGUI();
//            }
//        });
//    }

    /**
     * Sets up View object and Event Dispatch Thread when called by Client
     * Structure borrowed from Week 11 Wednesday lecture slide 75
     *
     * @param client the Client object used for this session
     */
    TestView(Client client) {
        this.client = client;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });
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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Displays text fields for domain name and port number
     * as well as a submit button
     */
    private void createConnectionScreen() {
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
        JButton quitButton = new JButton("Quit");
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
        mainPanel.add(selectOrBackPanel);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Displays options for a particular quiz (take quiz or view submissions) to a student
     */
    private void createStudentQuizOptionsMenu() {
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
        backButton.setActionCommand("back to teacher quiz options menu");
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
        JTextField pointValueTxt = new JTextField(30);
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

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
//    private void createCreateTrueFalseQuestion() {
//        String questionName = "";
//        int pointValue = 0;
//
//        boolean trueOrFalse = false;
//        client.createTrueFalseQuestion(questionName, pointValue, trueOrFalse);
//
//        mainPanel.validate();
//        mainPanel.repaint();
//    }

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
    private void createSelectNumAnswerChoices() {
        mainPanel.validate();
        mainPanel.repaint();
    }

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
//    private void createCreateMultipleChoiceQuestion() {
//        String questionName = "";
//        int pointValue = 0;
//
//        int numAnswerChoices = 0;
//        String[] answerChoices = new String[numAnswerChoices];
//        int correctAnswerIndex = 0;
//        client.createMultipleChoiceQuestion(questionName, pointValue, numAnswerChoices, answerChoices,
//                correctAnswerIndex);
//
//        mainPanel.validate();
//        mainPanel.repaint();
//    }
//
//    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
//    //use that method in the actual implementation. I have described various details of the needed methods at the top of
//    //the page directly under the import statements.
//    private void createCreateFillInTheBlankQuestion() {
//        String questionName = "";
//        int pointValue = 0;
//
//        String answer = "";
//        client.createFillInTheBlankQuestion(questionName, pointValue, answer);
//
//        mainPanel.validate();
//        mainPanel.repaint();
//    }

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
    private void createEditQuizMenu() {
        mainPanel.removeAll();
        activeComponents.clear();

        ArrayList<Question> questions = client.getQuestions();
        int questionNumber = 0;
        client.setActiveQuestion(questionNumber);

        mainPanel.validate();
        mainPanel.repaint();
    }

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
//    private void createEditQuestionScreen() {
//        mainPanel.removeAll();
//        activeComponents.clear();
//
//        client.deleteQuestion();
//        Question question = new Question();
//        client.updateQuestion(question);
//
//        mainPanel.validate();
//        mainPanel.repaint();
//    }

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
    private void createTakeQuizIntroScreen() {
        mainPanel.removeAll();
        activeComponents.clear();

        mainPanel.validate();
        mainPanel.repaint();
    }

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
    private void createActiveQuizScreen() {
        mainPanel.removeAll();
        activeComponents.clear();

        ArrayList<Question> questions = client.getQuestions();
        Quiz quiz = new Quiz(new Scanner(System.in), new Course("", new Teacher("", ""), 0));
        Submission submission = new Submission(new Student("", ""), quiz);
//        client.submitSubmission(submission);

        mainPanel.validate();
        mainPanel.repaint();
    }

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
    private JPanel assembleTrueFalseQuestion() {
        return new JPanel();
    }

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
    private JPanel assembleMultipleChoiceQuestion() {
        return new JPanel();
    }

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
    private JPanel assembleFillInTheBlankQuestion() {
        return new JPanel();
    }

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
//    private void createStudentSubmissionMenu() {
//        mainPanel.removeAll();
//        activeComponents.clear();
//
//        ArrayList<Submission> submissions = client.getStudentSubmissions();
//        client.setActiveSubmission(submissionNumber);
//
//        mainPanel.validate();
//        mainPanel.repaint();
//    }
//
//    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
//    //use that method in the actual implementation. I have described various details of the needed methods at the top of
//    //the page directly under the import statements.
//    private void createTeacherSubmissionMenu() {
//        mainPanel.removeAll();
//        activeComponents.clear();
//
//        ArrayList<Submission> submissions = client.getAllSubmissions();
//        client.setActiveSubmission(submissionNumber);
//
//        mainPanel.validate();
//        mainPanel.repaint();
//    }

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
//    private void createSubmissionViewer() {
//        mainPanel.removeAll();
//        activeComponents.clear();
//
//        Quiz quiz = client.getCurrentQuiz();
//        ArrayList<Question> questions = client.getQuestions();
//        client.clearActiveSubmission();
//
//        mainPanel.validate();
//        mainPanel.repaint();
//    }

    //NOTE: this method has not been implemented yet. All calls to Client methods are for reference, since I expect to
    //use that method in the actual implementation. I have described various details of the needed methods at the top of
    //the page directly under the import statements.
    /**
     * Updates view being displayed to be consistent with new server updates
     */
    public void update() {

    }

    //TODO: remove before submission
    /**
     * Main method for testing
     *
     * @param args
     */
//    public static void main(String[] args) {
//        View view = new View();
//    }
}
