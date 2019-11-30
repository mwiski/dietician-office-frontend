package pl.mwiski.dieticianfrontend.clients.visit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mwiski.dieticianfrontend.clients.dietician.SimpleDieticianDto;
import pl.mwiski.dieticianfrontend.clients.user.SimpleUserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitDto {

    private long id;
    private String dateTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SimpleUserDto user;
    private SimpleDieticianDto dietician;
    private boolean available;
    private boolean completed;
}
