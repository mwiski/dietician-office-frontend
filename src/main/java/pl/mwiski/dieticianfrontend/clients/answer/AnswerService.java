package pl.mwiski.dieticianfrontend.clients.answer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerClient answerClient;

    public List<AnswerDto> getAll() {
        return answerClient.getAll();
    }

    public List<AnswerDto> getDieticianAnswers(final long dieticianId) {
        return answerClient.getDieticianAnswers(dieticianId);
    }

    public AnswerDto add(final AnswerDto answerDto) {
        return answerClient.add(answerDto);
    }

    public AnswerDto edit(final long answerId, final String content) {
        return answerClient.edit(answerId, content);
    }

    public void delete(final long answerId) {
        answerClient.delete(answerId);
    }
}
