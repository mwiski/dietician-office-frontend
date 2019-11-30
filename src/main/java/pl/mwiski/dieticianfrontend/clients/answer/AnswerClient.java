package pl.mwiski.dieticianfrontend.clients.answer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "answer", url = "http://localhost:8081/v1/answers")
public interface AnswerClient {

    @GetMapping("${api.key}")
    List<AnswerDto> getAll();

    @GetMapping("dieticians/{dieticianId}/${api.key}")
    List<AnswerDto> getDieticianAnswers(@PathVariable(value = "dieticianId") final long dieticianId);

    @PostMapping(value = "${api.key}", consumes = APPLICATION_JSON_VALUE)
    AnswerDto add(@RequestBody final AnswerDto answerDto);

    @PutMapping("{answerId}/${api.key}")
    AnswerDto edit(@PathVariable(value = "answerId") final long answerId, @RequestParam(value = "content") final String content);

    @DeleteMapping("{answerId}/${api.key}")
    void delete(@PathVariable(value = "answerId") final long answerId);
}
