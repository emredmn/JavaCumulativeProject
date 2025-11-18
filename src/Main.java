import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        QuizManager<Question<String>> quiz = new QuizManager<>();

        // Soru 1 (Multiple Choice)
        Question<String> q1 = new Question<>("Which languages run on the JVM?", QuestionType.MULTIPLE_CHOICE);
        Answer<String> a1 = new Answer<>("Java", true);
        Answer<String> a2 = new Answer<>("Kotlin", true);
        Answer<String> a3 = new Answer<>("Python", false);
        q1.addOption(a1);
        q1.addOption(a2);
        q1.addOption(a3);

        // Soru 2 (Open-ended)
        Question<String> q2 = Question.openEnded("What is the satellite of Earth?", List.of("moon", "ay"));

        quiz.addQuestion(q1);
        quiz.addQuestion(q2);

        int score = 0;
        System.out.println("---- GENERIC QUIZ ----");

        for (IQuestion<String> question : quiz.getQuestions()) {
            System.out.println("\n" + question.getText());

            if (question.getType() != QuestionType.OPEN_ENDED) {
                List<IAnswer<String>> options = ((Question<String>) question).getOptions();
                for (int i = 0; i < options.size(); i++) {
                    System.out.println((i + 1) + ". " + options.get(i).getValue());
                }

                System.out.print("Your answer(s): ");
                String input = scanner.nextLine();
                String[] parts = input.split(",");
                List<IAnswer<String>> chosen = new java.util.ArrayList<>();
                for (String p : parts) {
                    int index = Integer.parseInt(p.trim()) - 1;
                    if (index >= 0 && index < options.size()) {
                        chosen.add(options.get(index));
                    }
                }

                if (question.checkAnswers(chosen)) {
                    System.out.println("✅ Correct!");
                    score++;
                } else {
                    System.out.println("❌ Wrong. Correct: " + question.getCorrectAnswers());
                }
            } else {
                System.out.print("Your answer: ");
                String ans = scanner.nextLine();
                if (question.checkOpenEnded(ans)) {
                    System.out.println("✅ Correct!");
                    score++;
                } else {
                    System.out.println("❌ Wrong. Correct: " + question.getCorrectAnswers());
                }
            }
        }

        System.out.println("\nYour score: " + score + " / " + quiz.getQuestions().size());
    }
}
