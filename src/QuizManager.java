import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizManager implements IQuiz {
    private final List<IQuestion> questions = new ArrayList<>();

    @Override
    public void addQuestion(IQuestion question) {
        questions.add(question);
    }

    @Override
    public List<IQuestion> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    @Override
    public int getTotalQuestions() {
        return questions.size();
    }
}
