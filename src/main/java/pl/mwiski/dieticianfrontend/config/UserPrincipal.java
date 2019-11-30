package pl.mwiski.dieticianfrontend.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.mwiski.dieticianfrontend.clients.login.LoginDto;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;
    private LoginDto login;

    public UserPrincipal(LoginDto login) {
        this.login = login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.login.getRole()));
    }

    @Override
    public String getPassword() {
        return this.login.getPassword();
    }

    @Override
    public String getUsername() {
        return this.login.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
