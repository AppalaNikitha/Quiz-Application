import java.util.List;
import java.util.*;
import java.sql.*;
import java.util.ArrayList;

class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quiz1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
 
class QuizResult {
    private String id;
    private String username;
    private int score;

    public QuizResult(String id, String username, int score) {
        this.id = id;
        this.username = username;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
class QuizResultDAO {
    public void saveQuizResult(QuizResult quizResult) {
        try{
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO quiz_results (id,Name,Score) VALUES (?,?,?)");
            stmt.setString(1, quizResult.getId());
            stmt.setString(2, quizResult.getUsername());
            stmt.setInt(3, quizResult.getScore());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public QuizResult getQuizResultById(String id) 
    {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM quiz_results WHERE id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("Name");
                int score = rs.getInt("Score");
                return new QuizResult(id, username, score);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if no previous result is found
    }
}
class QuizQuestion {
    private int question_id;
    private String question_text;
    private List<String> options;
    private int correct_option;
    private volatile boolean attempted;
    public boolean isAttempted() {
        return attempted;
    }


    // Setter for attempted
    public void setAttempted(boolean attempted) {
        this.attempted = attempted;
    }

    // Constructor without questionId
    public QuizQuestion(String question_text, List<String> options, int correct_option) {
        this.question_text = question_text;
        this.options = options;
        this.correct_option = correct_option;
    }

    // Constructor with questionId
    public QuizQuestion(int question_id, String question_text, List<String> options, int correct_option) {
        this.question_id = question_id;
        this.question_text = question_text;
        this.options = options;
        this.correct_option = correct_option;
        this.attempted = false;
    }

    // Getter for questionId
    public int getQuestionId() {
        return question_id;
    }

    // Setter for questionId
    public void setQuestionId(int question_id) {
        this.question_id = question_id;
    }

    // Getter for questionText
    public String getQuestionText() {
        return question_text;
    }

    // Setter for questionText
    public void setQuestionText(String question_text) {
        this.question_text = question_text;
    }

    // Getter for options
    public List<String> getOptions() {
        return options;
    }

    // Setter for options
    public void setOptions(List<String> options) {
        this.options = options;
    }

    // Getter for correctOption
    public int getCorrectOption() {
        return correct_option;
    }

    // Setter for correctOption
    public void setCorrectOption(int correct_option) {
        this.correct_option = correct_option;
    }
}



 class QuizDAO {
    public List<QuizQuestion> getQuizQuestions() {
        List<QuizQuestion> quizQuestions = new ArrayList<>();

        try{
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            String query = "SELECT id, question,option1, option2, option3, option4, correct_option " +
                           "FROM quiz_questions";

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int question_id = rs.getInt("id");
                String question_text = rs.getString("question");
                List<String> options = new ArrayList<>();
                options.add(rs.getString("option1"));
                options.add(rs.getString("option2"));
                options.add(rs.getString("option3"));
                options.add(rs.getString("option4"));
                int correct_option = rs.getInt("correct_option");

                QuizQuestion quizQuestion = new QuizQuestion(question_id, question_text, options, correct_option);
                quizQuestions.add(quizQuestion);
            }
            Collections.shuffle(quizQuestions);
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return quizQuestions;
    }
}
class TimeLimitThread extends Thread {
    private int timeLimitSeconds;
    private String studentId;
    private String username;
    private int score;
    private List<QuizQuestion> quizQuestions;
    private volatile Thread mainThread;

    public TimeLimitThread(int timeLimitSeconds, String studentId, String username, int score, List<QuizQuestion> quizQuestions, Thread mainThread) {
        this.timeLimitSeconds = timeLimitSeconds;
        this.studentId = studentId;
        this.username = username;
        this.score = score;
        this.quizQuestions = quizQuestions;
        this.mainThread = mainThread;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeLimitSeconds * 800);
            System.out.println("\nTime's up! Quiz completed.");
            mainThread.interrupt();
            printScoreAndExit();
        } catch (InterruptedException e) {
            
        }
    }

    private void printScoreAndExit() {
        Student student = new Student();
        student.displayCertificate(studentId, username, score, quizQuestions.size());
        QuizResultDAO quizResultDAO = new QuizResultDAO();
        QuizResult quizResult = new QuizResult(studentId, username, score);
        quizResultDAO.saveQuizResult(quizResult);
        System.exit(0);
    }
}


class Student 
{
    public void displayCertificate(String studentId, String username, int score, int totalQuestions) 
    {
        QuizDAO quizDAO = new QuizDAO();
        List<QuizQuestion> quizQuestions = quizDAO.getQuizQuestions();
        double percentile = (double) score / totalQuestions * 100;

        // Calculate grade based on percentile
        char grade;
        if (percentile >= 90) {
            grade = 'A';
        } else if (percentile >= 80) {
            grade = 'B';
        } else if (percentile >= 70) {
            grade = 'C';
        } else if (percentile >= 60) {
            grade = 'D';
        } else {
            grade = 'F';
        }

        System.out.println("\nCongratulations! Here's your Quiz Certificate:");
        System.out.println("===================================================");
        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + username);
        System.out.println("Marks Scored: " + score + "/" + totalQuestions);
        System.out.printf("Percentile: %.2f%%\n", percentile);
        System.out.println("Grade: " + grade);
        System.out.println("Quiz Summary:");
        System.out.println("------------");
        for (QuizQuestion question : quizQuestions) {
            System.out.println(question.getQuestionText());
            System.out.println("Correct Option: " + question.getCorrectOption() + "\n");
        }
        System.out.println("===================================================");

    }
    
}