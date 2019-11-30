package pl.mwiski.dieticianfrontend.clients.answer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mwiski.dieticianfrontend.clients.dietician.SimpleDieticianDto;
import pl.mwiski.dieticianfrontend.clients.question.QuestionDto;
import pl.mwiski.dieticianfrontend.clients.user.SimpleUserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerDto {

    private long id;
    private String answer;
    private QuestionDto question;
    private String addedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SimpleUserDto user;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SimpleDieticianDto dietician;
}
