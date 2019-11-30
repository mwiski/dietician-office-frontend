package pl.mwiski.dieticianfrontend.clients.question;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mwiski.dieticianfrontend.clients.dietician.SimpleDieticianDto;
import pl.mwiski.dieticianfrontend.clients.user.SimpleUserDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionDto {

    private long id;
    private String question;
    private String addedAt;
    private SimpleUserDto user;
    private List<SimpleDieticianDto> dieticians;
}
