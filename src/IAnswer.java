public interface IAnswer<T> {
    T getValue();

    boolean isCorrect();
}
