# CS180-Project-5
Initialization and Connection Instructions:
<br>
<br>
First, run the main method in the Server class on any computer networked to the computer you intend to run Client on.
<br>
Then, run the main method in the Client class.
<br>
This will open a GUI window that will allow you to enter connection details.
<br>
Enter the domain name of the computer you're running Server on, as well 8818 for the port number.
<br>
This should allow you to access Online Quiz Navigator v2.
<br>
<br>
IMPORTANT INFO FOR PRESENTATION: All audio/video files are included on each slide of the presentation.
<br>
<br>
Nathan Reed submitted the Report and the Project. Aditya Menon submitted the Presentation.
<br>
<br>
Tasks Completed:
<br>
Nathan R - built View class
<br>
Nathan S - Worked on Server and Concurrency class
<br>
Aditya - Worked on client class
<br>
Jay - Worked on Server and Concurrency Class
<br>
Louis - Wrote the part 1 report, support the concurrency + server class.
<br>
<br>
<br>
Classes:
<br>
*********
<br>
View
<br>
-------------------------------
<br>
Client client - stores active client object so relevant Client methods can be called
<br>
JFrame frame - the JFrame controlled by View
<br>
JPanel mainPanel - the main panel contained in the frame
<br>
ArrayList<Object> activeComponents - used for storing the active components on the screen so they can be accessed by the action listener
<br>
int accountType - indicates whether the account being used is a Student or a Teacher
<br>
int menuLocation - stores the location in the menu so that the correct menu can be refreshed in case of an update
<br>
ActionListener actionListener - the action listener for the GUI
<br>
public void update() - Updates view being displayed to be consistent with new server updates
<br>
private void createGUI() - Sets up initial components of GUI
<br>
private void createConnectionScreen() - Displays text fields for domain name and port number
<br>
private void createLoginScreen() - Displays fields allowing user to login or create account
<br>
private void createMainMenu() - Displays a list of courses the user currently has and an "add course" option
<br>
private void createAddCourseScreen() - Displays all available courses to student, allowing them to add any course.
<br>
private void createCreateCourseScreen() - Displays text fields for course creation to teacher, allowing them to create a course.
<br>
private void createCourseMenu() - Displays the menu for a particular course, allowing the user to select which quiz they want to access, as well as create a new quiz if the user is a teacher.
<br>
private void createStudentQuizOptionsMenu() - Displays options for a particular quiz (take quiz or view submissions) to a student
<br>
private void createTeacherQuizOptionsMenu() - Displays options for a particular quiz (edit quiz, view submissions, or delete quiz) to teacher
<br>
private void createCreateQuizIntroScreen() - Displays choice of quiz input method to teacher
<br>
private void createCreateQuizTitleScreen() - Displays screen to set quiz title and randomization
<br>
private void createCreateQuestionScreen() - Displays generic fields for question creation and allows the user to select whether the question should be true or false, multiple choice, or fill in the blank
<br>
private void createCreateTrueFalseQuestion() - Displays possible answer options for true or false questions, allows teacher to choose which should be correct
<br>
private void createSelectNumAnswerChoices() - Displays a field that allows the teacher to set the number of answer choices
<br>
private void createCreateMultipleChoiceQuestion(int numAnswerChoices) - Displays text fields for teacher to specify answer choices
<br>
private void createCreateFillInTheBlankQuestion() - Displays text field for teacher to specify answer to fill in the blank question
<br>
private void createEditQuizMenu() - Displays a list of questions that the teacher can choose to edit
<br>
private void createEditQuestionScreen() - Determines type of question to be edited, then calls relevant edit screen creation method
<br>
private void createEditTrueFalseScreen(TrueFalse trueFalse) - Displays fields of true or false question for editing
<br>
private void createEditMultipleChoiceScreen(MultipleChoice multipleChoice) - Displays fields of multiple choice question for editing
<br>
private void createEditFillInTheBlankScreen(FillInTheBlank fillInTheBlank) - Displays fields of fill in the blank question for editing
<br>
private void createTakeQuizIntroScreen() - Displays a confirmation message to make sure the student is taking the right quiz
<br>
private void createActiveQuizScreen() - Displays quiz to student
<br>
private JPanel assembleTrueFalseQuestion(TrueFalse trueFalse, int index) - Assembles a panel of a true or false question for a student taking a quiz
<br>
private JPanel assembleMultipleChoiceQuestion(MultipleChoice multipleChoice, int index) - Assembles a panel of a multiple choice question for a student taking a quiz
<br>
private JPanel assembleFillInTheBlankQuestion(FillInTheBlank fillInTheBlank, int index) - Assembles a panel of a multiple choice question for a student taking a quiz
<br>
private void createStudentSubmissionMenu() - Displays a list of the student's submissions for later viewing
<br>
private void createTeacherSubmissionMenu() - Displays a list of all submissions to a quiz for later viewing
<br>
private void createSubmissionViewer() - Displays a selected submission
<br>
<br>
Client
<br>
------------------------
<br>
Socket socket - Socket used for connection to Server
<br>
int courseNumber - Active course
<br>
int quizNumber - Active quiz
<br>
User user - Active user
<br>
int questionNumber - Active question
<br>
int submissionNumber - Active submission
<br>
ObjectOutputStream oos - Writes objects to server
<br>
ObjectInputStream ois - Reads objects from server
<br>
public boolean connectToServer(String domainName, String inputPortNumber) - Used to establish connection with Server
<br>
public boolean createAccount(String username, String password, String typeOfAccount) - Used to create account for Students and Teachers respectively
<br>
public boolean login(String username, String password, String typeOfAccount) - Used to login for Students and Teachers respectively
<br>
public ArrayList<Course> getAccountCourses() - Used to access courses associated with active user
<br>
public ArrayList<Course> getAllCourses() - Used to access all created courses
<br>
public ArrayList<Quiz> getQuizzes() - Used to access all quizzes under a specific course
<br>
public Quiz getCurrentQuiz() - Used to access active quiz
<br>
public ArrayList<Question> getQuestions() - Used to access questions under active quiz
<br>
public ArrayList<Submission> getStudentSubmissions() - Used to get a students submissions for a quiz
<br>
public ArrayList<Submission> getAllSubmissions() - Used to get all submissions for a quiz
<br>
public boolean createCourse(String courseName, int courseNumber) - Used to create a course
<br>
public boolean addImportedQuiz(File f) - Used to add a quiz to a course with a file
<br>
public void createQuiz(String quizName, boolean randomize) - Used to create a quiz under the active course
<br>
public void addStudentToCourse(int courseNumber) - Used to add a student to a course
<br>
public void deleteQuiz() - Used to delete the active quiz
<br>
public void createTrueFalseQuestion(String questionName, int pointValue, boolean trueOrFalse) - Used to create a true or false question
<br>
public void createMultipleChoiceQuestion(String questionName, int pointValue, int numChoices, ArrayList<String> answerChoices, int correctAnswerIndex) - Used to create a multiple choice question
<br>
public void createFillInTheBlankQuestion(String questionName, int pointValue, String answer) - Used to create fill in the blank question
<br>
public void updateTrueFalseQuestion(String questionName, int pointValue, boolean trueOrFalse) - Used to update true or false question
<br>
public void updateMultipleChoiceQuestion(String questionName, int pointValue, int numChoices, ArrayList<String> answerChoices, int correctAnswerIndex) - Used to update a multiple choice question
<br>
public void updateFillInTheBlankQuestion(String questionName, int pointValue, String answer) - Used to update fill in the blank question
<br>
public void deleteQuestion(int questionNumber) - Used to delete question
<br>
public void submitSubmission(ArrayList<String> answers) - Used to submit answers and create an answer report for a taken quiz
<br>
public void lastQuestionAdded() - Used to reset active quiz and active question values
<br>
public void setActiveQuestion(int questionNumber) - Used to set active question
<br>
public void setActiveSubmission(int submissionNumber) - Used to set active submission
<br>
public void setActiveCourse(int courseNumber) - Used to set active course
<br>
public void setActiveQuiz(int quizNumber) - Used to set active quiz
<br>
public void clearActiveCourse() - Used to clear the active course
<br>
public void clearActiveSubmission() - Used to clear the active submission
<br>
public void clearActiveQuestion() - Used to clear the active question
<br>
public void clearActiveQuiz() - Used to clear the active quiz
<br>
public Question getActiveQuestion() - Used to access active question
<br>
public ArrayList<String> getAnswersFromSubmission() - Used to get answers from the active submission
<br>
public void close() - Used to inform server that client is closing
<br>
<br>
User (implements Serializable):
<br>
-------------------------------
<br>
String username - Stores username of Student/Teacher
<br>
String password - Stores password of Student/Teacher
<br>
boolean isTeacher - True if User is Teacher, False if User is Student
<br>
public User(String username, String password, boolean isTeacher) - Constructor initializing fields of User
<br>
public String getUsername() - Returns username
<br>
public String getPassword() - Returns password
<br>
public boolean isTeacher() - Returns isTeacher
<br>
public boolean equals(Object o) - Returns true or false comparing Object o to this instance of User
<br>
public static void testEquals() - Method used to test the equals(Object o) method
<br>
<br>
Student (extends User):
<br>
-----------------------
<br>
private ArrayList<Course> courses - Stores the courses the Student chooses to enroll in
<br>
private ArrayList<Submission> submissions - Stores the quiz submissions of the Student
<br>
public Student(String username, String password) - Sends username, password, false to super class
<br>
public ArrayList<Submission> getSubmissions() - Returns submissions
<br>
public void addToCourse(Course c) - Adds Course c to courses
<br>
public ArrayList<Course> getCourses() - Returns courses
<br> 
public Course getCourse(int i) - Returns Course object with courseNumber i; null otherwise
<br>
public void addSubmission(Submission s) - Adds Submission s to submissions
<br>
public String toString() - Returns String representation of Student
<br>
<br>
Teacher (extends User):
<br>
-----------------------
<br>
private ArrayList<Course> courses - Stores courses created by the Teacher
<br>
public Teacher(String username, String password) - Sends username, password, true to super class
<br>
public void addCourse(Course c) - Adds Course c to courses
<br>
public ArrayList<Course> getCourses() - Returns courses
<br>
public Course getCourse(int i) - Returns Course object with courseNumber i; null otherwise
<br>
public String toString() - Returns String representation of Teacher
<br>
<br>
Menu:
<br>
-----------------------
<br>
public static void main() - controls the highest level of menu flow
<br>
public static int getIntegerFromScanner(Scanner scanner, String message, int minValue, int maxValue, 
boolean includeNewline) - returns an int between the minValue and maxValue after prompting the user, reprompts if 
the value is invalid or outside the given range
<br>
public static String getStringFromScanner(Scanner scanner, String message, boolean includeNewline) - 
returns a String that is not blank after prompting the user, reprompts if the String is blank
<br>
private static User login(Scanner scanner, TeacherList teacherList, StudentList studentList) - 
prompts user to log in or create account, and retrieves and returns that account
<br>
private static void teacherMenu(Scanner scanner, Teacher teacher, CourseList courseList) - 
controls the upper menu for the teacher, allowing selection of a course, then directs the teacher to
teacherCourseMenu()
<br>
private static Course createCourseMenu(Scanner scanner, Teacher teacher, CourseList courseList) - 
returns a course after creating it and adding it to all relevant directories
<br>
private static void teacherCourseMenu(Scanner scanner, Teacher teacher, Course course, CourseList courseList) -
controls the lower menu for the teacher after course selection, allowing the teacher to add, edit, delete, or view
quizzes
<br>
private static void studentMenu(Scanner scanner, Student student, CourseList courseList) -
controls the upper menu for the student, allowing selection of a course, then directs the student to
studentCourseMenu()
<br>
private static Course addCourseMenu(Scanner scanner, Student student, CourseList courseList) -
returns a course after adding it to a student's list of courses
<br>
private static void studentCourseMenu(Scanner scanner, Student student, Course course, CourseList courseList) - 
controls the lower menu for the student after course selection, allowing the student to take quizzes or view previous
submissions
<br>
<br>
File Imports:
<br>
-----------------------
<br>
public static String prompt() - 
return the user's input path for quiz files
<br>
public static ArrayList<String> readFile(String filePath) - 
return an ArrayList of the contents of the quiz file for further processing.
<br>
<br>
Course implements Serializable:
<br>
-----------------------
<br>
private String courseName - Stores name of Course
<br>
private Teacher teacher - Stores the teacher who created the course
<br>
private int courseNumber - Stores the unique number of the course
<br>
private ArrayList<Student> students - Stores the students taking the course
<br>
private ArrayList<Quiz> quizzes - Stores the quizzes that are present in the course
<br>
public Course(String courseName, Teacher teacher, int courseNumber) - Constructor that creates a course object
<br>
public String getCourseName() - returns the course name
<br>
public void setCourseName(String courseName) - sets the course name to String passed
<br>
public Teacher getTeacher() - returns the teacher who made the course
<br>
public void setTeacher(Teacher teacher) - sets the teacher who made the course
<br>
public int getCourseNumber - returns the course number 
<br>
public void setCourseNumber(int courseNumber) - sets the course number
<br>
public ArrayList<Student> getStudents() - returns the list of students taking the course
<br>
public void setStudents(ArrayList<Student> students - sets the list of students taking the course
<br>
public ArrayList<Quiz> getQuizzes() - returns the quizzes present in the course
<br>
public void setQuizzes(ArrayList<Quiz> quizzes) - sets the quizzes present in the course
<br>
public boolean addQuiz(Quiz quiz) - adds a quiz to the list of quizzes present in the course
<br>
public boolean removeQuiz(Quiz quiz) - removes a quiz from the list of quizzes in the course
<br>
public boolean addStudent(Student student) - adds a student to the course
<br>
public boolean removeStudent(Student student) - removes a student from the course
<br>
public boolean equals(Object o) - checks if the object passed is the same as the course object being referenced at that point
<br>
<br>
CourseList implements Serializable: 
<br>
-----------------------
<br>
public static final String FILENAME - stores the binary file that has the CourseList object
<br>
private ArrayList<Course> courses - stores the list of courses that have been created by teachers
<br>
public static CourseList readFromFile() - reads from the binary file to return the CourseList object
<br>
public ArrayList<Course> getCourses() - returns the list of courses created by teachers up till that point
<br>
public Course getCourse(int i) - returns the course in the course list at the index passed
<br>
public boolean exists(Course course) - checks whether the specific course passed is contained in the course list
<br>
public boolean add(Course course) - Adds the specific course passed to the course list, and returns whether it was added or already existed
<br> 
public boolean update(Course course) - stores an updated course object in the place of the same  course
<br>
public void saveToFile() - saves the CourseList object to the file
<br>
<br>
StudentList implements Serializable
<br>
-----------------------
<br>
public static final String FILENAME - stores the binary file that has the StudentList object
<br>
private ArrayList<Student> students - stores the list of students that have created an account
<br>
public static StudentList readFromFile() - reads from the binary file to return the StudentList object
<br>
public boolean exists(Student student) - checks whether the specific student passed is contained in the student list
<br>
public boolean add(Student student) - adds a student to the student list, checks whether student has been added or already existed
<br>
public boolean removes(Student student) - removes a student to the student list, checks whether student has been removed or never existed
<br>
public void saveToFile() - saves the StudentList object to the file
<br>
public Student findStudent(String username, String password) - returns a student from the student list whose username and password is given
<br>
<br>
TeacherList implements Serializable
<br>
-----------------------
<br>
public static final String FILENAME - stores the binary file that has the TeacherList object
<br>
private ArrayList<Teacher> teachers - stores the list of teachers that have created an account
<br>
public static TeacherList readFromFile() - reads from the binary file to return the TeacherList object
<br>
public boolean exists(Teacher teacher) - checks whether the specific teacher passed is contained in the teacher list
<br>
public boolean add(Teacher teaher) - adds a teacher to the teacher list, checks whether teacher has been added or already existed
<br>
public void saveToFile() - saves the TeacherList object to the file
<br>
public Teacher findTeacher(String username, String password) - returns a teacher from the teacher list whose username and password is given
<br>
<br>
Submission implements Serializable 
<br>
-----------------------
<br>
Student student - stores the who submittd the submission 
<br>
Quiz quizz - stores the quiz that is being attempted as a quiz submission
<br>
Timestamp timestamp - stores exactly when the student submitted his submission
<br>
int totalScore - stores the total score the student amassed by answering the question correct in the quiz submission
<br>
String filename - stores the name of the file in which the submission is being stored
<br>
public Submission(Student student, Quiz quizz) - Constructor that create a submission object using the student taking the quiz and the quiz being taken
<br>
public Student getStudent() - returns the student who is submitting the quiz
<br>
public String getFilename() - returns the name of the file where the submission will get stored
<br>
public TimeStamp getTimeStamp() - returns the time stamp of the submission
<br>
public Course getCourse() - returns the course the quiz which is being submitted belongs to
<br>
public Quiz getQuiz() - returns the quiz the submission belongs to
<br>
public boolean takeQuiz(Scanner scanner) - the quiz is attempted, a submission is made and stored in a file using submissionReport(ArrayList<String> answers, ArrayList<Question> questions).
<br>
public void submissionReport(ArrayList<String> answers, ArrayList<Question> questions)- generates a submission report and stores in a text file
<br>
public String createsNewFile() - creates a new file to store the submission report in
<br>
public String readingTheAnswerFormFile(Scanner scanner) - reads the answer that a student submits in a text file
<br>
public void view(int submissionNumber) - shows the submission which is called through its number to the student
<br>
<br>
public class Quiz implements Serializable
<br>
-----------------------
<br>
private int numQuestions - number of questions in quiz
<br>
private ArrayList<Question> quiz - an ArrayList that holds Question objects
<br>
private String name -  name of the quiz
<br>
private Course course -  the course that the quiz was added to;
<br>
private ArrayList<Submission> submissions - an ArrayList of Submissions for the Quiz
<br>
private boolean randomized - true if the quiz should be randomized, false if it is not
<br>
public Quiz (Scanner scanner, Course course) -calls the the creatQuizByInputIndividually(scanner) method as well as setting this.course to course
<br>
public Quiz (String fileName, Scanner scanner, Course course) - another quiz constructor that asks for name of the quiz and sets this.name = name. It also uses a filename and reads from a file to assist in creating a quiz. it also sets this.course = course;
<br> 
public boolean isRandomized() - getter for the randomized varaible;
<br>
public ArrayList<Submission> getSubmission() - getter for submissions varaible
<br>
public void addSubmission(Submission s) -  adds a submission to the Arraylist submissions
<br>
public void createQuiz FromFile (ArrayList<String> questionList,Scanner scanner) - creates a quiz from the given ArrayList of questions
<br>
public void creatQuizByInputIndividually(Scanner scanner) - creates a quiz object by walking the user through the steps. 
<br>
public String getName() - returns the name of the quiz
<br>
public ArrayList<Question> getQuiz() -  returns the array list of questions
<br>
public void editQuiz(Scanner scanner) - gives the user the oppurtunity to edit the variables of the quiz including the questions
<br>
public randomizeQuestions() - randomizes the order of the questions in the quiz 
<br>
<br>
public class FillInTheBlank extends Question
<br>
-----------------------
<br>
String answer -  the answer to the question
<br>
public FillIntheBlank (String question, String answer, int pointValue) -calls the super class to set question and point value and also sets the answer to answer.
<br>
public String getAnswer() - returns the answer of the FillInTheBlankObject
<br>
public void displayQuesiton() - prints the question
<br>
public void editQuestion(Scanner scanner) - edits the question from user input
<br>
<br>
public class TrueFalse extends Question
<br>
-----------------------
<br>
String answer -  the answer to the question
<br>
public TrueFalse (String question, String answer, int pointValue) -calls the super class to set question and point value and also sets the answer to answer.
<br>
public String getAnswer() - returns the answer of the FillInTheBlankObject
<br>
public void displayQuesiton() - prints the question with the options : "True" and "False"
<br>
public void editQuestion(Scanner scanner) - edits the question from user input
<br>
<br>
public class MultipleChoice extends Question
<br>
-----------------------
<br>
int numChoice -  the number of choices in the question
<br>
String[] answerChoices -  the different of answer choices saved as strings
<br>
itn correctAnswerIndex - the index of the correct answer
<br>
public MultipleChoice (String question, int numChoices, String[] answerChoices,int answer Index, int pointValue) -Constructor that asigns the question, point value, number of choices, the answer choices, and the correct answer index
<br>
public void displayQuesiton() - prints the question with all of the different answer choices
<br>
public void editQuestion(Scanner scanner) - edits the question from user input
<br>
<br>
Question (implements Serializable)
<br>
----------------------------------
<br>
String question - Stores name of the question
<br>
int pointValue - Stores point value of specific question
<br> 
public Question(String question, int pointValue) - Instantiates question and pointValue
<br>
public String getQuestion() - Returns question
<br>
public int getPointValue() - Returns pointValue
<br>
public void editQuestion(Scanner scanner) - Edits question
<br>
<br>
Concurrency (extends Thread)
<br>
----------------------------------
<br>
Socket scoket - client socket
<br>
ObjectInputStream inputStream - Stream for receiving over network
<br>
ObjetOutputStream outputStream - Stream for sending over network
<br>
TeacherList teacherList - object that provides for data persistence and access to teachers
<br>
StudentList studentList - object that provides for data persistence and access to students
<br>
CourseList courseList - object that provides for data persistence and access to courses
<br>
String typeOfAccount - stores whether teacher or student is the client in the thread being run
<br>
User user - stores whether the client is teacher or student
<br>
public Concurrency (Socket socket, TeacherList teacherList, StudentList studentList, CourseList courseList) - Constructor that creates a thread to connect with the specific client
<br>
public void run() - receives info from the client and based on the info, accesses or modifies the base classes and information
<br>
public void login(String username, String password, String typeOfUser) - Client is logged in with details received
<br>
public void getAccountCourses() - if teacher asks, gets the courses made by the teacher. If student asks, gets the courses registered for by the student.
<br>
public void getAllCourses() - sends all the courses to client
<br>
public void getQuizzes(int courseNumber) - sends all the quizzes from the course that has that course number to clients
<br>
public void getCurrentQuiz(int courseNumber, int quizNumber) - sends the quiz that has been referenced
<br>
public void getQuestions(int courseNumber, int quizNumber) - returns the questions in a specific quiz
<br>
public void getActiveQuestion(int courseNumber, int quizNumber,int questionNumber) - sets the question being referenced as active
<br>
public void getStudentSubmissions() - get specified student's submission.
<br> 
public void getAllSubmissions(int courseNumber, int quizNumber) - get all submissions.
<br>
public void createAccount(String username, String password, String typeOfUser) - create an account with specified parameters.
<br>
public void createCourse(String courseName, int courseNumber) - create a course.
<br>
public boolean addImportedQuiz(int courseNumber, File file) - import a quiz from a specified file.
<br>
public void createQuiz(int courseNumber,String quizName, String randomize) - creat a quiz from specified parameters.
<br>
public void addStudentToCourse(int courseNumber) - add a student to a specific course.
<br>
public void deleteQuiz(int courseNumber,int quizNumber) - delete a quiz.
<br>
public void storeLists() - store courseList, teacherList, studentList to .ser files.
<br>
public void createTrueFalseQuestion(int courseNumber,int quizNumber, String questionName, int pointValue, String trueFalse) - create True/False question.
<br>
public void updateTrueFalseQuestion(int courseNumber, int quizNumber,int questionNumber,String question, int pointValue,String answer) - update True/False question.
<br>
public void createMultipleChoiceQuestion(int courseNumber, int quizNumber, String questionName,int pointValue,int numChoices,ArrayList<String>answerChoices,int correctAnswerIndex) - create Multiple Choice Question.
<br>
private void updateMultipleChoiceQuestion(int courseNumber, int quizNumber, int questionNumber, String questionName, int pointValue, int numChoices, ArrayList<String> answerChoices, int correctAnswerIndex) - update multiple choice questions.
<br>
private void createFillInTheBlankQuestion(int courseNumber, int quizNumber, String questionName, int pointValue, String answer) - create Fill In The Blank questions.
<br>
private void updateFillInTheBlank(int courseNumber,int quizNumber,int questionNumber, String questionName,int pointValue, String answer) - update Fill In The Blank Questions.

<br>
<br>
Project Planning:
<br>
<br>
Louis' role:
<br>
handles concurrency part which allows multiple users to use the application at the same time.
<br>
<br>
Model (Jay):
<br>
handles data when called from Server
<br>
need to update existing data structures for compatibility
<br>
helped with client and server
<br>
<br>
Server (Nathan S):
<br>
while loop checks for new Clients and creates threads when they do
<br>
in thread, while loop checks for updates from Client or Model
<br>
with Client, establish some sort of communication protocol (maybe the first object sent should be a String naming the action, then the last object should be the String "STOP")
<br>
note: when communicating with Clients, this should send the fields (eg Strings of names, etc) of the data structures rather than the Course/Quiz/User objects themselves
<br>
<br>
Client (Aditya):
<br>
while loop checks for updates from Server or View
<br>
with Server, establish some sort of communication protocol (maybe the first object sent should be a String naming the action, then the last object should be the String "STOP")
<br>
<br>
View (Nathan R):
<br>
listens for user updates and send them to Client
<br>
updates when Client receives updates
<br>
