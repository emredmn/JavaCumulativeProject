import java.util.List;

public interface IQuiz<Q extends IQuestion<?>> {
    void addQuestion(Q question);
    List<Q> getQuestions();
}
