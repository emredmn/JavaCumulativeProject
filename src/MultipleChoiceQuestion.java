import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MultipleChoiceQuestion<T> extends AbstractQuestion {
    private final List<IAnswer<T>> options = new ArrayList<>();

    public MultipleChoiceQuestion(String text) {
        super(text);
    }

    public void addOption(IAnswer<T> option) {
        options.add(option);
    }

    @Override
    public boolean ask(Scanner scanner) {
        System.out.println("\n" + getText() + " (Multiple Choice)");

        // Seçenekleri yazdır
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i).getValue());
        }

        System.out.print("Your answer(s) (e.g., 1,2): ");
        String input = scanner.nextLine();

        // Girdiyi işle
        String[] parts = input.split(",");
        List<IAnswer<T>> chosen = new ArrayList<>();

        for (String p : parts) {
            try {
                int index = Integer.parseInt(p.trim()) - 1;
                if (index >= 0 && index < options.size()) {
                    chosen.add(options.get(index));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input ignored: " + p);
            }
        }

        // Kontrol et
        boolean isCorrect = checkAnswers(chosen);
        if (isCorrect) {
            System.out.println("✅ Correct!");
        } else {
            System.out.println("❌ Wrong.");
        }
        return isCorrect;
    }

    // Kendi iç mantığı (Eski checkAnswers metodu)
    private boolean checkAnswers(List<IAnswer<T>> chosen) {
        // Basitçe: Doğru olanların sayısı ve içeriği, seçilenlerle aynı mı?
        List<T> correctValues = options.stream()
                .filter(IAnswer::isCorrect)
                .map(IAnswer::getValue)
                .toList();

        List<T> chosenValues = chosen.stream()
                .map(IAnswer::getValue)
                .toList();

        return correctValues.size() == chosenValues.size() &&
                correctValues.containsAll(chosenValues);
    }

    // Added for serialization
    public List<IAnswer<T>> getOptions() {
        return Collections.unmodifiableList(options);
    }
}