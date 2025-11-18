import java.util.*;
import java.util.stream.Collectors;

public class Question<T> implements IQuestion<T> {
    private final String text;
    private final QuestionType type;
    private final List<IAnswer<T>> options = new ArrayList<>();
    private final Set<T> acceptedOpenAnswers = new HashSet<>();

    public Question(String text, QuestionType type) {
        this.text = text;
        this.type = type;
    }

    public static Question<String> openEnded(String text, Collection<String> accepted) {
        Question<String> q = new Question<>(text, QuestionType.OPEN_ENDED);
        for (String s : accepted)
            q.acceptedOpenAnswers.add(s.trim().toLowerCase());
        return q;
    }

    @Override
    public void addOption(IAnswer<T> answer) {
        options.add(answer);
    }

    @Override
    public boolean checkAnswers(Collection<IAnswer<T>> chosen) {
        if (type == QuestionType.OPEN_ENDED) throw new IllegalStateException("Use checkOpenEnded for open-ended question");

        Set<T> correct = options.stream()
                .filter(IAnswer::isCorrect)
                .map(IAnswer::getValue)
                .collect(Collectors.toSet());

        Set<T> selected = chosen.stream()
                .map(IAnswer::getValue)
                .collect(Collectors.toSet());

        return correct.equals(selected);
    }

    @Override
    public boolean checkOpenEnded(T answer) {
        return acceptedOpenAnswers.contains(
                answer.toString().trim().toLowerCase()
        );
    }

    @Override
    public List<IAnswer<T>> getCorrectAnswers() {
        return options.stream()
                .filter(IAnswer::isCorrect)
                .collect(Collectors.toList());
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public QuestionType getType() {
        return type;
    }

    public List<IAnswer<T>> getOptions() {
        return Collections.unmodifiableList(options);
    }
}
