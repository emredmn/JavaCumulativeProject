import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        IQuiz quiz = new QuizManager();

        // 2️⃣ Soru 1: Multiple choice
        IQuestion q1 = new Question("Which of these languages run on the JVM?", QuestionType.MULTIPLE_CHOICE);
        IAnswer a1 = new Answer("Java", true);
        IAnswer a2 = new Answer("Kotlin", true);
        IAnswer a3 = new Answer("Python", false);
        q1.addOption(a1);
        q1.addOption(a2);
        q1.addOption(a3);
        quiz.addQuestion(q1);

        // 3️⃣ Soru 2: Open-ended
        IQuestion q2 = Question.openEnded("What is the satellite of Earth?", List.of("moon", "Moon", "ay"));
        quiz.addQuestion(q2);

        System.out.println("Welcome to the Quiz!");
        System.out.println("--------------------");

        int score = 0;

        // 4️⃣ Tüm soruları sırayla kullanıcıya sor
        for (IQuestion question : quiz.getQuestions()) {
            System.out.println("\nQuestion: " + question.getText());

            if (question.getType() != QuestionType.OPEN_ENDED) {
                // Şıklarını göster
                List<IAnswer> options = ((Question) question).getOptions();
                for (int i = 0; i < options.size(); i++) {
                    System.out.println((i + 1) + ". " + options.get(i).getText());
                }

                System.out.print("Enter your choice (separate multiple answers with comma): ");
                String input = scanner.nextLine();
                String[] parts = input.split(",");
                List<String> chosenIds = new ArrayList<>();
                for (String p : parts) {
                    try {
                        int index = Integer.parseInt(p.trim()) - 1;
                        if (index >= 0 && index < options.size()) {
                            chosenIds.add(options.get(index).getId());
                        }
                    } catch (NumberFormatException ignored) { }
                }

                boolean correct = question.checkAnswersById(chosenIds);
                if (correct) {
                    System.out.println("✅ Correct!");
                    score++;
                } else {
                    System.out.println("❌ Wrong! Correct answers: " + question.getCorrectAnswerTexts());
                }

            } else {
                // Açık uçlu soru
                System.out.print("Your answer: ");
                String answer = scanner.nextLine();

                boolean correct = question.checkOpenEndedAnswer(answer);
                if (correct) {
                    System.out.println("✅ Correct!");
                    score++;
                } else {
                    System.out.println("❌ Wrong! Correct answer: " + question.getCorrectAnswerTexts());
                }
            }
        }

        System.out.println("\n--------------------");
        System.out.println("Your final score: " + score + " / " + quiz.getTotalQuestions());
        System.out.println("Thanks for playing!");
    }
}
