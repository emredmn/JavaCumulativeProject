import java.util.Collection;
import java.util.List;

public interface IQuestion<T> {
    String getText();
    QuestionType getType();

    void addOption(IAnswer<T> answer);
    boolean checkAnswers(Collection<IAnswer<T>> chosen);
    boolean checkOpenEnded(T answer);
    List<IAnswer<T>> getCorrectAnswers();
}
