import java.io.*;
import java.util.*;

public class MyQuizApplication {
    public static void main(String args[]) throws IOException {
        int i = 0;
        String s = "", s1 = "";
        int score = 0;
        char ch, ch1;
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        // Set a time limit of 60 minutes (in milliseconds)
        long timeLimit = 60 * 60 * 1000;
        long startTime = System.currentTimeMillis();

        try (FileReader f = new FileReader("./Question.txt");
             BufferedReader bf = new BufferedReader(f);
             FileReader f1 = new FileReader("./Answer.txt");
             BufferedReader bf1 = new BufferedReader(f1)) {

            while (true) {
                s = bf.readLine(); // reading the question
                if (s == null)
                    break;
                System.out.println(s);

                for (i = 0; i < 4; i++) {
                    s = bf.readLine(); // reading options a, b, c, d from the question file
                    if (s == null)
                        break;
                    System.out.println(s);
                }

                if (s == null)
                    break; // Break out of the loop if the options are null

                System.out.println("Enter the correct option (a, b, c, d)"); // taking answer as input
                ch = sc.next().charAt(0);

                s1 = bf1.readLine(); // reading the answer from the answer file
                if (s1 == null)
                    break; // Break out of the loop if the answer is null

                ch1 = s1.charAt(0);
                if (Character.toUpperCase(ch) == Character.toUpperCase(ch1)) { // comparing the input answer with the correct answer
                    System.out.println("Correct answer");
                    score++;
                } else {
                    System.out.println("Wrong answer");
                }

                // Check if time is up
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime >= timeLimit) {
                    System.out.println("Time's up!");
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Please check the file location.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Quiz Completed, dear " + name + "!");
        System.out.println("Congratulations! Your score is " + score + " out of " + (i / 5)); // displaying the score
    }
}