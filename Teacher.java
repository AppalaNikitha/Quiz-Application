import java.util.List;
import java.sql.*;

class Teacher {
    public void addQuizQuestion(QuizQuestion quizQuestion) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt1 = conn.createStatement();

            String query = "SELECT COUNT(*) FROM quiz_questions";
            int questionCount = 0;
            ResultSet rs = stmt1.executeQuery(query);
            if (rs.next()) {
                questionCount = rs.getInt(1);
            }
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO quiz_questions (id,question, option1, option2, option3, option4, correct_option) VALUES (?,?, ?, ?, ?, ?, ?)");
            stmt.setInt(1,questionCount+1);
            stmt.setString(2, quizQuestion.getQuestionText());
            List<String> options = quizQuestion.getOptions();
            for (int i = 0; i < 4; i++) {
                stmt.setString(i + 3, options.get(i));
            }
            stmt.setInt(7, quizQuestion.getCorrectOption());

            stmt.executeUpdate();
            System.out.println("Quiz question added successfully!");
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateQuizQuestion(int questionId, QuizQuestion updatedQuestion) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE quiz_questions SET question=?, option1=?, option2=?, option3=?, option4=?, correct_option=? WHERE id=?");

            stmt.setString(1, updatedQuestion.getQuestionText());
            List<String> options = updatedQuestion.getOptions();
            for (int i = 0; i < 4; i++) {
                stmt.setString(i + 2, options.get(i));
            }
            stmt.setInt(6, updatedQuestion.getCorrectOption());
            stmt.setInt(7, questionId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Quiz question updated successfully!");
            } else {
                System.out.println("No quiz question found with ID: " + questionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteQuizQuestion(int questionId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM quiz_questions WHERE id=?");
            stmt.setInt(1, questionId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Quiz question deleted successfully!");
            } else {
                System.out.println("No quiz question found with ID: " + questionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getStudentMarksById(String studentId) {
        try {
            QuizDAO quizDAO = new QuizDAO();
            List<QuizQuestion> quizQuestions = quizDAO.getQuizQuestions();
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT Name, Score FROM quiz_results WHERE id=?");
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                String studentName = rs.getString("Name");
                int score = rs.getInt("Score");
                System.out.printf("Student ID: %s\n", studentId);
                System.out.printf("Student Name: %s\n", studentName);
                System.out.printf("Marks Scored: %d/%d\n", score, quizQuestions.size());
            } else {
                System.out.println("No quiz result found for ID: " + studentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    public void printStudentsOrderByMarks() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT id, Name, Score FROM quiz_results ORDER BY Score DESC");
            ResultSet rs = stmt.executeQuery();

            System.out.println("Students in order of highest marks:");
            System.out.println("------------------------------------");
            while (rs.next()) {
                String studentId = rs.getString("id");
                String studentName = rs.getString("Name");
                int score = rs.getInt("Score");
                System.out.printf("Student ID: %s, Name: %s, Marks Scored: %d\n", studentId, studentName, score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}