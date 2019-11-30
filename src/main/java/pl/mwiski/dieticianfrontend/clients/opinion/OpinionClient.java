package pl.mwiski.dieticianfrontend.clients.opinion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "opinion", url = "http://localhost:8081/v1/opinions")
public interface OpinionClient {

    @GetMapping("${api.key}")
    List<OpinionDto> getAll();

    @GetMapping("users/{userId}/${api.key}")
    List<OpinionDto> getUserOpinions(@PathVariable(value = "userId") final long userId);

    @PostMapping(value = "${api.key}", consumes = APPLICATION_JSON_VALUE)
    OpinionDto add(@RequestBody final OpinionDto opinionDto);

    @PutMapping("{opinionId}/${api.key}")
    OpinionDto edit(@PathVariable(value = "opinionId") final long opinionId, @RequestParam(value = "content") final String content);

    @DeleteMapping("{opinionId}/${api.key}")
    void delete(@PathVariable(value = "opinionId") final long opinionId);
}
