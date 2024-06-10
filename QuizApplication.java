import java.util.*;
public class QuizApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("***************************");
            System.out.println("WELCOME TO QUIZ APPLICATION");
            System.out.println("***************************");
            System.out.println("");
            System.out.println("================================");
            System.out.println("Are you a Teacher or a Student?");
            System.out.println("================================");
            System.out.println("---------------------------------");
            System.out.println("1. Teacher");
            System.out.println("2. Student");
            System.out.println("3. Exit Quiz Application");
            System.out.println("---------------------------------");
            System.out.print("Enter your choice (1/2/3): ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                System.out.println("Enter the password:");
                String password=scanner.next();
                Teacher teacher = new Teacher();
                if (password.equals("root")) 
                {
                    while(true)
                    {
                        System.out.println("");
                        System.out.println("Choose an option:");
                        System.out.println("1. Add a quiz question");
                        System.out.println("2. Update a quiz question");
                        System.out.println("3. Delete a quiz question");
                        System.out.println("4. See student marks");
                        System.out.println("5. All Students Progress");
                        System.out.println("6.Exit Teacher Field");
                        System.out.print("Enter your choice (1/2/3/4/5/6): ");
                        int option = scanner.nextInt();
                        switch (option)
                        {
                            case 1:
                                scanner.nextLine(); // Consume the newline left by nextInt()
                                System.out.print("Enter the question text: ");
                                String questionText = scanner.nextLine();
                                List<String> options = new ArrayList<>();
                                for (int i = 1; i <= 4; i++) {
                                    System.out.print("Enter option " + i + ": ");
                                    String optionText = scanner.nextLine();
                                    options.add(optionText);
                                }
                                System.out.print("Enter the correct option index (1/2/3/4): ");
                                int correctOption = scanner.nextInt();
                                QuizQuestion newQuestion = new QuizQuestion(questionText, options, correctOption);
                                teacher.addQuizQuestion(newQuestion);
                                break;

                            case 2:
                                System.out.print("Enter the question ID to update: ");
                                int questionIdToUpdate = scanner.nextInt();
                                scanner.nextLine(); // Consume the newline left by nextInt()
                                System.out.print("Enter the updated question text: ");
                                String updatedQuestionText = scanner.nextLine();
                                List<String> updatedOptions = new ArrayList<>();
                                for (int i = 1; i <= 4; i++) 
                                {
                                    System.out.print("Enter updated option " + i + ": ");
                                    String updatedOptionText = scanner.nextLine();
                                    updatedOptions.add(updatedOptionText);
                                }
                                System.out.print("Enter the updated correct option index (1/2/3/4): ");
                                int updatedCorrectOption = scanner.nextInt();
                                QuizQuestion updatedQuestion = new QuizQuestion(questionIdToUpdate, updatedQuestionText, updatedOptions, updatedCorrectOption);
                                teacher.updateQuizQuestion(questionIdToUpdate,updatedQuestion);
                                break;

                            case 3:
                                System.out.print("Enter the question ID to delete: ");
                                int questionIdToDelete = scanner.nextInt();
                                teacher.deleteQuizQuestion(questionIdToDelete);
                                break;

                            case 4:
                                System.out.print("Enter the student's ID to see marks: ");
                                String studentId = scanner.next();
                                teacher.getStudentMarksById(studentId);
                                break;
                            case 5:
                                System.out.println("Displaying all Students marks who attempted");
                                teacher.printStudentsOrderByMarks();
                                break;
                            case 6:
                                System.out.println("Exiting... Goodbye!");
                                break;

                            default:
                                System.out.println("Invalid option. Please enter a valid choice (1/2/3/4).");
                                break;
                        }
                        if (option == 6) {
                            break; // Exit the teacher menu loop and go back to the main loop
                        }
                    }
                } 
                else {
                    System.out.println("Invalid password. Access denied.");
                }
            // Take input for quiz question and options
            } 
            else if (choice == 2) 
            {
               String password="student";
               System.out.println("Enter Password");
               String p=scanner.next();
               if(p.equals(password))
               {
                    QuizDAO quizDAO = new QuizDAO();
                    List<QuizQuestion> quizQuestions = quizDAO.getQuizQuestions();

                    int timeLimitSeconds = 120; // 2 minutes

                    System.out.print("Enter your ID: ");
                    String id=scanner.next();
                    
                    System.out.print("Enter your username: ");
                    String username = scanner.next();

                    QuizResultDAO quizResultDAO = new QuizResultDAO(); // Update the variable name here
                    QuizResult previousResult = quizResultDAO.getQuizResultById(id); // Update the variable name here
                
                    
                    if (previousResult != null) {
                        System.out.println("You have already attempted the quiz.");
                        System.out.println("Your previous score: " + previousResult.getScore());
                        System.exit(0); // Exit the application since the user has already attempted the quiz.
                    }
                    int score = 0;
                    for (QuizQuestion question : quizQuestions) 
                    {
                        System.out.println(question.getQuestionText());
                        List<String> options = question.getOptions();
                        for (int i = 0; i < options.size(); i++) 
                        {
                            System.out.println((i + 1) + ". " + options.get(i));
                        }
                        System.out.print("Enter your answer (1/2/3/4): ");
                        int userAnswerIndex = scanner.nextInt()-1;
                        int correctAnswer = question.getCorrectOption();
                        // Get the user's answer using the user's answer index
                        if (userAnswerIndex >= 0 && userAnswerIndex < options.size()) {
                            if (userAnswerIndex+1==correctAnswer) {
                                System.out.println("Correct!");
                                score++;
                                TimeLimitThread timeLimitThread = new TimeLimitThread(timeLimitSeconds, id, username, score, quizQuestions, Thread.currentThread());
                                timeLimitThread.start();
                            }
                            else {
                                System.out.println("Wrong!");
                            }
                            question.setAttempted(true);
                            System.out.println("");
                        }
                        else {
                            System.out.println("Invalid answer choice. Please choose a valid option (1/2/3/4).");
                        }
                    }
                    TimeLimitThread timeLimitThread = new TimeLimitThread(timeLimitSeconds, id, username, score, quizQuestions, Thread.currentThread());
                    timeLimitThread.start();
                    QuizResult quizResult = new QuizResult(id,username,score);
                    quizResultDAO.saveQuizResult(quizResult);
                    Student print=new Student();
                    System.out.printf("Your score is: %d/%d",score,quizQuestions.size());
                    System.out.println("");
                    //print.displayCertificate(id, username, score, quizQuestions.size());
                    timeLimitThread.interrupt();
                }
            }
            else if(choice ==3){
                System.out.println("Exiting");
                System.exit(0);
            } 
            else{
                System.out.println("Invalid choice!!");
            }
            
        }
    }
}