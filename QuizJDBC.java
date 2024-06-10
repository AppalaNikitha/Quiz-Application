import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

 class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quiz";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
 

class QuizQuestion {
    private int question_id;
    private String question_text;
    private List<String> options;
    private String correct_option;

    // Constructor without questionId
    public QuizQuestion(String question_text, List<String> options, String correct_option) {
        this.question_text = question_text;
        this.options = options;
        this.correct_option = correct_option;
    }

    // Constructor with questionId
    public QuizQuestion(int question_id, String question_text, List<String> options, String correct_option) {
        this.question_id = question_id;
        this.question_text = question_text;
        this.options = options;
        this.correct_option = correct_option;
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
    public String getCorrectOption() {
        return correct_option;
    }

    // Setter for correctOption
    public void setCorrectOption(String correct_option) {
        this.correct_option = correct_option;
    }
}



 class QuizDAO {
    public List<QuizQuestion> getQuizQuestions() {
        List<QuizQuestion> quizQuestions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT question_id, question_text, option1, option2, option3, option4, correct_option " +
                           "FROM ques";

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int question_id = rs.getInt("question_id");
                String question_text = rs.getString("question_text");
                List<String> options = new ArrayList<>();
                options.add(rs.getString("option1"));
                options.add(rs.getString("option2"));
                options.add(rs.getString("option3"));
                options.add(rs.getString("option4"));
                String correct_option = rs.getString("correct_option");

                QuizQuestion quizQuestion = new QuizQuestion(question_id, question_text, options, correct_option);
                quizQuestions.add(quizQuestion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizQuestions;
    }
}



public class QuizJDBC {
    public static void main(String[] args) {
        QuizDAO quizDAO = new QuizDAO();
        List<QuizQuestion> quizQuestions = quizDAO.getQuizQuestions();

        Scanner scanner = new Scanner(System.in);
        int score = 0;

        for (QuizQuestion question : quizQuestions) {
            System.out.println(question.getQuestionText());
            List<String> options = question.getOptions();

            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i));
            }

            System.out.print("Enter your answer (1/2/3/4): ");
            int userAnswerIndex = scanner.nextInt() - 1;
            String userAnswer = options.get(userAnswerIndex);

            if (userAnswer.equals(question.getCorrectOption())) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Wrong!");
            }
        }

        System.out.println("Quiz completed! Your score: " + score + "/" + quizQuestions.size());
    }
}