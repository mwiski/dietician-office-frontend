package pl.mwiski.dieticianfrontend.clients.spoonacular;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "spoonacular", url = "http://localhost:8081/v1/spoonacular")
public interface SpoonacularClient {

    @GetMapping("recipes/{query}/${api.key}")
    List<FoundResultDto> getRecipes(@PathVariable(value = "query") final String query);

    @GetMapping("recipes/info/{id}/${api.key}")
    FoundRecipeDto getRecipe(@PathVariable(value = "id") final int id);
}
