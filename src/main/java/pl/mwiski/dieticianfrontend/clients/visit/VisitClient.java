package pl.mwiski.dieticianfrontend.clients.visit;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "visit", url = "http://localhost:8081/v1/visits")
public interface VisitClient {

    @GetMapping("${api.key}")
    List<VisitDto> getAll();

    @GetMapping("date/${api.key}")
    List<VisitDto> getAvailableVisits(@RequestParam(value = "date") final String date);

    @GetMapping("users/{userId}/${api.key}")
    List<VisitDto> getUserVisits(@PathVariable(value = "userId") final long userId);

    @GetMapping("dieticians/{dieticianId}/${api.key}")
    List<VisitDto> getDieticianVisits(@PathVariable(value = "dieticianId") final long dieticianId);

    @GetMapping("{visitId}/${api.key}")
    VisitDto get(@PathVariable(value = "visitId") final long visitId);

    @PostMapping(value = "${api.key}", consumes = APPLICATION_JSON_VALUE)
    VisitDto add(@RequestBody final VisitDto visitDto);

    @PutMapping("{visitId}/users/{userId}/${api.key}")
    VisitDto schedule(@PathVariable(value = "visitId") final long visitId, @PathVariable(value = "userId") long userId);

    @DeleteMapping("{visitId}/${api.key}")
    void cancel(@PathVariable(value = "visitId") final long visitId);
}
