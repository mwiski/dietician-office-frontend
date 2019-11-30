package pl.mwiski.dieticianfrontend.clients.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginClient loginClient;

    public LoginDto get(final String username) {
        return loginClient.get(username);
    }
}
