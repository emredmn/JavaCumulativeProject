import java.util.Objects;
import java.util.UUID;

public class Answer implements IAnswer {
    private final String id;
    private final String text;
    private final boolean correct;

    public Answer(String text, boolean correct) {
        this.id = UUID.randomUUID().toString();
        this.text = Objects.requireNonNull(text).trim();
        this.correct = correct;
    }

    @Override
    public String getId() { return id; }

    @Override
    public String getText() { return text; }

    @Override
    public boolean isCorrect() { return correct; }

    @Override
    public String toString() {
        return text + (correct ? " (correct)" : "");
    }
}
