package pl.mwiski.dieticianfrontend.clients.question;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "question", url = "http://localhost:8081/v1/questions")
public interface QuestionClient {

    @GetMapping("${api.key}")
    List<QuestionDto> getAll();

    @GetMapping("users/{userId}/${api.key}")
    List<QuestionDto> getUserQuestions(@PathVariable(value = "userId") final long userId);

    @PostMapping(value = "${api.key}", consumes = APPLICATION_JSON_VALUE)
    QuestionDto add(@RequestBody final QuestionDto questionDto);

    @PutMapping("{questionId}/${api.key}")
    QuestionDto edit(@PathVariable(value = "questionId") final long questionId, @RequestParam(value = "content") final String content);
    
    @DeleteMapping("{questionId}/${api.key}")
    void delete(@PathVariable(value = "questionId") final long questionId);
}
