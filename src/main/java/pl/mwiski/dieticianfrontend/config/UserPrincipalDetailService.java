package pl.mwiski.dieticianfrontend.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.mwiski.dieticianfrontend.clients.login.LoginDto;
import pl.mwiski.dieticianfrontend.clients.login.LoginService;

@Service
public class UserPrincipalDetailService implements UserDetailsService {

    private LoginService loginService;

    public UserPrincipalDetailService(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginDto login = this.loginService.get(username);
        if (login == null) {
            throw new UsernameNotFoundException("Could not find user " + username);
        }

        return new UserPrincipal(login);
    }
}
