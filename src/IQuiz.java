import java.util.List;

// DİKKAT: IQuestion<?> yerine sadece IQuestion yazıyoruz.
public interface IQuiz<Q extends IQuestion> {
    void addQuestion(Q question);

    List<Q> getQuestions();
}