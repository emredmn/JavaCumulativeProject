import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizManager<Q extends IQuestion<?>> implements IQuiz<Q> {
    private final List<Q> questions = new ArrayList<>();

    @Override
    public void addQuestion(Q question) {
        questions.add(question);
    }

    @Override
    public List<Q> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
}
