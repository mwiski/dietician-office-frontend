package pl.mwiski.dieticianfrontend.clients.edamam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoundFoodDto {

    private String searchedParam;
    private String found;
    private NutrientsDto nutrients;
}
