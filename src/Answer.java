public class Answer<T> implements IAnswer<T> {
    private final T value;
    private final boolean correct;

    public Answer(T value, boolean correct) {
        this.value = value;
        this.correct = correct;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public boolean isCorrect() {
        return correct;
    }

    @Override
    public String toString() {
        return value + (correct ? " (correct)" : "");
    }
}
