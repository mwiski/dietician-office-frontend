package pl.mwiski.dieticianfrontend.clients.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "user", url = "http://localhost:8081/v1/users")
public interface UserClient {

    @GetMapping(value = "${api.key}", consumes = APPLICATION_JSON_VALUE)
    List<UserDto> getAll();

    @GetMapping("{userId}/${api.key}")
    UserDto get(@PathVariable(value = "userId") final long userId);

    @GetMapping("name/{username}/${api.key}")
    UserDto getUserByLogin(@PathVariable(value = "username") final String username);

    @PostMapping(value = "${api.key}", consumes = APPLICATION_JSON_VALUE)
    UserDto add(@RequestBody @Valid final UserDto userDto);

    @PostMapping(value = "${api.key}", consumes = APPLICATION_JSON_VALUE)
    void addAdmin(@RequestBody @Valid final UserDto userDto);

    @PutMapping("${api.key}")
    UserDto update(@RequestBody @Valid final UserDto userDto);

    @DeleteMapping("{userId}/${api.key}")
    void delete(@PathVariable(value = "userId") final long userId);
}
