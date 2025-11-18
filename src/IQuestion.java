import java.util.Collection;
import java.util.List;

public interface IQuestion {
    String getId();
    String getText();
    QuestionType getType();

    void addOption(IAnswer answer);
    boolean checkAnswersById(Collection<String> chosenIds);
    boolean checkOpenEndedAnswer(String answer);
    List<String> getCorrectAnswerTexts();
}
