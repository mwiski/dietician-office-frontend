package pl.mwiski.dieticianfrontend.clients.edamam;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "edamam", url = "http://localhost:8081/v1/edamam")
public interface EdamamClient {

    @GetMapping("foods/{foodName}/${api.key}")
    FoundFoodDto getFoodsNutritionalValue(@PathVariable(value = "foodName") final String foodName);
}
