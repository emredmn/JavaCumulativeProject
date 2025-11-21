import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuizFileHandler {

    // --- SAVE ---

    public void saveQuiz(QuizManager<?> quiz, String filename) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        List<? extends IQuestion> questions = quiz.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            IQuestion q = questions.get(i);
            json.append(serializeQuestion(q));
            if (i < questions.size() - 1) {
                json.append(",\n");
            }
        }

        json.append("\n]");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(json.toString());
        }
    }

    private String serializeQuestion(IQuestion q) {
        StringBuilder sb = new StringBuilder();
        sb.append("  {\n");
        sb.append("    \"text\": \"").append(escape(q.getText())).append("\",\n");

        if (q instanceof MultipleChoiceQuestion) {
            MultipleChoiceQuestion<?> mcq = (MultipleChoiceQuestion<?>) q;
            List<? extends IAnswer<?>> options = mcq.getOptions();

            // Options list
            sb.append("    \"options\": [");
            for (int i = 0; i < options.size(); i++) {
                sb.append("\"").append(escape(options.get(i).getValue().toString())).append("\"");
                if (i < options.size() - 1)
                    sb.append(", ");
            }
            sb.append("],\n");

            // Correct indices
            sb.append("    \"correct\": [");
            List<Integer> correctIndices = new ArrayList<>();
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).isCorrect()) {
                    correctIndices.add(i);
                }
            }
            for (int i = 0; i < correctIndices.size(); i++) {
                sb.append(correctIndices.get(i));
                if (i < correctIndices.size() - 1)
                    sb.append(", ");
            }
            sb.append("]\n");

        } else if (q instanceof OpenEndedQuestion) {
            OpenEndedQuestion oeq = (OpenEndedQuestion) q;
            sb.append("    \"answers\": [");
            List<String> answers = new ArrayList<>(oeq.getAcceptedAnswers());
            for (int i = 0; i < answers.size(); i++) {
                sb.append("\"").append(escape(answers.get(i))).append("\"");
                if (i < answers.size() - 1)
                    sb.append(", ");
            }
            sb.append("]\n");
        }

        sb.append("  }");
        return sb.toString();
    }

    private String escape(String s) {
        return s.replace("\"", "\\\"").replace("\n", "\\n");
    }

    // --- LOAD ---

    public QuizManager<IQuestion> loadQuiz(String filename) throws IOException {
        QuizManager<IQuestion> quiz = new QuizManager<>();
        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line.trim());
            }
        }

        String json = jsonContent.toString();
        // Remove outer brackets
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
        }

        // Split by objects
        List<String> objects = splitObjects(json);

        for (String obj : objects) {
            if (obj.trim().isEmpty())
                continue;
            IQuestion q = parseQuestion(obj);
            if (q != null) {
                quiz.addQuestion(q);
            }
        }

        return quiz;
    }

    private List<String> splitObjects(String json) {
        List<String> objs = new ArrayList<>();
        int braceCount = 0;
        StringBuilder current = new StringBuilder();
        for (char c : json.toCharArray()) {
            if (c == '{')
                braceCount++;
            if (c == '}')
                braceCount--;

            current.append(c);

            if (braceCount == 0 && c == '}') {
                objs.add(current.toString());
                current = new StringBuilder();
            } else if (braceCount == 0 && c == ',') {
                // Skip comma between objects
                current = new StringBuilder();
            }
        }
        return objs;
    }

    private IQuestion parseQuestion(String json) {
        String text = extractValue(json, "text");

        // Infer type based on fields
        if (json.contains("\"options\":")) {
            // Multiple Choice
            MultipleChoiceQuestion<String> mcq = new MultipleChoiceQuestion<>(text);

            String optionsJson = extractArray(json, "options");
            List<String> optionValues = parseStringList(optionsJson);

            String correctJson = extractArray(json, "correct");
            List<Integer> correctIndices = parseIntList(correctJson);

            for (int i = 0; i < optionValues.size(); i++) {
                boolean isCorrect = correctIndices.contains(i);
                mcq.addOption(new Answer<>(optionValues.get(i), isCorrect));
            }
            return mcq;

        } else if (json.contains("\"answers\":")) {
            // Open Ended
            String answersJson = extractArray(json, "answers");
            List<String> answers = parseStringList(answersJson);
            return new OpenEndedQuestion(text, answers);
        }

        return null;
    }

    private List<String> parseStringList(String jsonArray) {
        List<String> list = new ArrayList<>();
        // Remove brackets
        if (jsonArray.startsWith("[") && jsonArray.endsWith("]")) {
            jsonArray = jsonArray.substring(1, jsonArray.length() - 1);
        }
        if (jsonArray.trim().isEmpty())
            return list;

        // Split by comma, respecting quotes
        boolean inQuote = false;
        StringBuilder current = new StringBuilder();
        for (char c : jsonArray.toCharArray()) {
            if (c == '\"')
                inQuote = !inQuote;

            if (c == ',' && !inQuote) {
                list.add(current.toString().replace("\"", "").trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        list.add(current.toString().replace("\"", "").trim());
        return list;
    }

    private List<Integer> parseIntList(String jsonArray) {
        List<Integer> list = new ArrayList<>();
        if (jsonArray.startsWith("[") && jsonArray.endsWith("]")) {
            jsonArray = jsonArray.substring(1, jsonArray.length() - 1);
        }
        if (jsonArray.trim().isEmpty())
            return list;

        String[] parts = jsonArray.split(",");
        for (String p : parts) {
            try {
                list.add(Integer.parseInt(p.trim()));
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return list;
    }

    private String extractValue(String json, String key) {
        String keyPattern = "\"" + key + "\":";
        int start = json.indexOf(keyPattern);
        if (start == -1)
            return "";
        start += keyPattern.length();

        // Find start of value
        while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '"')) {
            start++;
        }

        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private String extractArray(String json, String key) {
        String keyPattern = "\"" + key + "\":";
        int start = json.indexOf(keyPattern);
        if (start == -1)
            return "";
        start += keyPattern.length();

        while (start < json.length() && json.charAt(start) != '[')
            start++;

        int bracketCount = 0;
        int end = start;
        do {
            char c = json.charAt(end);
            if (c == '[')
                bracketCount++;
            if (c == ']')
                bracketCount--;
            end++;
        } while (bracketCount > 0 && end < json.length());

        return json.substring(start, end);
    }
}
