package pl.mwiski.dieticianfrontend.clients.edamam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EdamamService {

    private final EdamamClient edamamClient;

    public FoundFoodDto getFoodsNutritionalValue(final String foodName) {
        return edamamClient.getFoodsNutritionalValue(foodName);
    }
}
