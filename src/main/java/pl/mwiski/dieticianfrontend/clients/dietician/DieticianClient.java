package pl.mwiski.dieticianfrontend.clients.dietician;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "dietician", url = "http://localhost:8081/v1/dieticians")
public interface DieticianClient {

    @GetMapping("${api.key}")
    List<DieticianDto> getAll();

    @GetMapping("{dieticianId}/${api.key}")
    DieticianDto get(@PathVariable(value = "dieticianId") final long dieticianId);

    @PostMapping(value = "${api.key}", consumes = APPLICATION_JSON_VALUE)
    DieticianDto add(@RequestBody final DieticianDto dieticianDto);

    @PutMapping("${api.key}")
    DieticianDto update(@RequestBody final DieticianDto dieticianDto);

    @DeleteMapping("{dieticianId}/${api.key}")
    void delete(@PathVariable(value = "dieticianId") final long dieticianId);
}
