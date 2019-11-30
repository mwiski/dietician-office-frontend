package pl.mwiski.dieticianfrontend.clients.spoonacular;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpoonacularService {

    private final SpoonacularClient spoonacularClient;

    public List<FoundResultDto> getRecipes(final String query) {
        return spoonacularClient.getRecipes(query);
    }

    public FoundRecipeDto getRecipe(final int id) {
        return spoonacularClient.getRecipe(id);
    }
}
