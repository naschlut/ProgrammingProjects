Test 1: Create New Account
<br>
1) Open application
2) Enter credentials in username and password
3) Select Student or Teacher
4) Create Account
<br>
<br>
Expected Result: New account is created and user is directed to home page
<br>
Test Result: Passed
<br>

Test 2: Login with existing account
<br>
1) Open application
2) Enter credentials in username and password
3) Select Student or Teacher
4) Login
<br>
<br>
Expected Result: User is able to log in and their homepage is loaded
<br>
Test Result: Passed
<br>

Test 3: User creates account with taken credentials
<br>
1) Open application
2) Enter taken credentials in username and password
3) Select Student or Teacher
4) Create account
<br>
<br>
Expected Result: User is presented with error message informing them that those credentials are taken
<br>
Test Result: Passed
<br>

Test 4: User logs in with incorrect credentials
<br>
1) Open application
2) Enter incorrect credentials in username and password
3) Select Student or Teacher
4) Login
<br>
<br>
Expected Result: User is informed they have entered invalid credentials
<br>
Test Result: Passed
<br>

Test 5: Teacher creates course
<br>
1) Teacher logs in
2) Teacher selects create new course
3) Teacher enters course name and course number
<br>
<br>
Expected Result: Course is created
<br>
Test Result: Passed
<br>

Test 6: Teacher creates course with invalid course number
<br>
1) Teacher logs in
2) Teacher selects create new course
3) Teacher enters course name and course number
<br>
<br>
Expected Result: User is informed that their course number is taken or invalid
<br>
Test Result: Passed
<br>

Test 7: Teacher creates quiz 
<br>
1) Teacher logs in 
2) Teacher selects created course
3) Teacher selects option to create new quiz
4) Teacher enters quiz name, quiz number, randomized or not
5) Teacher enters questions to be in quiz as well as point values for each
<br>
<br>
Expected Result: Quiz is created under the specified course
<br>
Test Result: Passed
<br>

Test 8: Teacher creates quiz with invalid quiz number
<br>
1) Teacher logs in
2) Teacher selects created course
3) Teacher selects option to create new quiz
4) Teacher enters quiz name and invalid quiz number, randomized or not
<br>
<br>
Expected Result: User receives error message that quiz number is taken or invalid
<br>
Test Result: Passed
<br>

Test 9: Teacher creates quiz through file import
<br>
1) Teacher logs in
2) Teacher selects created course
3) Teacher selects option to create new quiz from file import
4) Teacher enters filepath
<br>
<br>
Expected Result: Quiz is created
<br>
Test Result: Passed
<br>

Test 10: Teacher creates quiz with invalid filepath/invalid file format
<br>
1) Teacher logs in
2) Teacher selects created course
3) Teacher selects option to create new quiz from invalid filepath/invalid file format
4) Teacher enters filepath
<br>
<br>
Expected Result: User receives error message that the filepath or the file format is incorrect
<br>
Test Result: Passed
<br>

Test 11: Teacher enters invalid point value when creating questions for quiz
<br>
1) Teacher logs in
2) Teacher selects created course
3) Teacher selects option to create new quiz
4) Teacher enters quiz name and quiz number, randomized or not
5) Teacher creates question and enters an invalid point value
<br>
<br>
Expected Result: User receives error message prompting them to re-enter the point value correctly
<br>
Test Result: Passed
<br>

Test 12: Teacher enters invalid value when choosing to randomize quiz or not
<br>
1) Teacher logs in
2) Teacher selects created course
3) Teacher selects option to create new quiz
4) Teacher enters quiz name, quiz number, invalid value for randomization
<br>
<br>
Expected Result: User receives error message prompting them to re-enter the value for randomization correctly
<br>
Test Result: Passed
<br>

Test 13: Teacher deletes quiz
<br>
1) Teacher logs in 
2) Teacher selects created course
3) Teacher selects created quiz
4) Teacher selects option to delete quiz
<br>
<br>
Expected Result: Quiz is deleted
<br>
Test Result: Passed
<br>

Test 14: Teacher deletes question in quiz
<br>
1) Teacher logs in
2) Teacher selects created course
3) Teacher selects created quiz
4) Teacher selects option to edit quiz
5) Teacher deletes question
<br>
<br>
Expected Result: Question is deleted
<br>
Test Result: Passed
<br>

Test 15: Teacher edits question title
<br>
1) Teacher logs in
2) Teacher selects created course
3) Teacher selects created quiz
4) Teacher selects option to edit quiz
5) Teacher changes name of question
<br>
<br>
Expected Result: Question name is changed
<br>
Test Result: Passed
<br>

Test 16: Teacher edits answer/answer choices to question
<br>
1) Teacher logs in
2) Teacher selects created course
3) Teacher selects created quiz
4) Teacher selects option to edit quiz
5) Teacher edits answer/answer choices to question
<br>
<br>
Expected Result: Answer/answer choices to question are changed
<br>
Test Result: Passed
<br>

Test 17: Teacher edits point value of question
<br>
1) Teacher logs in
2) Teacher selects created course
3) Teacher selects created quiz
4) Teacher selects option to edit quiz
5) Teacher edits point value of question
<br>
<br>
Expected Result: Point value of question is changed
<br>
Test Result: Passed
<br>

Test 18: Teacher edits point value of question with invalid value
<br>
1) Teacher logs in
2) Teacher selects created course
3) Teacher selects created quiz
4) Teacher selects option to edit quiz
5) Teacher edits point value of question with invalid value
<br>
<br>
Expected Result: User receives error message prompting them to re-enter point value correctly
<br>
Test Result: Passed
<br>

Test 19: Teacher views submissions for course
<br>
1) Teacher logs in
2) Teacher selects created course
3) Teacher selects created quiz
4) Teacher selects view submissions
<br>
<br>
Expected Result: User is able to view all student submissions for selected quiz
<br>
Test Result: Passed
<br>

Test 20: Student adds course
<br>
1) Student logs in
2) Student selects add course
3) Student selects course to add
<br>
<br>
Expected Result: User adds course to the list of courses they are taking
<br>
Test Result: Passed
<br>

Test 21: Student takes quiz manually
<br>
1) Student logs in 
2) Student selects course
3) Student selects quiz
4) Student chooses to take quiz manually
5) Student enters answers to each question
<br>
<br>
Expected Result: Student takes quiz and submission report is created
<br>
Test Result: Passed
<br>

Test 22: Student takes quiz manually and enters invalid value when answering
<br>
1) Student logs in 
2) Student selects course
3) Student selects quiz
4) Student chooses to take quiz manually
5) Student enters invalid value to answer question
<br>
<br>
Expected Result: User receives error message prompting them to re-enter value correctly
<br>
Test Result: Passed
<br>

Test 23: Student submits answers to quiz through file
<br>
1) Student logs in
2) Student selects course
3) Student selects quiz
4) Student chooses to answer quiz with file
5) Student enters filepath
<br>
<br>
Expected Result: Answers are processed and submission report is created
<br>
Test Result: Passed
<br>

Test 24: Student submits answers to quiz through invalid file/filepath
<br>
1) Student logs in
2) Student selects course
3) Student selects quiz
4) Student chooses to answer quiz with file
5) Student enters invalid filepath/file with invalid format
<br>
<br>
Expected Result: User receives error message informing them that the filepath or the format of the file is incorrect
<br>
Test Result: Passed
<br>

Test 25: Student views submission
<br>
1) Student logs in
2) Student selects course
3) Student selects quiz
4) Student selects view submissions
<br>
<br>
Expected Result: Student is able to view all their submissions as well as grades on each attempt
<br>
Test result: Passed
<br>
