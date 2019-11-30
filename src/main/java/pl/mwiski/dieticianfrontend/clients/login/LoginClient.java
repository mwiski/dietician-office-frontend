package pl.mwiski.dieticianfrontend.clients.login;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "login", url = "http://localhost:8081/v1/login")
public interface LoginClient {

    @GetMapping("{username}/${api.key}")
    public LoginDto get(@PathVariable(value = "username") final String username);
}
