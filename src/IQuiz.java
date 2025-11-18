import java.util.List;

public interface IQuiz {
    void addQuestion(IQuestion question);
    List<IQuestion> getQuestions();
    int getTotalQuestions();
}
