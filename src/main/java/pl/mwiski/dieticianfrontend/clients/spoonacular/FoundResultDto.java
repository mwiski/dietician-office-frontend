package pl.mwiski.dieticianfrontend.clients.spoonacular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoundResultDto {

    private int id;
    private String title;
    private int readyInMinutes;
    private int servings;
}
