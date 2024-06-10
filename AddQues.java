import java.sql.*;
public class AddQues
{
    //Class.forName("com.mysql.cj.jdbc.Driver");
    public void creating(){
        Connection con;
        try{
            //Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz1","root","root");
            Statement stmt=con.createStatement();
            String sql = "CREATE TABLE quiz_questions (" +
                            "id INTEGER PRIMARY KEY," +
                            "question varchar(400) NOT NULL," +
                            "option1 varchar(100) NOT NULL," +
                            "option2 varchar(100) NOT NULL," +
                            "option3 varchar(100) NOT NULL," +
                            "option4 varchar(100) NOT NULL," +
                            "correct_option INTEGER NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            con.close();
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    public void inserting(){
        Connection con;
        try{
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz1","root","root");
            Statement stmt=con.createStatement();
            String query1 = "INSERT INTO quiz_questions (id, question, option1, option2, option3, option4, correct_option) VALUES " +
                            "(1, 'JDK stands for ____.', " +
                            "'Java development kit', " +
                            "'Java deployment kit', " +
                            "'JavaScript deployment kit', " +
                            "'None of these', 1), " +
                            "(2, 'JRE stands for ___.', " + 
                            "'Java run ecosystem', " +
                            "'JDK runtime Environment', " +
                            "'Java Runtime Environment', " +
                            "'None of these', 3), " +
                            "(3, 'What makes the Java platform independent?', " +
                            "'Advanced programming language', " +
                            "'It uses bytecode for execution', " +
                            "'Class compilation', " +
                            "'All of these', 2), " +
                            "(4, 'Can we keep a different name for the java class name and java file name?', " +
                            "'Yes', " +
                            "'No', " +
                            "'Both A and B', " +
                            "'None of these', 1), " +
                            "(5, 'What are the types of memory allocated in memory in java?', " +
                            "'Heap memory', " +
                            "'Stack memory', " +
                            "'Both A and B', " +
                            "'None of these', 3)";
            stmt.executeUpdate(query1);
            stmt.close();
            con.close();
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    public void result_Data(){
        Connection con;
        try{
            //Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz1","root","root");
            Statement stmt=con.createStatement();
            String sql = "CREATE TABLE quiz_results (" +
                            "id VARCHAR(20) PRIMARY KEY NOT NULL," +
                            "Name varchar(20) NOT NULL," +
                            "Score int NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            con.close();
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        AddQues q=new AddQues();
        //q.creating();
        //q.inserting();
        q.result_Data();
    }
}

