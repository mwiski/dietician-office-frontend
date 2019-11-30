package pl.mwiski.dieticianfrontend.clients.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionClient questionClient;

    public List<QuestionDto> getAll() {
        return questionClient.getAll();
    }

    public List<QuestionDto> getUserQuestions(final long userId) {
        return questionClient.getUserQuestions(userId);
    }

    public QuestionDto add(final QuestionDto questionDto) {
        return questionClient.add(questionDto);
    }

    public QuestionDto edit(final long questionId, final String content) {
        return questionClient.edit(questionId, content);
    }

    public void delete(final long questionId) {
        questionClient.delete(questionId);
    }
}
