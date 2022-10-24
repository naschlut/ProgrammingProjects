# CS180-Project-4
Group 087's Project 4 for CS18000
<br>
Option chosen - Option 2: Quizzes
<br>
<br>
Louis: Files (+help with randomizations in Quizzes)
<br>
Aditya: Accounts
<br>
Nathan R: Menu
<br>
Nathan S: Quizzes and Question types
<br>
Jay: Submission, File handling (CourseList, StudentList, TeacherList), Course, Quiz (just the reading from file)
<br>
<br>
information on the structure of the program and division of roles found in image file which Nathan R will upload
<br>
<br>
Both Project and Project Report will be submitted by Nathan Reed
<br>
<br>
Classes
<br>
********
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









