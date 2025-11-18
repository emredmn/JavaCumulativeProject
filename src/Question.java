import java.util.*;
import java.util.stream.Collectors;

public class Question implements IQuestion {
    private final String id;
    private String text;
    private final QuestionType type;
    private final List<IAnswer> options = new ArrayList<>();
    private final Set<String> acceptedOpenAnswers = new HashSet<>();

    public Question(String text, QuestionType type) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.type = type;
    }

    public static Question openEnded(String text, Collection<String> acceptedAnswers) {
        Question q = new Question(text, QuestionType.OPEN_ENDED);
        for (String a : acceptedAnswers) {
            q.acceptedOpenAnswers.add(a.trim().toLowerCase());
        }
        return q;
    }

    @Override
    public void addOption(IAnswer answer) {
        if (type == QuestionType.OPEN_ENDED)
            throw new IllegalStateException("Open-ended question can't have options");
        options.add(answer);
    }

    @Override
    public boolean checkAnswersById(Collection<String> chosenIds) {
        if (type == QuestionType.OPEN_ENDED)
            throw new IllegalStateException("Use checkOpenEndedAnswer for open-ended question");

        Set<String> correctIds = options.stream()
                .filter(IAnswer::isCorrect)
                .map(IAnswer::getId)
                .collect(Collectors.toSet());

        return correctIds.equals(new HashSet<>(chosenIds));
    }

    @Override
    public boolean checkOpenEndedAnswer(String answer) {
        return acceptedOpenAnswers.contains(answer.trim().toLowerCase());
    }

    @Override
    public List<String> getCorrectAnswerTexts() {
        if (type == QuestionType.OPEN_ENDED)
            return new ArrayList<>(acceptedOpenAnswers);
        return options.stream()
                .filter(IAnswer::isCorrect)
                .map(IAnswer::getText)
                .collect(Collectors.toList());
    }

    @Override
    public String getId() { return id; }

    @Override
    public String getText() { return text; }

    @Override
    public QuestionType getType() { return type; }

    public List<IAnswer> getOptions() {
        return Collections.unmodifiableList(options);
    }

}
