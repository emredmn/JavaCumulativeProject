import java.io.IOException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        QuizFileHandler fileHandler = new QuizFileHandler();
        String filename = "quiz_data.json";

        try {
            System.out.println("Loading quiz from " + filename + "...");
            QuizManager<IQuestion> quiz = fileHandler.loadQuiz(filename);

            if (quiz.getQuestions().isEmpty()) {
                System.out
                        .println("No questions found in " + filename + ". Please add some questions to the JSON file.");
                return;
            }

            System.out.println("Quiz loaded successfully! Starting quiz...\n");

            int score = 0;
            System.out.println("---- GENERIC QUIZ ----");

            for (IQuestion question : quiz.getQuestions()) {
                if (question.ask(scanner)) {
                    score++;
                }
            }

            System.out.println("\nYour score: " + score + " / " + quiz.getQuestions().size());

        } catch (IOException e) {
            System.err.println("Error loading quiz file: " + e.getMessage());
            System.err.println("Make sure " + filename + " exists and is valid JSON.");
        }
    }
}