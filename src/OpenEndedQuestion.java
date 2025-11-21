import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class OpenEndedQuestion extends AbstractQuestion {
    private final Set<String> acceptedAnswers = new HashSet<>();

    public OpenEndedQuestion(String text, Collection<String> accepted) {
        super(text);
        for (String s : accepted) {
            acceptedAnswers.add(s.trim().toLowerCase());
        }
    }

    @Override
    public boolean ask(Scanner scanner) {
        System.out.println("\n" + getText() + " (Type your answer)");
        System.out.print("Your answer: ");
        String input = scanner.nextLine();

        boolean isCorrect = acceptedAnswers.contains(input.trim().toLowerCase());

        if (isCorrect) {
            System.out.println("✅ Correct!");
        } else {
            System.out.println("❌ Wrong.");
        }
        return isCorrect;
    }

    // Added for serialization
    public Set<String> getAcceptedAnswers() {
        return Collections.unmodifiableSet(acceptedAnswers);
    }
}