package pl.mwiski.dieticianfrontend.clients.edamam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutrientsDto {

    private double kcal;
    private double proteins;
    private double fat;
    private double carbohydrates;
    private double fiber;
}
